package com.ernakh.demo.controllers;

import com.ernakh.demo.models.Pessoa;
import com.ernakh.demo.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    // Lista estática em memória conforme construída em aula (Slide 44 do PDF)
    private static final List<Pessoa> pessoas = new ArrayList<>();

    @Autowired(required = false)
    private PessoaRepository pessoaRepository;

    // Slide 44 e 48: Listar todas as pessoas
    @GetMapping({"", "/pessoas"})
    public List<Pessoa> listar() {
        return pessoas;
    }

    // Slide 44 e 46: Criar nova pessoa
    @PostMapping({"", "/pessoa"})
    @ResponseStatus(HttpStatus.CREATED)
    public Pessoa criar(@RequestBody Pessoa p) {
        if (p.getId() == null) {
            long proximoId = pessoas.stream()
                    .mapToLong(Pessoa::getId)
                    .max()
                    .orElse(0L) + 1;
            p.setId(proximoId);
        }
        pessoas.add(p);
        if (pessoaRepository != null) {
            try {
                pessoaRepository.save(p);
            } catch (Exception ignored) {}
        }
        return p;
    }

    // Slide 44 e 47: Buscar por nome
    @GetMapping({"/nome/{nome}", "/pessoa/{nome}"})
    public Optional<Pessoa> buscarPorNome(@PathVariable String nome) {
        return pessoas.stream()
                .filter(i -> i.getNome() != null && i.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }

    // Buscar por ID numérico
    @GetMapping("/{id:[0-9]+}")
    public Pessoa buscarPorId(@PathVariable Long id) {
        return pessoas.stream()
                .filter(p -> p.getId() != null && p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pessoa não encontrada com id: " + id
                ));
    }

    // =========================================================================
    // EXERCÍCIO 2 - Item 1: PUT /pessoa/{id}
    // Substitui tudo (id e nome).
    // =========================================================================
    @PutMapping({"/{id:[0-9]+}", "/pessoa/{id:[0-9]+}"})
    public Pessoa updatePessoaCompleto(@PathVariable Long id, @RequestBody Pessoa novaPessoa) {
        Pessoa p = pessoas.stream()
                .filter(item -> item.getId() != null && item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pessoa não encontrada com id: " + id
                ));

        // Substitui todos os atributos
        if (novaPessoa.getId() != null) {
            p.setId(novaPessoa.getId());
        }
        p.setNome(novaPessoa.getNome());

        if (pessoaRepository != null) {
            try {
                pessoaRepository.save(p);
            } catch (Exception ignored) {}
        }

        return p;
    }

    // =========================================================================
    // EXERCÍCIO 2 - Item 2: PATCH /pessoa/{id}
    // Atualiza parcial (ex.: só nome).
    // =========================================================================
    @PatchMapping({"/{id:[0-9]+}", "/pessoa/{id:[0-9]+}"})
    public Pessoa updatePessoaParcial(@PathVariable Long id, @RequestBody Pessoa dadosParciais) {
        Pessoa p = pessoas.stream()
                .filter(item -> item.getId() != null && item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pessoa não encontrada com id: " + id
                ));

        // Atualização parcial: só modifica o que foi informado
        if (dadosParciais.getNome() != null && !dadosParciais.getNome().isBlank()) {
            p.setNome(dadosParciais.getNome());
        }

        if (pessoaRepository != null) {
            try {
                pessoaRepository.save(p);
            } catch (Exception ignored) {}
        }

        return p;
    }

    // =========================================================================
    // EXERCÍCIO 2 - Item 3: DELETE /pessoa/{id}
    // Remove e retorna 204 No Content.
    // =========================================================================
    @DeleteMapping({"/{id:[0-9]+}", "/pessoa/{id:[0-9]+}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletePessoa(@PathVariable Long id) {
        boolean removed = pessoas.removeIf(p -> p.getId() != null && p.getId().equals(id));

        if (!removed) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Pessoa não encontrada com id: " + id
            );
        }

        if (pessoaRepository != null) {
            try {
                pessoaRepository.deleteById(id);
            } catch (Exception ignored) {}
        }

        return ResponseEntity.noContent().build();
    }
}
