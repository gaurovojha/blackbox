package com.blackbox.ids.core.dao;

import java.util.Calendar;
import java.util.List;

import com.blackbox.ids.core.model.ValidationMatrix;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.NumberType;

public interface NumberFormatValidationDao {

	public List<ValidationMatrix> getValidationMatrix(NumberType numberType,String jurisdiction,ApplicationType applicationType,Calendar filingDate,Calendar grantDate);

}
