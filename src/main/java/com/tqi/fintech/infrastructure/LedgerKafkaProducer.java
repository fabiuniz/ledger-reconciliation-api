package com.tqi.fintech.infrastructure;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class LedgerKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC_NAME = "ledger-reconciliation-events";

    public LedgerKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publicarResultado(String key, String payload) {
        System.out.println("[KAFKA-PRODUCER] [INFO] Publicando evento de auditoria contábil no tópico: " + TOPIC_NAME);
        
        // Envia o evento de forma assíncrona para o cluster Confluent local
        this.kafkaTemplate.send(TOPIC_NAME, key, payload);
    }
}
