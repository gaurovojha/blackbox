package com.blackbox.ids.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blackbox.ids.core.model.Nationality;

public interface NationalityRepository extends JpaRepository<Nationality,Long> {

}
