/**
 *
 */
package com.blackbox.ids.core.dao.impl.mstr;

import java.util.Calendar;

import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dao.mstr.IDSFilingFeeDAO;
import com.blackbox.ids.core.model.mstr.Assignee.Entity;
import com.blackbox.ids.core.model.mstr.IDSFilingFee;
import com.blackbox.ids.core.model.mstr.QIDSFilingFee;

/**
 * @author ajay2258
 *
 */
@Repository
public class IDSFilingFeeDaoImpl extends BaseDaoImpl<IDSFilingFee, Long> implements IDSFilingFeeDAO {

	@Override
	public Double findFeeByEntityOn(Entity entity, Calendar filingOn) {
		QIDSFilingFee filingFee = QIDSFilingFee.iDSFilingFee;

		return getJPAQuery().from(filingFee)
				.where(filingFee.entity.eq(entity)
						.and(filingFee.startDate.loe(filingOn))
						.andAnyOf(filingFee.endDate.isNull(), filingFee.endDate.gt(filingOn)))
				.uniqueResult(filingFee.fee);
	}

}
