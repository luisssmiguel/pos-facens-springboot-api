package com.ernakh.demo.controllers;

import com.ernakh.demo.models.Livro;
import com.ernakh.demo.repositories.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;

    // Slide 36
    @GetMapping("/livros-eager")
    public List<String> livrosEager() {
        var livros = livroRepository.findAll();
        return livros.stream()
                .map(l -> l.getTitulo() + " - autor: " + (l.getAutor() != null ? l.getAutor().getNome() : "Sem autor"))
                .toList();
    }

    @GetMapping("/livros")
    public List<Livro> listarLivros() {
        return livroRepository.findAll();
    }

    @PostMapping("/livros")
    public Livro criarLivro(@RequestBody Livro livro) {
        return livroRepository.save(livro);
    }
}
