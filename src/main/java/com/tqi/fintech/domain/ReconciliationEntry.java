package com.tqi.fintech.domain;

import java.math.BigDecimal;

public record ReconciliationEntry(
    String transactionId,
    BigDecimal amount,
    String operationType
) {
    public ReconciliationEntry {
        if (transactionId == null || transactionId.isBlank()) {
            throw new IllegalArgumentException("ID da transação é obrigatório.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transação deve ser estritamente maior que zero.");
        }
        if (!"CREDIT".equals(operationType) && !"DEBIT".equals(operationType)) {
            throw new IllegalArgumentException("Tipo de operação deve ser CREDIT ou DEBIT.");
        }
    }
}
