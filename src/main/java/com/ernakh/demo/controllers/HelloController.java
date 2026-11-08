package com.ernakh.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    // Slide 26
    @GetMapping("/teste")
    public String meuEndPoint() {
        return "Hello World!";
    }

    // Slide 29
    @GetMapping("/hello/teste")
    public String helloTeste() {
        return "Hello World!";
    }

    // Slide 30
    @GetMapping("/hello/hello")
    public String hello() {
        return "HELLO World!";
    }

    // Slide 30
    @GetMapping("/hello/world")
    public String world() {
        return "Hello WORLD!";
    }

    // Slide 35
    @PostMapping("/hello/world/{valor}")
    @ResponseStatus(HttpStatus.CREATED)
    public String retornaValor(@PathVariable String valor) {
        return valor;
    }

    // Slide 38
    @PostMapping("/hello/hello/{valor1}")
    @ResponseStatus(HttpStatus.CREATED)
    public int retornaSoma(@PathVariable int valor1, @RequestBody int valor2) {
        return valor1 + valor2;
    }

    // ==========================================
    // EXERCÍCIO 1 - Item 1: GET /hello/{nome}
    // ==========================================
    @GetMapping("/hello/{nome}")
    public String saudacao(@PathVariable String nome) {
        return "Olá, " + nome + "!";
    }
}
