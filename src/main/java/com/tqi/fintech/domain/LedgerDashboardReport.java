package com.tqi.fintech.domain;

import java.math.BigDecimal;

public record LedgerDashboardReport(
    BigDecimal totalVolume,
    long discrepancyCount,
    double accuracyRate
) {}
