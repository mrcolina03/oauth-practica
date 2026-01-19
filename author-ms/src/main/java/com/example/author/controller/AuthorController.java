package com.example.author.controller;

import com.example.author.model.Author;
import com.example.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    @Autowired
    private AuthorRepository repository;

    @GetMapping
    public List<Author> getAll() {
        if(repository.count() == 0) { // Datos de prueba
            repository.save(new Author("Gabriel García Márquez"));
            repository.save(new Author("Isabel Allende"));
        }
        return repository.findAll();
    }

    @PostMapping
    public Author create(@RequestBody Author author) {
        return repository.save(author);
    }
}