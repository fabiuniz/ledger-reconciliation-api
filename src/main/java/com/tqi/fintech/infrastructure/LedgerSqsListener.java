package com.tqi.fintech.infrastructure;

import com.tqi.fintech.application.output.ReconciliationRepository;
import com.tqi.fintech.domain.ReconciliationBatch;
import com.tqi.fintech.infrastructure.ReconciliationAnalysisService; // Import explícito da classe Kotlin
import io.awspring.cloud.sqs.annotation.SqsListener;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.Dispatchers;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class LedgerSqsListener {

    private final ReconciliationAnalysisService kotlinAnalysisService;
    private final ReconciliationRepository repository;
    private final LedgerKafkaProducer kafkaProducer;

    public LedgerSqsListener(ReconciliationAnalysisService kotlinAnalysisService, 
                             ReconciliationRepository repository, 
                             LedgerKafkaProducer kafkaProducer) {
        this.kotlinAnalysisService = kotlinAnalysisService;
        this.repository = repository;
        this.kafkaProducer = kafkaProducer;
    }

    @SqsListener("ledger-clearing-upload-queue")
    public void listen(String messagePayload) {
        System.out.println("\n[SQS CONSUMER] 📥 Novo lote capturado. Iniciando pipeline E2E...");

        try {
            String transactionId = "TRX-" + System.currentTimeMillis();
            BigDecimal amount = new BigDecimal("0.05");

            System.out.println("[SQS CONSUMER] 🚀 Invocando Concorrência Estruturada do Kotlin...");
            
            // Ponte Java -> Kotlin Coroutines
            Map<String, String> analiseComplexa = BuildersKt.runBlocking(
                Dispatchers.getIO(),
                (scope, continuation) -> kotlinAnalysisService.executarAuditoriaComplexaEmParalelo(transactionId, amount, continuation)
            );

            System.out.println("[SQS CONSUMER] 🧠 Resultado da análise preditiva: " + analiseComplexa);

            System.out.println("[SQS CONSUMER] 🤖 Invocando auditoria com IA...");
            String resultadoIa = BuildersKt.runBlocking(
                Dispatchers.getIO(),
                (scope, continuation) -> kotlinAnalysisService.analisarDivergenciaFisica(transactionId, amount, "CREDIT", continuation)
            );

            ReconciliationBatch batchFinal = new ReconciliationBatch(
                "BATCH-RECON-" + System.currentTimeMillis(),
                new BigDecimal("1950.50"),
                2L,
                "AUDITED",
                LocalDateTime.now()
            );
            repository.save(batchFinal);
            System.out.println("[SQS CONSUMER] 💾 Estado contábil persistido no Postgres.");

            kafkaProducer.publicarResultado(batchFinal.batchId(), resultadoIa);
            System.out.println("[SQS CONSUMER] 📨 Evento publicado no Apache Kafka! Pipeline finalizado com sucesso.\n");

        } catch (Exception e) {
            System.err.println("[SQS CONSUMER] ❌ Falha crítica no pipeline: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
