package com.tqi.fintech.application.output;

import com.tqi.fintech.domain.ReconciliationBatch;
import java.util.List;

public interface ReconciliationRepository {
    ReconciliationBatch save(ReconciliationBatch batch);
    List<ReconciliationBatch> findAll();
}
