package com.blackbox.ids.core.repository.mdm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.model.mstr.AttorneyDocketNumber;


@Repository
public interface AttorneyDocketRepository extends JpaRepository<AttorneyDocketNumber, Long> {
	
	@Query("Select adn From AttorneyDocketNumber adn where adn.segment = :segment")
	AttorneyDocketNumber findAttorneyDocketNumberByValue(@Param("segment") final String segment);
}
