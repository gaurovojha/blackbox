/**
 *
 */
package com.blackbox.ids.core.dao.mstr;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.model.mstr.Assignee;

/**
 * @author ajay2258
 *
 */
public interface AssigneeDAO extends BaseDAO<Assignee, Long> {

	public Map<String, Long> mapAssigneeNameIds(final Collection<String> assigneeNames);

	public List<String> getAssigeesByIdsIn(final Collection<Long> assigneeIds);

}
