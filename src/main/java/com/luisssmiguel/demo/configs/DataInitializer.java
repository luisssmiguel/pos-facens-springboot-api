package com.luisssmiguel.demo.configs;

import com.luisssmiguel.demo.models.Autor;
import com.luisssmiguel.demo.models.Livro;
import com.luisssmiguel.demo.repositories.AutorRepository;
import com.luisssmiguel.demo.repositories.LivroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(AutorRepository autorRepo, LivroRepository livroRepo) {
        return args -> {
            Autor autor = new Autor();
            autor.setNome("Machado de Assis");
            autor = autorRepo.save(autor);

            Livro l1 = new Livro("Dom Casmurro", autor);
            Livro l2 = new Livro("Memórias Póstumas", autor);
            livroRepo.saveAll(List.of(l1, l2));
        };
    }
}
