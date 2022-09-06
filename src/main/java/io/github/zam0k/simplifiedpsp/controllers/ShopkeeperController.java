package io.github.zam0k.simplifiedpsp.controllers;

import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import io.github.zam0k.simplifiedpsp.services.ShopkeeperService;
import io.github.zam0k.simplifiedpsp.services.exceptions.handler.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/shopkeepers/v1")
@RequiredArgsConstructor
@Tag(name = "Shopkeeper", description = "Endpoint for managing shopkeepers")
public class ShopkeeperController {

  private final ShopkeeperService service;

  @PostMapping
  @Operation(
          summary = "Creates a shopkeeper",
          description = "Creates a shopkeeper",
          tags = {"Shopkeeper"},
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
                          })
          })
  public ResponseEntity<ShopkeeperDTO> create(@Valid @RequestBody ShopkeeperDTO entity) {
    ShopkeeperDTO newEntity = service.save(entity);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newEntity.getKey())
            .toUri();
    return ResponseEntity.created(uri).build();
  }

  @GetMapping
  @Operation(
          summary = "Creates all shopkeepers",
          description = "Creates all shopkeepers",
          tags = {"Shopkeeper"},
          responses = {
                  @ApiResponse(
                          description = "Success",
                          responseCode = "200",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  array = @ArraySchema(schema = @Schema(implementation = ShopkeeperDTO.class)))),
                  @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                  @ApiResponse(
                          description = "Internal Server Error",
                          responseCode = "500",
                          content = {
                                  @Content(
                                          mediaType = "application/json",
                                          schema = @Schema(implementation = ApiError.class))
                          })
          })
  public ResponseEntity<PagedModel<EntityModel<ShopkeeperDTO>>> findAll(
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size) {

    Pageable pageable = PageRequest.of(page, size);

    PagedModel<EntityModel<ShopkeeperDTO>> all = service.findAll(pageable);
    if (all.getContent().isEmpty()) return ResponseEntity.noContent().build();

    return ResponseEntity.ok(all);
  }

  @GetMapping("/{id}")
  @Operation(
          summary = "Creates a transaction",
          description = "Creates a transaction",
          tags = {"Shopkeeper"},
          responses = {
                  @ApiResponse(description = "Created", responseCode = "201", content = @Content),
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
  public ResponseEntity<ShopkeeperDTO> findById(@PathVariable UUID id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @GetMapping("/{id}/transactions")
  @Operation(
          summary = "Find a transaction by its id",
          description = "Find a transaction by its id",
          tags = {"Shopkeeper"},
          responses = {
                  @ApiResponse(
                          description = "Success",
                          responseCode = "200",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  array = @ArraySchema(schema = @Schema(implementation = ShopkeeperDTO.class)))),
                  @ApiResponse(
                          description = "No Content",
                          responseCode = "204",
                          content = @Content),
                  @ApiResponse(
                          description = "Not Found",
                          responseCode = "404",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = ShopkeeperDTO.class))),
                  @ApiResponse(
                          description = "Internal Server Error",
                          responseCode = "500",
                          content = {
                                  @Content(
                                          mediaType = "application/json",
                                          schema = @Schema(implementation = ApiError.class))
                          })
          })
  public ResponseEntity<PagedModel<EntityModel<TransactionDTO>>> getUserTransactions(
      @PathVariable("id") UUID id,
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size) {

    Pageable pageable = PageRequest.of(page, size);

    PagedModel<EntityModel<TransactionDTO>> transactions = service.findTransactions(id, pageable);
    if (transactions.getContent().isEmpty()) return ResponseEntity.noContent().build();

    return ResponseEntity.ok(transactions);
  }
}
