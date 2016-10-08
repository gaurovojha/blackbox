package com.blackbox.ids.core.repository.correspondence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.model.correspondence.PairAudit;

@Repository("pairAuditRepository")
public interface PairAuditRepository extends JpaRepository<PairAudit, Long> {

}
