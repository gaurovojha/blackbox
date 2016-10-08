package com.blackbox.ids.core.dao.impl;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.dao.NumberFormatValidationDao;
import com.blackbox.ids.core.model.ValidationMatrix;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.NumberType;

@Repository("numberFormatValidationDaoImpl")
public class NumberFormatValidationDaoImpl implements NumberFormatValidationDao {

	/** Instance used to interact with persistence context. */
	@PersistenceContext(unitName = "entityManagerFactory")
	private EntityManager entityManager;

	/**
	 * This method is used to return ValidationMatrix list based on given condition
	 */
	@Override
	public List<ValidationMatrix> getValidationMatrix(NumberType numberType,String jurisdiction,ApplicationType applicationType,Calendar filingDate,Calendar grantDate) {
		TypedQuery<ValidationMatrix> query=entityManager.createNamedQuery("findByType", ValidationMatrix.class);
		query.setParameter("numberType", numberType);
		query.setParameter("jurisdiction", jurisdiction);
		query.setParameter("applicationType", applicationType);
		query.setParameter("defaultType", ApplicationType.ALL);
		query.setParameter("filingDate", filingDate);
		query.setParameter("grantDate", grantDate);
		return query.getResultList();
	}

}
