package io.github.zam0k.simplifiedpsp.controllers;

import io.github.zam0k.simplifiedpsp.controllers.dto.NaturalPersonDTO;
import io.github.zam0k.simplifiedpsp.domain.NaturalPerson;
import io.github.zam0k.simplifiedpsp.services.NaturalPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/natural-person")
@RequiredArgsConstructor
public class NaturalPersonController {
    private final NaturalPersonService service;

    @PostMapping
    public ResponseEntity<NaturalPerson> create(@RequestBody NaturalPersonDTO entity) {
        NaturalPerson newEntity = service.save(entity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newEntity.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
