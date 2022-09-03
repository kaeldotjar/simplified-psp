package io.github.zam0k.simplifiedpsp.controllers;

import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperUserDTO;
import io.github.zam0k.simplifiedpsp.services.ShopkeeperUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/shopkeeper")
@RequiredArgsConstructor
public class ShopkeeperUserController {

    private final ShopkeeperUserService service;

    @PostMapping
    public ResponseEntity<ShopkeeperUserDTO> create(@Valid @RequestBody ShopkeeperUserDTO entity) {
        ShopkeeperUserDTO newEntity = service.save(entity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newEntity.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<ShopkeeperUserDTO>> findAll() {
        List<ShopkeeperUserDTO> all = service.findAll();
        if(all.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopkeeperUserDTO> findOneById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findOneById(id));
    }

}
