package io.github.zam0k.simplifiedpsp.controllers;

import io.github.zam0k.simplifiedpsp.controllers.dto.CommonUserDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import io.github.zam0k.simplifiedpsp.services.CommonUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/common-users")
@RequiredArgsConstructor
public class CommonUserController {
    private final CommonUserService service;

    @PostMapping
    public ResponseEntity<CommonUserDTO> create(@Valid @RequestBody CommonUserDTO entity) {
        CommonUserDTO newEntity = service.save(entity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newEntity.getKey()).toUri();
           return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonUserDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<PagedModel<EntityModel<TransactionDTO>>> getUserTransactions(
            @PathVariable("id") Long id,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        PagedModel<EntityModel<TransactionDTO>> transactions = service.findTransactions(id, pageable);
        if(transactions.getContent().isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(transactions);
    }
}