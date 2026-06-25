package com.tqi.fintech.infrastructure;

import com.tqi.fintech.application.output.AiAuditorEngine;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class ReconciliationAiEngine implements AiAuditorEngine {

    private final RestClient restClient;
    
    @Value("${gemini.api.url}")
    private String apiUrl;
    
    @Value("${gemini.api.key}")
    private String apiKey;

    public ReconciliationAiEngine() {
        this.restClient = RestClient.builder().build();
    }

    @Override
    @CircuitBreaker(name = "geminiAiEngine", fallbackMethod = "fallbackAnalyse")
    public String auditDiscrepancy(String transactionId, BigDecimal amount, String type) {
        System.out.println("[AI-ENGINE] 🌐 [HTTP] Disparando requisicao real para o Gemini 2.5-Flash...");

        if ("mock-key-development".equals(apiKey) || apiKey == null) {
            System.out.println("[AI-ENGINE] ⚠️ GEMINI_API_KEY nao configurada. Usando simulador local.");
            return retornarMockEstruturado(transactionId, amount, type);
        }

        String promptContexto = String.format(
            "[CONTEXTO FINTECH - AUDITORIA DE LEDGER]\n" +
            "Inconsistencia contabil detectada:\n" +
            "- ID: %s\n" +
            "- Valor: R$ %s\n" +
            "- Tipo: %s\n\n" +
            "Retorne um JSON com: \"categoria_risco\", \"parecer_auditoria\" e \"acao_recomendada\".",
            transactionId, amount.toString(), type
        );

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", promptContexto)
                ))
            )
        );

        return restClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);
    }

    // Assinatura do Fallback deve ser idêntica à do método principal + a exceção capturada
    public String fallbackAnalyse(String transactionId, BigDecimal amount, String type, Throwable t) {
        System.err.println("[AI-ENGINE] 🚨 Circuit Breaker ATUOU! Motivo: " + t.getMessage());
        return "{\"categoria_risco\": \"DESCONHECIDO\", \"parecer_auditoria\": \"IA indisponivel temporariamente.\", \"acao_recomendada\": \"REVISAO_MANUAL_HUMANA\"}";
    }

    private String retornarMockEstruturado(String transactionId, BigDecimal amount, String type) {
        return String.format(
            "{\"categoria_risco\": \"BAIXO\", \"parecer_auditoria\": \"Analise simulada para %s com valor R$ %s.\", \"acao_recomendada\": \"AJUSTE_AUTOMATICO\"}",
            transactionId, amount.toString()
        );
    }
}
