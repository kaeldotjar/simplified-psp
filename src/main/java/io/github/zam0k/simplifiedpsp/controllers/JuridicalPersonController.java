package io.github.zam0k.simplifiedpsp.controllers;

import io.github.zam0k.simplifiedpsp.controllers.dto.JuridicalPersonDTO;
import io.github.zam0k.simplifiedpsp.domain.JuridicalPerson;
import io.github.zam0k.simplifiedpsp.services.JuridicalPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/juridical-person")
@RequiredArgsConstructor
public class JuridicalPersonController {

    private final JuridicalPersonService service;

    @PostMapping
    public ResponseEntity<JuridicalPerson> create(@RequestBody JuridicalPersonDTO entity) {
        JuridicalPerson newEntity = service.save(entity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newEntity.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<JuridicalPerson>> findAll() {
        List<JuridicalPerson> all = service.findAll();
        if(all.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JuridicalPerson> findOneById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findOneById(id));
    }

}
