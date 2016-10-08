/**
 * 
 */
package com.blackbox.ids.services.impl.jurisdiction;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.repository.JurisdictionRepository;
import com.blackbox.ids.services.jurisdiction.IJurisdictionService;

/**
 * @author gurtegsingh
 *
 */
@Service
public class JurisdictionServiceImpl implements IJurisdictionService {

	@Autowired
	JurisdictionRepository jurisdictionRepository;
	
	/* (non-Javadoc)
	 * @see com.blackbox.ids.services.jurisdiction.IJurisdictionService#findAllJurisdictionsValues()
	 */
	@Override
	public List<String> findAllJurisdictionsValues() {
		List<Jurisdiction> jurisdiction = jurisdictionRepository.findAll();
		List<String> jurisdictionValues=new ArrayList<>();
		for (Jurisdiction jurisdiction2 : jurisdiction) {
			jurisdictionValues.add(jurisdiction2.getCode());
		}
		return jurisdictionValues;
	}

}
