package io.github.zam0k.simplifiedpsp.controllers;

import io.github.zam0k.simplifiedpsp.controllers.dto.CommonUserDTO;
import io.github.zam0k.simplifiedpsp.services.CommonUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/common-user")
@RequiredArgsConstructor
public class CommonUserController {
    private final CommonUserService service;

    @PostMapping
    public ResponseEntity<CommonUserDTO> create(@Valid @RequestBody CommonUserDTO entity) {
        CommonUserDTO newEntity = service.save(entity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newEntity.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}