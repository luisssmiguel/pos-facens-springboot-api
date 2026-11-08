package com.ernakh.demo.controllers;

import com.ernakh.demo.models.Autor;
import com.ernakh.demo.repositories.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AutorController {

    @Autowired
    private AutorRepository autorRepository;

    // Slide 35
    @GetMapping("/autores-lazy")
    @Transactional(readOnly = true)
    public List<String> autoresLazy() {
        var autores = autorRepository.findAll();
        return autores.stream()
                .map(a -> a.getNome() + " (livros=" + a.getLivros().size() + ")")
                .toList();
    }

    @GetMapping("/autores")
    public List<Autor> listarAutores() {
        return autorRepository.findAll();
    }

    @PostMapping("/autores")
    public Autor criarAutor(@RequestBody Autor autor) {
        return autorRepository.save(autor);
    }
}
