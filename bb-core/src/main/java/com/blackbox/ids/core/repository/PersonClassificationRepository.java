package com.blackbox.ids.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blackbox.ids.core.model.mstr.PersonClassification;

public interface PersonClassificationRepository extends JpaRepository<PersonClassification,Long>{

}
