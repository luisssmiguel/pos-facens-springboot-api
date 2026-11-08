package com.ernakh.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/temperatura")
public class TemperaturaController {

    private static final Set<String> UNIDADES_VALIDAS = Set.of("C", "F", "K");

    // =========================================================================
    // EXERCÍCIO 1 - Item 3:
    // GET /temperatura/{valor}/{de}/{para} ou /temperatura/convert?valor=30&de=C&para=F
    // Converte temperatura.
    // Validar de/para (só C/F/K). Se inválido: 400.
    // =========================================================================

    // Versão via PathVariable: /temperatura/{valor}/{de}/{para}
    @GetMapping("/{valor}/{de}/{para}")
    public ResponseEntity<?> converterPath(
            @PathVariable Double valor,
            @PathVariable String de,
            @PathVariable String para) {

        return processarConversao(valor, de, para);
    }

    // Versão via QueryParams: /temperatura/convert?valor=30&de=C&para=F
    @GetMapping("/convert")
    public ResponseEntity<?> converterQuery(
            @RequestParam(required = false) Double valor,
            @RequestParam(required = false) String de,
            @RequestParam(required = false) String para) {

        if (valor == null || de == null || para == null) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Parâmetros 'valor', 'de' e 'para' são obrigatórios. Exemplo: /temperatura/convert?valor=30&de=C&para=F");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }

        return processarConversao(valor, de, para);
    }

    private ResponseEntity<?> processarConversao(Double valor, String de, String para) {
        String deNorm = de != null ? de.trim().toUpperCase() : "";
        String paraNorm = para != null ? para.trim().toUpperCase() : "";

        if (!UNIDADES_VALIDAS.contains(deNorm) || !UNIDADES_VALIDAS.contains(paraNorm)) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Unidade inválida informada ('" + de + "' ou '" + para + "'). Apenas 'C', 'F' e 'K' são permitidos.");
            erro.put("status", 400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }

        // 1. Converte a unidade de origem para Celsius
        double tempCelsius;
        switch (deNorm) {
            case "C":
                tempCelsius = valor;
                break;
            case "F":
                tempCelsius = (valor - 32.0) * 5.0 / 9.0;
                break;
            case "K":
                tempCelsius = valor - 273.15;
                break;
            default:
                tempCelsius = valor;
        }

        // 2. Converte de Celsius para a unidade de destino
        double resultado;
        switch (paraNorm) {
            case "C":
                resultado = tempCelsius;
                break;
            case "F":
                resultado = (tempCelsius * 9.0 / 5.0) + 32.0;
                break;
            case "K":
                resultado = tempCelsius + 273.15;
                break;
            default:
                resultado = tempCelsius;
        }

        double resultadoArredondado = Math.round(resultado * 100.0) / 100.0;

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("valorOriginal", valor);
        resposta.put("de", deNorm);
        resposta.put("para", paraNorm);
        resposta.put("resultado", resultadoArredondado);

        return ResponseEntity.ok(resposta);
    }
}
