package com.tqi.fintech.infrastructure;

import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/live-dashboard")
@CrossOrigin(origins = "*")
public class DashboardLiveController {

    private static final ConcurrentLinkedQueue<Map<String, Object>> eventoBuffer = new ConcurrentLinkedQueue<>();

    public static void notificar(int step, String mensagem, String tipo) {
        eventoBuffer.add(Map.of(
            "step", step,
            "text", mensagem,
            "type", tipo,
            "timestamp", System.currentTimeMillis()
        ));
    }

    @GetMapping("/events")
    public List<Map<String, Object>> consumirEventos() {
        List<Map<String, Object>> eventosAtuais = new ArrayList<>();
        Map<String, Object> evento;
        while ((evento = eventoBuffer.poll()) != null) {
            eventosAtuais.add(evento);
        }
        return eventosAtuais;
    }
}
