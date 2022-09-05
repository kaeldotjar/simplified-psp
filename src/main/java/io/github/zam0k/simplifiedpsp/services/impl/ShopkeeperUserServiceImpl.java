package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.CommonUserController;
import io.github.zam0k.simplifiedpsp.controllers.ShopkeeperUserController;
import io.github.zam0k.simplifiedpsp.controllers.TransactionController;
import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperUserDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import io.github.zam0k.simplifiedpsp.domain.ShopkeeperUser;
import io.github.zam0k.simplifiedpsp.repositories.ShopkeeperUserRepository;
import io.github.zam0k.simplifiedpsp.repositories.TransactionRepository;
import io.github.zam0k.simplifiedpsp.services.ShopkeeperUserService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class ShopkeeperUserServiceImpl implements ShopkeeperUserService {

    private final ModelMapper mapper;
    private final ShopkeeperUserRepository repository;
    private final TransactionRepository transactionRepository;
    private final PagedResourcesAssembler pagedResourcesAssembler;

    @Override
    public ShopkeeperUserDTO save(ShopkeeperUserDTO entity) {

        Optional<ShopkeeperUser> cnpj = repository.findByCnpj(entity.getCnpj());

        if(cnpj.isPresent()) throw new BadRequestException("Cnpj already taken");

        Optional<ShopkeeperUser> email = repository.findByEmail(entity.getEmail());

        if(email.isPresent()) throw new BadRequestException("Email already taken");

        ShopkeeperUser shop = repository.save(mapper.map(entity, ShopkeeperUser.class));

        return mapper.map(shop, ShopkeeperUserDTO.class);
    }

    @Override
    public PagedModel<EntityModel<ShopkeeperUserDTO>> findAll(Pageable pageable) {

        Page<ShopkeeperUserDTO> shopkeeperDtoPage = repository.findAll(pageable).map(e ->
                mapper.map(e, ShopkeeperUserDTO.class));

        shopkeeperDtoPage = shopkeeperDtoPage.map(dto ->
                dto.add(linkTo(methodOn(ShopkeeperUserController.class).findOneById(dto.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(ShopkeeperUserController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize())).withSelfRel();

        return pagedResourcesAssembler.toModel(shopkeeperDtoPage, link);
    }

    @Override
    public ShopkeeperUserDTO findById(Long id) {
        ShopkeeperUser entity = repository.findById(id).orElseThrow(NotFoundException::new);
        ShopkeeperUserDTO dto = mapper.map(entity, ShopkeeperUserDTO.class);

        dto.add(linkTo(methodOn(ShopkeeperUserController.class).findOneById(id)).withSelfRel(),
                linkTo(methodOn(ShopkeeperUserController.class).findAll(0, 10)).withRel("shopkeepers"));
        return dto;
    }

    @Override
    public PagedModel<EntityModel<TransactionDTO>> findTransactions(Long id, Pageable pageable) {
        Page<TransactionDTO> transactionsDtoPage = transactionRepository.findAllByPayee(id, pageable)
                .map(el -> mapper.map(el, TransactionDTO.class));

        transactionsDtoPage = transactionsDtoPage.map(el ->
                el.add(linkTo(methodOn(TransactionController.class).findById(el.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(CommonUserController.class)
                .getUserTransactions(id, pageable.getPageNumber(), pageable.getPageSize())).withSelfRel();

        return pagedResourcesAssembler.toModel(transactionsDtoPage, link);
    }
}
