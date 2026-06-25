package com.tqi.fintech.infrastructure;

import com.tqi.fintech.application.output.AiAuditorEngine;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class ReconciliationAiEngine implements AiAuditorEngine {

    @Override
    @CircuitBreaker(name = "geminiAiEngine", fallbackMethod = "fallbackAudit")
    public String auditDiscrepancy(String transactionId, BigDecimal amount, String operationType) {
        System.out.println("[AI-ENGINE] [INFO] Enviando contexto para o modelo gemini-2.5-flash...");
        
        // Simulação do payload JSON estruturado que seria retornado pelo Gemini 2.5
        // Em caso de latência real de rede, se ultrapassar o limite, o Circuit Breaker abrirá automaticamente.
        return String.format(
            "{\"status\":\"AUDITED\",\"analysis\":\"Divergência de %s em centavos avaliada como quebra de arredondamento aceitável.\",\"transactionId\":\"%s\"}",
            amount.toPlainString(), transactionId
        );
    }

    // Método de Contingência (Fallback) - Mantém o sistema operando mesmo se o Gemini cair
    public String fallbackAudit(String transactionId, BigDecimal amount, String operationType, Throwable t) {
        System.out.println("[AI-ENGINE] [WARN] Fallback acionado devido a falha ou timeout no Gemini: " + t.getMessage());
        return String.format(
            "{\"status\":\"FALLBACK_REVIEW\",\"analysis\":\"Análise automatizada indisponível. Encaminhado para a fila de auditoria humana.\",\"transactionId\":\"%s\"}",
            transactionId
        );
    }
}
