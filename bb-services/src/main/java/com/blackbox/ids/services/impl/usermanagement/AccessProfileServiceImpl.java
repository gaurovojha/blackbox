package com.blackbox.ids.services.impl.usermanagement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.model.usermanagement.AccessProfile;
import com.blackbox.ids.core.model.usermanagement.Permission;
import com.blackbox.ids.core.repository.RoleRepository;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.usermanagement.AccessProfileRepository;
import com.blackbox.ids.core.repository.usermanagement.PermissionRepository;
import com.blackbox.ids.dto.RoleDTO;
import com.blackbox.ids.dto.usermanagement.AccessProfileDTO;
import com.blackbox.ids.services.usermanagement.AccessProfileService;

/**
 * Handles all the access profile related request and perform business logic
 * 
 * @author Nagarro
 *
 */
@Service("accessProfileService")
public class AccessProfileServiceImpl implements AccessProfileService {

	private final static Logger log = Logger.getLogger(AccessProfileServiceImpl.class);

	// date format
	private final static DateFormat format = new SimpleDateFormat("MMM dd, yyyy");
	
	private final static DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");

	@Autowired
	private AccessProfileRepository accessProfileRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	@Transactional
	public AccessProfileDTO getAccessProfileByName(String name) throws ApplicationException {
		AccessProfileDTO accessProfileDto;

		try {
			AccessProfile profile = accessProfileRepository.findAccessProfileByName(name);
			accessProfileDto = getDTOFromAccessProfile(profile);
		} catch (DataAccessException e) {
			log.debug("Exception occurred while getting Access profile " + name, e);
			throw new ApplicationException(10001, "Exception occurred while getting Access profile : " + name, e);
		}

		return accessProfileDto;
	}

	@Override
	@Transactional
	public AccessProfileDTO getAccessProfileById(final Long id) throws ApplicationException {
		AccessProfileDTO accessProfileDto;

		try {
			AccessProfile profile = accessProfileRepository.findOne(id);
			accessProfileDto = getDTOFromAccessProfile(profile);
		} catch (DataAccessException e) {
			log.debug("Exception occurred while getting Access profile " + id, e);
			throw new ApplicationException(10001, "Exception occurred while getting Access profile : " + id, e);
		}

		return accessProfileDto;
	}

	@Override
	@Transactional
	public List<String> getAllAccessProfileNames() throws ApplicationException {
		List<String> profileNames = null;

		try {
			profileNames = accessProfileRepository.findAllAccessProfileNames();
		} catch (DataAccessException e) {
			log.debug("Exception occurred while getting Access profile names", e);
			throw new ApplicationException(10001, "Exception occurred while getting Access profile Names", e);
		}

		return profileNames;
	}

	@Override
	@Transactional
	public void createAccessProfile(final AccessProfileDTO dto) throws ApplicationException {

		try {
			AccessProfile profile = getAccessProfileFromDTO(dto);
			accessProfileRepository.save(profile);
		} catch (DataIntegrityViolationException e) {
			log.debug("Exception occurred while saving Access profile : " + dto.getId(), e);
			throw new ApplicationException(10002, "Exception occurred while saving Access profile " + dto.getId(), e);
		}
	}

	@Override
	@Transactional
	public void updateAccessProfile(final AccessProfileDTO dto) throws ApplicationException {

		try {
			AccessProfile profile = accessProfileRepository.findOne(Long.valueOf(dto.getId()));
			populateAccessProfileFromDTO(profile, dto);
			accessProfileRepository.save(profile);
		} catch (DataIntegrityViolationException e) {
			log.debug("Exception occurred while saving Access profile : " + dto.getId(), e);
			throw new ApplicationException(10002, "Exception occurred while saving Access profile " + dto.getId(), e);
		}
	}

	@Override
	@Transactional
	public List<AccessProfileDTO> getAllAccessProfiles() throws ApplicationException {
		List<AccessProfileDTO> accessProfileDtos = new ArrayList<>();

		try {
			List<AccessProfile> accessProfiles = accessProfileRepository.findAll();

			for (AccessProfile accessProfile : accessProfiles) {
				AccessProfileDTO accessProfileDto = getDTOFromAccessProfile(accessProfile);
				accessProfileDtos.add(accessProfileDto);
			}
		} catch (DataAccessException e) {
			log.debug("Exception occurred while getting Access profile ", e);
			throw new ApplicationException(10001, "Exception occurred while getting Access profiles : ", e);
		}

		return accessProfileDtos;
	}

	@Override
	@Transactional
	public void removeAccessProfile(final Long id) {
		try {
			accessProfileRepository.delete(id);
		} catch (DataIntegrityViolationException e) {
			log.debug("Exception occurred while deleting access profile : " + id, e);
			throw new ApplicationException(10004, "Exception occurred while deleting access profile : " + id, e);
		}
	}

	@Override
	@Transactional
	public boolean deactivate(final Long id) {
		try {
			AccessProfile profile = accessProfileRepository.findOne(id);
			if(!profile.isSeeded()) {
				return accessProfileRepository.deactivateAccessProfile(id);
			}
		} catch (Exception e) {
			log.debug("Exception occurred while deactivating access profile : " + id, e);
			throw new ApplicationException(10004, "Exception occurred while updating access profile id " + id, e);
		}
		return false;
	}

	@Override
	@Transactional
	public long getUserCount(final Long id) throws ApplicationException {
		try {
			return accessProfileRepository.findUserIdByAccessProfileId(id).size();
		} catch (DataAccessException e) {
			log.debug("Exception occurred while getting user count for access profile id : " + id, e);
			throw new ApplicationException(10004,
					"Exception occurred while getting user count for access profile id " + id, e);
		}
	}

	@Override
	@Transactional
	public List<AccessProfileDTO> getActiveAccessProfiles() {
		List<AccessProfileDTO> accessProfileDTOList = new ArrayList<AccessProfileDTO>();
		try {
			List<AccessProfile> accessProfileList = accessProfileRepository.findAccessProfileByActive(true);
			for (AccessProfile profile : accessProfileList) {
				AccessProfileDTO dto = getDTOFromAccessProfile(profile);
				accessProfileDTOList.add(dto);
			}
		} catch (Exception e) {
			log.debug("Exception occurred while getting active access profile details: ", e);
			throw new ApplicationException(10004, "Exception occurred while getting active access profile details ", e);
		}
		return accessProfileDTOList;
	}

	/**
	 * Merge access profile and dto
	 * 
	 * @param profile
	 * @param dto
	 */
	private void populateAccessProfileFromDTO(AccessProfile profile, AccessProfileDTO dto) {
		profile.setName(dto.getName());
		profile.setDescription(dto.getDescription());
		populatePermissions(dto, profile);
	}

	/**
	 * Populates access profile from dto
	 * 
	 * @param profile
	 * @return
	 */
	private AccessProfile getAccessProfileFromDTO(AccessProfileDTO dto) {
		AccessProfile accessProfile = new AccessProfile();
		accessProfile.setName(dto.getName());
		accessProfile.setDescription(dto.getDescription());
		accessProfile.setSeeded(false);
		accessProfile.setActive(true);

		populatePermissions(dto, accessProfile);

		return accessProfile;
	}

	/**
	 * @param accessProfileDto
	 * @param profile
	 */
	private void populatePermissions(AccessProfileDTO accessProfileDto, AccessProfile profile) {
		List<Long> accessControlIds = new ArrayList<>();

		for (Long accessControlId : accessProfileDto.getAccessControlIds()) {
			accessControlIds.add(accessControlId);
		}

		List<Permission> permissionList = permissionRepository.findPermissionByAccessControlIdIn(accessControlIds);
		profile.getPermissions().clear();
		profile.getPermissions().addAll(permissionList);
	}

	/**
	 * Populates dto from access profile
	 * 
	 * @param accessProfile
	 * @return
	 */
	private AccessProfileDTO getDTOFromAccessProfile(AccessProfile accessProfile) {
		AccessProfileDTO accessProfileDto = new AccessProfileDTO();
		accessProfileDto.setId(accessProfile.getId());
		accessProfileDto.setName(accessProfile.getName());
		accessProfileDto.setUserCount(getUserCount(accessProfile.getId()));
		accessProfileDto.setActive(accessProfile.isActive());
		accessProfileDto.setSeeded(accessProfile.isSeeded());
		accessProfileDto.setDescription(accessProfile.getDescription());

		if (accessProfile.getEndDate() != null) {
			accessProfileDto.setEndDate(formatDate(accessProfile.getEndDate()));
		}

		accessProfileDto.setCreatedDate(convertCalendar(accessProfile.getCreatedDate()));
		accessProfileDto.setCreatedBy(
				userRepository.getUserFullName(userRepository.getEmailId(accessProfile.getCreatedByUser())));

		if (accessProfile.getUpdatedByUser() != null) {
			accessProfileDto.setUpdatedBy(
					userRepository.getUserFullName(userRepository.getEmailId(accessProfile.getUpdatedByUser())));
		}

		if (accessProfile.getUpdatedDate() != null) {
			accessProfileDto.setUpdatedDate(formatDate(accessProfile.getUpdatedDate()));
		}
		
		List<Long> accessControlIds = new ArrayList<>();
		
		for (Permission permission : accessProfile.getPermissions()) {
			accessControlIds.add(permission.getAccessControlId());
		}
		
		accessProfileDto.getAccessControlIds().addAll(accessControlIds);

		// setting roles
		List<RoleDTO> rdtoList = new ArrayList<RoleDTO>();
		List<Role> roles = roleRepository.findByAccessProfile(accessProfile);

		for (Role role : roles) {
			RoleDTO rdto = new RoleDTO();
			rdto.setId(role.getId());
			rdto.setName(role.getName());
			rdtoList.add(rdto);
		}

		accessProfileDto.setRoles(rdtoList);
		
		return accessProfileDto;
	}

	/**
	 * Formats date to string
	 * 
	 * @param date
	 * @return
	 */
	private String formatDate(Calendar calendar) {
		if (calendar != null) {
			return format.format(calendar.getTime());
		}
		return "";
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	private String convertCalendar(Calendar date) {
		String stringDate = null;
		if (date != null) {
			stringDate = formatter.format(date.getTime());
		}
		return stringDate;
	}
}