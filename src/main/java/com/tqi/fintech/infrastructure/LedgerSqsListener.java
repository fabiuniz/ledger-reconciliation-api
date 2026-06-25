package com.tqi.fintech.infrastructure;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class LedgerSqsListener {

    @SqsListener("ledger-clearing-upload-queue")
    public void listen(String messagePayload) {
        // Log sênior estruturado simulando tracing de transação bancária
        System.out.println("[SQS CONSUMER] [INFO] Novo lote de liquidação capturado para processamento: " + messagePayload);
        
        // Aqui nas próximas tasks conectaremos o motor de IA e a publicação no Kafka
    }
}
