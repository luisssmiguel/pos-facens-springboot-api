package com.luisssmiguel.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luisssmiguel.demo.models.Pessoa;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================================================================
    // TESTES DA AULA - HelloController
    // =========================================================================

    @Test
    void testHelloEndpointsDeAula() throws Exception {
        mockMvc.perform(get("/teste"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World!"));

        mockMvc.perform(get("/hello/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("HELLO World!"));

        mockMvc.perform(get("/hello/world"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello WORLD!"));

        mockMvc.perform(post("/hello/world/Oi"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Oi"));

        mockMvc.perform(post("/hello/hello/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("3"))
                .andExpect(status().isCreated())
                .andExpect(content().string("10"));
    }

    // =========================================================================
    // EXERCÍCIO 1 - Item 1: GET /hello/{nome} -> "Olá, {nome}!"
    // =========================================================================

    @Test
    void testExercicio1_HelloNome() throws Exception {
        mockMvc.perform(get("/hello/Luis"))
                .andExpect(status().isOk())
                .andExpect(content().string("Olá, Luis!"));

        mockMvc.perform(get("/hello/Fabricio"))
                .andExpect(status().isOk())
                .andExpect(content().string("Olá, Fabricio!"));
    }

    // =========================================================================
    // EXERCÍCIO 1 - Item 2: GET /calc/soma/{a}/{b} ou /calc/soma?a=10&b=5
    // =========================================================================

    @Test
    void testExercicio1_CalcSomaSucesso() throws Exception {
        // Via PathVariable
        mockMvc.perform(get("/calc/soma/10/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado", is(15)));

        // Via QueryParams
        mockMvc.perform(get("/calc/soma?a=10&b=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado", is(15)));
    }

    @Test
    void testExercicio1_CalcSomaErro400SeFaltarParametros() throws Exception {
        // Faltando ambos
        mockMvc.perform(get("/calc/soma"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro", notNullValue()));

        // Faltando b na query
        mockMvc.perform(get("/calc/soma?a=10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro", notNullValue()));

        // Faltando a na query
        mockMvc.perform(get("/calc/soma?b=5"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro", notNullValue()));

        // Faltando b no path
        mockMvc.perform(get("/calc/soma/10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro", notNullValue()));
    }

    // =========================================================================
    // EXERCÍCIO 1 - Item 3: GET /temperatura/convert?valor=30&de=C&para=F
    // e /temperatura/{valor}/{de}/{para}
    // =========================================================================

    @Test
    void testExercicio1_TemperaturaConversaoSucesso() throws Exception {
        // 30 C -> F = 86.0
        mockMvc.perform(get("/temperatura/30/C/F"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado", is(86.0)))
                .andExpect(jsonPath("$.de", is("C")))
                .andExpect(jsonPath("$.para", is("F")));

        // Via QueryParams
        mockMvc.perform(get("/temperatura/convert?valor=30&de=C&para=F"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado", is(86.0)));

        // 100 C -> K = 373.15
        mockMvc.perform(get("/temperatura/100/C/K"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado", is(373.15)));

        // 32 F -> C = 0.0
        mockMvc.perform(get("/temperatura/32/F/C"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado", is(0.0)));
    }

    @Test
    void testExercicio1_TemperaturaUnidadeInvalida400() throws Exception {
        // Unidade X inválida
        mockMvc.perform(get("/temperatura/30/X/F"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro", containsString("Unidade inválida")));

        mockMvc.perform(get("/temperatura/convert?valor=30&de=C&para=Z"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro", containsString("Unidade inválida")));

        // Faltando parâmetros
        mockMvc.perform(get("/temperatura/convert"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro", notNullValue()));
    }

    // =========================================================================
    // EXERCÍCIO 2 - CRUD na Lista de Pessoas
    // PUT /pessoa/{id} (substitui tudo)
    // PATCH /pessoa/{id} (atualiza parcial)
    // DELETE /pessoa/{id} (remove e retorna 204)
    // =========================================================================

    @Test
    @Order(1)
    void testAula_PessoaCriarEListar() throws Exception {
        Pessoa p1 = new Pessoa(1L, "Luis Miguel");
        mockMvc.perform(post("/pessoa/pessoa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Luis Miguel")));

        mockMvc.perform(get("/pessoa/pessoas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));

        mockMvc.perform(get("/pessoa/pessoa/Luis Miguel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Luis Miguel")));
    }

    @Test
    @Order(2)
    void testExercicio2_PutPessoaSubstituiTudo() throws Exception {
        Pessoa pessoaAtualizada = new Pessoa(1L, "Luis Miguel Atualizado");

        mockMvc.perform(put("/pessoa/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoaAtualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Luis Miguel Atualizado")));
    }

    @Test
    @Order(3)
    void testExercicio2_PatchPessoaAtualizaParcial() throws Exception {
        String jsonParcial = "{\"nome\": \"Luis Miguel Silva\"}";

        mockMvc.perform(patch("/pessoa/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParcial))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Luis Miguel Silva")));
    }

    @Test
    @Order(4)
    void testExercicio2_DeletePessoaRetorna204() throws Exception {
        mockMvc.perform(delete("/pessoa/1"))
                .andExpect(status().isNoContent());

        // Confirma que a pessoa não existe mais
        mockMvc.perform(get("/pessoa/1"))
                .andExpect(status().isNotFound());
    }

    // =========================================================================
    // TESTES DA AULA - Autor e Livro (Lazy / Eager)
    // =========================================================================

    @Test
    void testAula_LazyEEager() throws Exception {
        mockMvc.perform(get("/autores-lazy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]", containsString("Machado de Assis")));

        mockMvc.perform(get("/livros-eager"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
