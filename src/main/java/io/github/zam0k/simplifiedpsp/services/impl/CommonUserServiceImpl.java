package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.CommonUserController;
import io.github.zam0k.simplifiedpsp.controllers.TransactionController;
import io.github.zam0k.simplifiedpsp.controllers.dto.CommonUserDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import io.github.zam0k.simplifiedpsp.domain.CommonUser;
import io.github.zam0k.simplifiedpsp.repositories.CommonUserRepository;
import io.github.zam0k.simplifiedpsp.repositories.TransactionRepository;
import io.github.zam0k.simplifiedpsp.services.CommonUserService;
import io.github.zam0k.simplifiedpsp.services.exceptions.BadRequestException;
import io.github.zam0k.simplifiedpsp.services.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class CommonUserServiceImpl implements CommonUserService {

  private final ModelMapper mapper;
  private final CommonUserRepository repository;
  private final TransactionRepository transactionRepository;
  private final PagedResourcesAssembler pagedResourcesAssembler;

  @Override
  public CommonUserDTO save(CommonUserDTO entity) {
    Optional<CommonUser> cpfAlreadyExists = repository.findByCpf(entity.getCpf());

    if (cpfAlreadyExists.isPresent()) throw new BadRequestException("Cpf must be unique");

    Optional<CommonUser> emailAlreadyExists = repository.findByEmail(entity.getEmail());

    if (emailAlreadyExists.isPresent()) throw new BadRequestException("Email must be unique");

    CommonUser commonUser = repository.save(mapper.map(entity, CommonUser.class));

    return mapper.map(commonUser, CommonUserDTO.class);
  }

  @Override
  public CommonUserDTO findById(UUID id) {
    CommonUser commonUser = repository.findById(id).orElseThrow(NotFoundException::new);
    CommonUserDTO dto = mapper.map(commonUser, CommonUserDTO.class);

    dto.add(linkTo(methodOn(CommonUserController.class).findById(id)).withSelfRel());

    return dto;
  }

  @Override
  public PagedModel<EntityModel<TransactionDTO>> findTransactions(UUID id, Pageable pageable) {

    boolean isCommonUserPresent = repository.findById(id).isPresent();

    if (!isCommonUserPresent) throw new NotFoundException();

    Page<TransactionDTO> transactionsDtoPage =
        transactionRepository
            .findAllOwnerTransactions(id, pageable)
            .map(el -> mapper.map(el, TransactionDTO.class));

    transactionsDtoPage =
        transactionsDtoPage.map(
            el ->
                el.add(
                    linkTo(methodOn(TransactionController.class).findById(el.getKey()))
                        .withSelfRel()));

    Link link =
        linkTo(
                methodOn(CommonUserController.class)
                    .getUserTransactions(id, pageable.getPageNumber(), pageable.getPageSize()))
            .withSelfRel();

    return pagedResourcesAssembler.toModel(transactionsDtoPage, link);
  }
}
