package io.github.zam0k.simplifiedpsp.controllers;

import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import io.github.zam0k.simplifiedpsp.services.TransactionService;
import io.github.zam0k.simplifiedpsp.services.exceptions.handler.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Transaction", description = "Endpoint for managing transactions")
public class TransactionController {

  private final TransactionService service;

  @PostMapping
  @Operation(
      summary = "Creates a transaction",
      description = "Creates a transaction",
      tags = {"Transaction"},
      responses = {
        @ApiResponse(description = "Created", responseCode = "201", content = @Content),
        @ApiResponse(
            description = "Bad Request",
            responseCode = "400",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            }),
        @ApiResponse(
            description = "Internal Server Error",
            responseCode = "500",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            }),
        @ApiResponse(
            description = "Bad Gateway",
            responseCode = "502",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
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
  @Operation(
      summary = "Find a transaction by its id",
      description = "Find a transaction by its id",
      tags = {"Transaction"},
      responses = {
        @ApiResponse(
            description = "Success",
            responseCode = "200",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TransactionDTO.class))),
        @ApiResponse(
            description = "Not Found",
            responseCode = "404",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            }),
        @ApiResponse(
            description = "Internal Server Error",
            responseCode = "500",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class))
            })
      })
  public ResponseEntity<TransactionDTO> findById(@PathVariable("id") UUID id) {
    return ResponseEntity.ok(service.findById(id));
  }
}
