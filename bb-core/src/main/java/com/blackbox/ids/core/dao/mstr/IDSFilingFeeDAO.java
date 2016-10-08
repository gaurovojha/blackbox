/**
 *
 */
package com.blackbox.ids.core.dao.mstr;

import java.util.Calendar;

import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.model.mstr.Assignee.Entity;
import com.blackbox.ids.core.model.mstr.IDSFilingFee;

/**
 * @author ajay2258
 *
 */
public interface IDSFilingFeeDAO extends BaseDAO<IDSFilingFee, Long> {

	public Double findFeeByEntityOn(Entity entity, Calendar instance);

}
