package com.ernakh.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/calc")
public class CalculadoraController {

    // ==============================================================
    // EXERCÍCIO 1 - Item 2 (Slide 40 do PDF)
    // GET /calc/soma/{a}/{b} ou /calc/soma?a=10&b=5
    // Retorna: {"resultado": 15}
    // Se faltar a ou b, retornar erro 400 com mensagem clara.
    // ==============================================================

    // Versão PathVariable: /calc/soma/{a}/{b}
    @GetMapping("/soma/{a}/{b}")
    public ResponseEntity<?> somarPath(@PathVariable Integer a, @PathVariable Integer b) {
        return ResponseEntity.ok(Map.of("resultado", a + b));
    }

    // Se faltar parâmetro no path
    @GetMapping("/soma/{a}")
    public ResponseEntity<?> somarPathFaltandoB(@PathVariable Integer a) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("erro", "O parâmetro 'b' está faltando na rota."));
    }

    // Versão QueryParams: /calc/soma?a=10&b=5
    @GetMapping("/soma")
    public ResponseEntity<?> somarQuery(
            @RequestParam(required = false) Integer a,
            @RequestParam(required = false) Integer b) {

        if (a == null || b == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", "Os parâmetros 'a' e 'b' são obrigatórios."));
        }
        return ResponseEntity.ok(Map.of("resultado", a + b));
    }
}
