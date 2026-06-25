package com.tqi.fintech.infrastructure;

import com.tqi.fintech.application.output.ReconciliationRepository;
import com.tqi.fintech.domain.ReconciliationBatch;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReconciliationRepositoryAdapter implements ReconciliationRepository {

    private final PostgresReconciliationRepository jpaRepository;

    public ReconciliationRepositoryAdapter(PostgresReconciliationRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ReconciliationBatch save(ReconciliationBatch batch) {
        ReconciliationBatchEntity entity = new ReconciliationBatchEntity(
            batch.batchId(),
            batch.totalVolume(),
            batch.transactionCount(),
            batch.status(),
            batch.processedAt()
        );
        jpaRepository.save(entity);
        return batch;
    }

    @Override
    public List<ReconciliationBatch> findAll() {
        return jpaRepository.findAll().stream()
            .map(entity -> new ReconciliationBatch(
                entity.getBatchId(),
                entity.getTotalVolume(),
                entity.getTransactionCount(),
                entity.getStatus(),
                entity.getProcessedAt()
            ))
            .collect(Collectors.toList());
    }
}
