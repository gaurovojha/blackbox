package com.blackbox.ids.core.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.model.mstr.Assignee;

/**
 * A DAO for the entity Assignee is simply created by extending the JpaRepository interface provided by spring and
 * custom repository. The following methods are some of the ones available from former interface: save, delete,
 * deleteAll, findOne and findAll. The magic is that such methods must not be implemented, and moreover it is possible
 * create new query methods working only by defining their signature! Custom interface can be used to define custom
 * query methods that can not be implemented by Spring.
 * @author Nagarro
 */
@Repository("assigneeRepository")
public interface AssigneeRepository extends JpaRepository<Assignee, Long> {

	public Set<Assignee> findByIdIn(Set<Long> assigneeIds);

	@Query(value = "Select assignee.name From Assignee assignee Where assignee.id IN (:assignees) And assignee.id > 0")
	public List<String> findNamesByIdsIn(@Param("assignees") final Collection<Long> userAssignees);

	public Assignee findByName(String name);
}
