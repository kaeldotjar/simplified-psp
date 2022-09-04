package io.github.zam0k.simplifiedpsp.controllers;

import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperUserDTO;
import io.github.zam0k.simplifiedpsp.services.ShopkeeperUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/shopkeepers")
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
    public ResponseEntity<Page<ShopkeeperUserDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ShopkeeperUserDTO> all = service.findAll(pageable);
        if(all.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopkeeperUserDTO> findOneById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findOneById(id));
    }

}
