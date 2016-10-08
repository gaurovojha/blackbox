package com.blackbox.ids.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blackbox.ids.core.model.AssociatedAgent;

public interface AssociatedAgentRepository extends JpaRepository<AssociatedAgent, Long> {

}
