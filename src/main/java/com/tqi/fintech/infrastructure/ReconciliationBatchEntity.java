package com.tqi.fintech.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reconciliation_batches")
public class ReconciliationBatchEntity {

    @Id
    private String batchId;
    private BigDecimal totalVolume;
    private Long transactionCount;
    private String status;
    private LocalDateTime processedAt;

    // Construtores padrão para o Hibernate
    public ReconciliationBatchEntity() {}

    public ReconciliationBatchEntity(String batchId, BigDecimal totalVolume, Long transactionCount, String status, LocalDateTime processedAt) {
        this.batchId = batchId;
        this.totalVolume = totalVolume;
        this.transactionCount = transactionCount;
        this.status = status;
        this.processedAt = processedAt;
    }

    // Getters e Setters
    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }
    public BigDecimal getTotalVolume() { return totalVolume; }
    public void setTotalVolume(BigDecimal totalVolume) { this.totalVolume = totalVolume; }
    public Long getTransactionCount() { return transactionCount; }
    public void setTransactionCount(Long transactionCount) { this.transactionCount = transactionCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
}
