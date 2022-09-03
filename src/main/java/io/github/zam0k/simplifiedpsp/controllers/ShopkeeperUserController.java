package io.github.zam0k.simplifiedpsp.controllers;

import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperUserDTO;
import io.github.zam0k.simplifiedpsp.domain.ShopkeeperUser;
import io.github.zam0k.simplifiedpsp.services.ShopkeeperUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/juridical-person")
@RequiredArgsConstructor
public class ShopkeeperUserController {

    private final ShopkeeperUserService service;

    @PostMapping
    public ResponseEntity<ShopkeeperUser> create(@Valid @RequestBody ShopkeeperUserDTO entity) {
        ShopkeeperUser newEntity = service.save(entity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newEntity.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<ShopkeeperUser>> findAll() {
        List<ShopkeeperUser> all = service.findAll();
        if(all.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopkeeperUser> findOneById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findOneById(id));
    }

}
