package com.tqi.fintech.infrastructure

import com.tqi.fintech.application.output.AiAuditorEngine
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ReconciliationAnalysisService(
    private val aiEngine: AiAuditorEngine // Injeta a porta de domínio da IA
) {

    // Task 3.4: Concorrência Estruturada com async/await para auditoria interna rápida
    suspend fun executarAuditoriaComplexaEmParalelo(transactionId: String, amount: BigDecimal, tipo: String): Map<String, String> = coroutineScope {
        println("[KOTLIN-CORE] ⚡ Iniciando checagem paralela de score e compliance para: $transactionId")

        // Dispara duas análises em paralelo sem travar a thread principal
        val scoreDeferred: Deferred<String> = async { calcularScoreRiscoInterno(transactionId, amount) }
        val complianceDeferred: Deferred<String> = async { checarRegrasCompliance(transactionId, tipo) }

        // Aguarda o resultado de ambas (Structured Concurrency)
        val riskScore = scoreDeferred.await()
        val complianceStatus = complianceDeferred.await()

        mapOf(
            "transactionId" to transactionId,
            "amount" to amount.toString(),
            "riskScore" to riskScore,
            "complianceStatus" to complianceStatus
        )
    }

    // Task 4.4: Chamada direcionada ao motor que envelopa o Gemini/Circuit Breaker
    suspend fun analisarDivergenciaFisica(transactionId: String, amount: BigDecimal, tipo: String): String {
        println("[KOTLIN-CORE] 🧠 Encaminhando dados de desbalanceamento para o motor de IA...")
        
        // Simula pequena latência de tratamento reativo
        delay(50) 
        
        // Invoca o método correto da porta da Arquitetura Hexagonal
        return aiEngine.auditDiscrepancy(transactionId, amount, tipo)
    }

    private suspend fun calcularScoreRiscoInterno(transactionId: String, amount: BigDecimal): String {
        delay(100)
        return if (amount > BigDecimal("1000.00")) "RISCO_MEDIO" else "RISCO_BAIXO"
    }

    private suspend fun checarRegrasCompliance(transactionId: String, tipo: String): String {
        delay(150)
        return "COMPLIANT"
    }
}
