package io.github.zam0k.simplifiedpsp.controllers;

import io.github.zam0k.simplifiedpsp.controllers.dto.JuridicalPersonDTO;
import io.github.zam0k.simplifiedpsp.domain.JuridicalPerson;
import io.github.zam0k.simplifiedpsp.services.JuridicalPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/juridical-person")
@RequiredArgsConstructor
public class JuridicalPersonController {

    private final JuridicalPersonService service;

    @PostMapping
    public ResponseEntity<JuridicalPerson> create(@RequestBody JuridicalPersonDTO entity) {
        JuridicalPerson newEntity = service.save(entity);
//        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
//                .path("/{id}").buildAndExpand(newEntity.getId()).toUri();
        return ResponseEntity.ok(newEntity);
    }
}
