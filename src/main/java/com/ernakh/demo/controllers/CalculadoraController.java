package com.ernakh.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/calc")
public class CalculadoraController {

    // ==============================================================
    // EXERCÍCIO 1 - Item 2: GET /calc/soma/{a}/{b} ou /calc/soma?a=10&b=5
    // Retorna: {"resultado": 15}
    // Se faltar a ou b: erro 400 com mensagem clara
    // ==============================================================

    // Versão via PathVariable: /calc/soma/{a}/{b}
    @GetMapping("/soma/{a}/{b}")
    public ResponseEntity<?> somarPath(@PathVariable Integer a, @PathVariable Integer b) {
        if (a == null || b == null) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Parâmetros 'a' e 'b' são obrigatórios.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }
        Map<String, Integer> resposta = Collections.singletonMap("resultado", a + b);
        return ResponseEntity.ok(resposta);
    }

    // Caso o cliente chame apenas /calc/soma/{a} faltando o b
    @GetMapping("/soma/{a}")
    public ResponseEntity<?> somarPathFaltandoB(@PathVariable Integer a) {
        Map<String, String> erro = new HashMap<>();
        erro.put("erro", "O parâmetro 'b' está faltando na rota. Utilize /calc/soma/{a}/{b} ou /calc/soma?a=10&b=5.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // Versão via QueryParams: /calc/soma?a=10&b=5
    @GetMapping("/soma")
    public ResponseEntity<?> somarQuery(
            @RequestParam(required = false) Integer a,
            @RequestParam(required = false) Integer b) {

        if (a == null && b == null) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Os parâmetros 'a' e 'b' são obrigatórios. Exemplo: /calc/soma?a=10&b=5");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }
        if (a == null) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "O parâmetro 'a' é obrigatório.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }
        if (b == null) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "O parâmetro 'b' é obrigatório.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }

        Map<String, Integer> resposta = Collections.singletonMap("resultado", a + b);
        return ResponseEntity.ok(resposta);
    }
}
