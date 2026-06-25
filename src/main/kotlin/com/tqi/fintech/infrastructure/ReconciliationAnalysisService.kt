package com.tqi.fintech.infrastructure

import com.tqi.fintech.application.output.AiAuditorEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ReconciliationAnalysisService(
    private val aiEngine: AiAuditorEngine
) {

    suspend fun analisarDivergenciaFisica(transactionId: String, amount: BigDecimal, type: String): String {
        return withContext(Dispatchers.IO) {
            aiEngine.auditDiscrepancy(transactionId, amount, type)
        }
    }

    // Task 3.4: Concorrência Estruturada cruzando dados regulatórios e de score de risco
    suspend fun executarAuditoriaComplexaEmParalelo(transactionId: String, amount: BigDecimal): Map<String, String> = coroutineScope {
        
        // Dispara a primeira análise assíncrona
        val scoreDeferred = async(Dispatchers.IO) {
            delay(50) // Simula latência de rede/cálculo
            "RISCO_BAIXO"
        }

        // Dispara a segunda análise regulatória em paralelo
        val conformidadeDeferred = async(Dispatchers.IO) {
            delay(40) // Simula verificação de tabela do Bacen
            "COMPLIANT"
        }

        // Aguarda o resultado de ambas usando await() de forma não-bloqueante
        mapOf(
            "transactionId" to transactionId,
            "riskScore" to scoreDeferred.await(),
            "complianceStatus" to conformidadeDeferred.await(),
            "amount" to amount.toPlainString()
        )
    }
}
