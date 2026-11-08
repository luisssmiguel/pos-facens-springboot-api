package com.ernakh.demo.repositories;

import com.ernakh.demo.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a JOIN FETCH a.livros WHERE a.nome = :nome")
    Autor buscarComLivros(@Param("nome") String nome);

    Optional<Autor> findByNomeIgnoreCase(String nome);
}
