package com.tqi.fintech.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReconciliationBatch(
    String batchId,
    BigDecimal totalVolume,
    Long transactionCount,
    String status,
    LocalDateTime processedAt
) {
    public ReconciliationBatch {
        if (totalVolume == null || totalVolume.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O volume total do lote não pode ser negativo ou nulo.");
        }
        if (transactionCount == null || transactionCount < 0) {
            throw new IllegalArgumentException("A contagem de transações não pode ser negativa.");
        }
    }
}
