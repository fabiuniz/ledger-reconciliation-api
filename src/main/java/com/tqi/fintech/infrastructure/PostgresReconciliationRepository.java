package com.tqi.fintech.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostgresReconciliationRepository extends JpaRepository<ReconciliationBatchEntity, String> {
}
