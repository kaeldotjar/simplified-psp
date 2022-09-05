package io.github.zam0k.simplifiedpsp.controllers;

import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import io.github.zam0k.simplifiedpsp.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions/v1")
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService service;

  @PostMapping
  public ResponseEntity<TransactionDTO> create(@Valid @RequestBody TransactionDTO transaction) {
    TransactionDTO newEntity = service.create(transaction);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newEntity.getKey())
            .toUri();

    return ResponseEntity.created(uri).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<TransactionDTO> findById(@PathVariable("id") UUID id) {
    return ResponseEntity.ok(service.findById(id));
  }
}
