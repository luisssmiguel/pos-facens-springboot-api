package com.ernakh.demo.controllers;

import com.ernakh.demo.models.Pessoa;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    // Lista em memória criada em aula (Slide 44 do PDF)
    private static final List<Pessoa> pessoas = new ArrayList<>();

    // Slide 44 e 48: Listar todas as pessoas (GET /pessoa/pessoas)
    @GetMapping("/pessoas")
    public List<Pessoa> listar() {
        return pessoas;
    }

    // Slide 44 e 46: Cadastrar nova pessoa (POST /pessoa/pessoa)
    @PostMapping("/pessoa")
    @ResponseStatus(HttpStatus.CREATED)
    public Pessoa criar(@RequestBody Pessoa p) {
        pessoas.add(p);
        return p;
    }

    // Slide 44 e 47: Buscar pessoa por nome (GET /pessoa/pessoa/{nome})
    @GetMapping("/pessoa/{nome}")
    public Optional<Pessoa> buscar(@PathVariable String nome) {
        return pessoas.stream()
                .filter(i -> i.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }

    // Buscar pessoa por ID (GET /pessoa/{id})
    @GetMapping("/{id}")
    public Pessoa buscarPorId(@PathVariable Long id) {
        return pessoas.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada com id: " + id));
    }

    // =========================================================================
    // EXERCÍCIO 2 (Slide 49 do PDF)
    // Para a nossa lista de Pessoas, implemente:
    // =========================================================================

    // PUT /pessoa/{id} -> Substitui tudo (id e nome).
    @PutMapping("/{id}")
    public Pessoa substituir(@PathVariable Long id, @RequestBody Pessoa novaPessoa) {
        Pessoa p = pessoas.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada com id: " + id));

        p.setId(novaPessoa.getId());
        p.setNome(novaPessoa.getNome());
        return p;
    }

    // PATCH /pessoa/{id} -> Atualiza parcial (ex.: só nome).
    @PatchMapping("/{id}")
    public Pessoa atualizarParcial(@PathVariable Long id, @RequestBody Pessoa dados) {
        Pessoa p = pessoas.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada com id: " + id));

        if (dados.getNome() != null && !dados.getNome().isBlank()) {
            p.setNome(dados.getNome());
        }
        return p;
    }

    // DELETE /pessoa/{id} -> Remove e retorna 204.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        boolean removido = pessoas.removeIf(item -> item.getId().equals(id));
        if (!removido) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada com id: " + id);
        }
    }
}
