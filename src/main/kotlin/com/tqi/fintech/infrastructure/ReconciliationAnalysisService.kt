package com.tqi.fintech.infrastructure

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ReconciliationAnalysisService {

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

    // Task 4.4: Construção do payload contextual para envio ao Gemini 2.5 Flash
    suspend fun analisarDivergenciaFisica(transactionId: String, amount: BigDecimal, tipo: String): String {
        println("[KOTLIN-CORE] 🧠 Formatando prompt de auditoria avançada para o Gemini...")

        val promptFintech = """
            [CONTEXTO FINTECH - AUDITORIA DE LEDGER]
            Você é o agente especialista em reconciliação bancária da TQI Fintech.
            Análise a seguinte inconsistência contábil capturada no pipeline:
            - ID da Transação: $transactionId
            - Valor do Desbalanceamento: R$ $amount
            - Tipo de Lançamento Base: $tipo

            Instruções Estritas:
            1. Categorize o risco em: BAIXO (se < R$ 1.00), MEDIO (se < R$ 100.00), ALTO (se >= R$ 100.00).
            2. Se o tipo for CREDIT e o valor for uma dízima ou valor muito baixo, verifique se há padrão de 'Salami Attack' (fraude de centavos).
            3. Retorne a resposta exclusivamente em formato JSON com as chaves: "categoria_risco", "parecer_auditoria" e "acao_recomendada".
        """.trimIndent()

        // Aqui simulamos o tempo de resposta HTTP da API do Gemini (protegido pelo Circuit Breaker do Java)
        delay(300) 
        
        return """
            {
              "categoria_risco": "BAIXO",
              "parecer_auditoria": "Divergência residual de R$ $amount identificada em transação de $tipo. Padrão compatível com arredondamento matemático de truncamento de juros flutuantes.",
              "acao_recomendada": "AJUSTE_AUTOMATICO_CONTA_PONTE"
            }
        """.trimIndent()
    }

    private suspend fun calcularScoreRiscoInterno(transactionId: String, amount: BigDecimal): String {
        delay(100) // Simula análise matemática pesada
        return if (amount > BigDecimal("1000.00")) "RISCO_MEDIO" else "RISCO_BAIXO"
    }

    private suspend fun checarRegrasCompliance(transactionId: String, tipo: String): String {
        delay(150) // Simula checagem com barramento da CIP/Bacen
        return "COMPLIANT"
    }
}
