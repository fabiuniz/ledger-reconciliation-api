package com.tqi.fintech.application.input;

import com.tqi.fintech.application.output.ReconciliationRepository;
import com.tqi.fintech.domain.LedgerDashboardReport;
import com.tqi.fintech.domain.ReconciliationBatch;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class ReconciliationService {

    private final ReconciliationRepository repository;

    public ReconciliationService(ReconciliationRepository repository) {
        this.repository = repository;
    }

    public LedgerDashboardReport gerarDashboard() {
        // Uso de Virtual Threads através do try-with-resources (AutoCloseable no Java 21)
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            // Dispara a busca I/O de dados de forma assíncrona/não-bloqueante
            Future<List<ReconciliationBatch>> futureBatches = executor.submit(repository::findAll);
            List<ReconciliationBatch> batches = futureBatches.get();

            // Executa o processamento matemático pesado de agregação
            BigDecimal volumeTotal = batches.stream()
                .map(ReconciliationBatch::totalVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            long divergencias = batches.stream()
                .filter(b -> "DISCREPANCY".equals(b.status()))
                .count();

            long totalLotes = batches.size();
            double taxaAcuracia = totalLotes == 0 ? 100.0 : ((double) (totalLotes - divergencias) / totalLotes) * 100.0;

            return new LedgerDashboardReport(volumeTotal, divergencias, taxaAcuracia);
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar agregação paralela via Virtual Threads", e);
        }
    }
}
