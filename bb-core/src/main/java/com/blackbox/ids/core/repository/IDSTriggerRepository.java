package com.blackbox.ids.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.model.IDS.IDSTrigger;

@Repository
public interface IDSTriggerRepository extends JpaRepository<IDSTrigger, Long> {

}
