package com.tqi.fintech.application.output;

import java.math.BigDecimal;

public interface AiAuditorEngine {
    String auditDiscrepancy(String transactionId, BigDecimal amount, String operationType);
}
