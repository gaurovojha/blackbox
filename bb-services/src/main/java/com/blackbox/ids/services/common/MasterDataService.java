package com.blackbox.ids.services.common;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.mstr.AssigneeAttorneyDocketNo;
import com.blackbox.ids.core.model.mstr.AttorneyDocketFormat;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.dto.AssigneeDTO;
import com.blackbox.ids.dto.CustomerDTO;
import com.blackbox.ids.dto.JurisdictionDTO;
import com.blackbox.ids.dto.OrganizationDTO;
import com.blackbox.ids.dto.TechnologyGroupDTO;
import com.blackbox.ids.dto.usermanagement.ModuleDTO;

/**
 * Handles reference master data related to service requests.
 *
 * @author Nagarro
 */
public interface MasterDataService {

	public List<JurisdictionDTO> getAllJurisdictions() throws ApplicationException;

	public List<AssigneeDTO> getAllAssignees() throws ApplicationException;

	public List<CustomerDTO> getAllCustomers() throws ApplicationException;

	public List<TechnologyGroupDTO> getAllTechnologyGroups() throws ApplicationException;

	public List<OrganizationDTO> getAllOrganizations() throws ApplicationException;

	public List<ModuleDTO> getAllModules() throws ApplicationException;

	long updateAssignee(String familyId, String strAssignee, long notificationId);

	/*- APIs to fetch data as per the logged in users data access rights. */
	public List<Jurisdiction> getUserJurisdictions();

	public List<String> getUserAssignees();

	public List<String> getUserCustomerNumbers();

	/*- APIs to map entity identifier codes with their DB Ids */
	public Map<String, Long> mapAssigneeNameIds(Collection<String> assigneeNames);

	public Map<String, Long> mapCustomerNameIds(Collection<String> customerNumbers);

	public Map<String, Long> mapJurisdictionNameIds(Collection<String> jurisdictionCodes);

	public Map<String, Long> mapOrganizationNameIds(Collection<String> organizationNames);

	public List<String> getAllEntity() throws ApplicationException;

	public boolean isValidJurisdiction(final String jurisdictionCode);

	public AttorneyDocketFormat getAttorneyDocketFormat();

	public String getAttorneyDocketSeperator();

	public List<AssigneeAttorneyDocketNo> getAssigneeAttorneyDocketNo(String segmentValue) ;

}
