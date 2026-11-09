package com.ernakh.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/temperatura")
public class TemperaturaController {

    // =========================================================================
    // EXERCÍCIO 1 - Item 3 (Slide 40 do PDF)
    // GET /temperatura/{valor}/{de}/{para} ou /temperatura/convert?valor=30&de=C&para=F
    // Converte temperatura.
    // Validar de/para (só C/F/K). Se inválido: 400.
    // =========================================================================

    // Versão via rota (PathVariable)
    @GetMapping("/{valor}/{de}/{para}")
    public ResponseEntity<?> converterPath(
            @PathVariable Double valor,
            @PathVariable String de,
            @PathVariable String para) {
        return processarConversao(valor, de, para);
    }

    // Versão via query parameter: /temperatura/convert?valor=30&de=C&para=F
    @GetMapping("/convert")
    public ResponseEntity<?> converterQuery(
            @RequestParam(required = false) Double valor,
            @RequestParam(required = false) String de,
            @RequestParam(required = false) String para) {

        if (valor == null || de == null || para == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", "Parâmetros 'valor', 'de' e 'para' são obrigatórios."));
        }
        return processarConversao(valor, de, para);
    }

    private ResponseEntity<?> processarConversao(Double valor, String de, String para) {
        String deNorm = de.toUpperCase();
        String paraNorm = para.toUpperCase();

        if (!List.of("C", "F", "K").contains(deNorm) || !List.of("C", "F", "K").contains(paraNorm)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", "Unidade inválida informada. Permitidos apenas 'C', 'F' ou 'K'."));
        }

        // 1. Converte de origem para Celsius
        double emCelsius = switch (deNorm) {
            case "F" -> (valor - 32.0) * 5.0 / 9.0;
            case "K" -> valor - 273.15;
            default -> valor;
        };

        // 2. Converte de Celsius para destino
        double resultado = switch (paraNorm) {
            case "F" -> (emCelsius * 9.0 / 5.0) + 32.0;
            case "K" -> emCelsius + 273.15;
            default -> emCelsius;
        };

        return ResponseEntity.ok(Map.of(
                "valorOriginal", valor,
                "de", deNorm,
                "para", paraNorm,
                "resultado", Math.round(resultado * 100.0) / 100.0
        ));
    }
}
