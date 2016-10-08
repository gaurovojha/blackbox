/**
 *
 */
package com.blackbox.ids.core.dao.impl.mstr;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dao.mstr.AssigneeDAO;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.QAssignee;

/**
 * @author ajay2258
 *
 */
@Repository
public class AssigneeDaoImpl extends BaseDaoImpl<Assignee, Long> implements AssigneeDAO {

	@Override
	public Map<String, Long> mapAssigneeNameIds(Collection<String> assigneeNames) {
		Collection<String> upperNames = assigneeNames.stream().map(String::toUpperCase).collect(Collectors.toSet());
		QAssignee assignee = QAssignee.assignee;
		return getJPAQuery().from(assignee)
				.where(assignee.name.upper().in(upperNames))
				.map(assignee.name.upper(), assignee.id);
	}
	
	public List<String> getAssigeesByIdsIn(final Collection<Long> assigneeIds) {
		QAssignee assignee = QAssignee.assignee;
		return getJPAQuery().from(assignee)
				.where(assignee.id.gt(0L).and(assignee.id.in(assigneeIds)))
				.list(assignee.name);
	}

}
