package com.example.author.controller;

import com.example.author.model.Author;
import com.example.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    @Autowired
    private AuthorRepository repository;

    // Ruta solicitada para ver el token
    @GetMapping("/authorized")
    public Map<String, String> getAuthorizedToken(
            @RegisteredOAuth2AuthorizedClient("oidc-client") OAuth2AuthorizedClient authorizedClient) {
        return Map.of("token", authorizedClient.getAccessToken().getTokenValue());
    }

    @GetMapping
    public List<Author> getAll() {
        if(repository.count() == 0) {
            repository.save(new Author("Gabriel García Márquez"));
            repository.save(new Author("Isabel Allende"));
        }
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Author getById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));
    }

    @PostMapping
    public Author create(@RequestBody Author author) {
        return repository.save(author);
    }

    @PutMapping("/{id}")
    public Author update(@PathVariable Long id, @RequestBody Author authorDetails) {
        Author author = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));
        author.setName(authorDetails.getName());
        return repository.save(author);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}