package com.tqi.fintech.infrastructure

import com.tqi.fintech.application.output.AiAuditorEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ReconciliationAnalysisService(
    private val aiEngine: AiAuditorEngine
) {

    // Função suspensível que delega a chamada bloqueante de I/O para o pool otimizado das Coroutines
    suspend fun analisarDivergenciaFisica(transactionId: String, amount: BigDecimal, type: String): String {
        return withContext(Dispatchers.IO) {
            // Executa a chamada ao motor que futuramente invocará o Gemini
            aiEngine.auditDiscrepancy(transactionId, amount, type)
        }
    }
}
