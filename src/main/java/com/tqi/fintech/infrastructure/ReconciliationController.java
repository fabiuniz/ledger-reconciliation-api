package com.tqi.fintech.infrastructure;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/reconciliations")
public class ReconciliationController {

    private final SqsTemplate sqsTemplate;
    private static final String QUEUE_NAME = "ledger-clearing-upload-queue";

    public ReconciliationController(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadClearingFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Arquivo vazio"));
        }

        try {
            // Gera um ID de correlação para rastrear o processamento assíncrono do lote bancário
            String batchId = UUID.randomUUID().toString();
            
            // Simula a leitura rápida do cabeçalho e envia os metadados brutos do lote para o AWS SQS local
            String messagePayload = String.format("{\"batchId\":\"%s\",\"fileName\":\"%s\"}", batchId, file.getOriginalFilename());
            
            sqsTemplate.send(QUEUE_NAME, messagePayload);

            // Resposta imediata HTTP 202 - Padrão de Sistemas Assíncronos de Alta Performance
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                "message", "Lote encaminhado com sucesso para a fila de liquidação contábil",
                "batchId", batchId
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Falha ao enfileirar lote: " + e.getMessage()));
        }
    }
}
