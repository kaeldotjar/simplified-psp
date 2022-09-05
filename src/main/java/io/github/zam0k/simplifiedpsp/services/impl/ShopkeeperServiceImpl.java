package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.CommonUserController;
import io.github.zam0k.simplifiedpsp.controllers.ShopkeeperController;
import io.github.zam0k.simplifiedpsp.controllers.TransactionController;
import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import io.github.zam0k.simplifiedpsp.domain.Shopkeeper;
import io.github.zam0k.simplifiedpsp.repositories.ShopkeeperRepository;
import io.github.zam0k.simplifiedpsp.repositories.TransactionRepository;
import io.github.zam0k.simplifiedpsp.services.ShopkeeperService;
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
public class ShopkeeperServiceImpl implements ShopkeeperService {

    private final ModelMapper mapper;
    private final ShopkeeperRepository repository;
    private final TransactionRepository transactionRepository;
    private final PagedResourcesAssembler pagedResourcesAssembler;

    @Override
    public ShopkeeperDTO save(ShopkeeperDTO entity) {

        Optional<Shopkeeper> cnpj = repository.findByCnpj(entity.getCnpj());

        if(cnpj.isPresent()) throw new BadRequestException("Cnpj already taken");

        Optional<Shopkeeper> email = repository.findByEmail(entity.getEmail());

        if(email.isPresent()) throw new BadRequestException("Email already taken");

        Shopkeeper shop = repository.save(mapper.map(entity, Shopkeeper.class));

        return mapper.map(shop, ShopkeeperDTO.class);
    }

    @Override
    public PagedModel<EntityModel<ShopkeeperDTO>> findAll(Pageable pageable) {

        Page<ShopkeeperDTO> shopkeeperDtoPage = repository.findAll(pageable).map(e ->
                mapper.map(e, ShopkeeperDTO.class));

        shopkeeperDtoPage = shopkeeperDtoPage.map(dto ->
                dto.add(linkTo(methodOn(ShopkeeperController.class).findOneById(dto.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(ShopkeeperController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize())).withSelfRel();

        return pagedResourcesAssembler.toModel(shopkeeperDtoPage, link);
    }

    @Override
    public ShopkeeperDTO findById(UUID id) {
        Shopkeeper entity = repository.findById(id).orElseThrow(NotFoundException::new);
        ShopkeeperDTO dto = mapper.map(entity, ShopkeeperDTO.class);

        dto.add(linkTo(methodOn(ShopkeeperController.class).findOneById(id)).withSelfRel(),
                linkTo(methodOn(ShopkeeperController.class).findAll(0, 10)).withRel("shopkeepers"));
        return dto;
    }

    @Override
    public PagedModel<EntityModel<TransactionDTO>> findTransactions(UUID id, Pageable pageable) {
        Page<TransactionDTO> transactionsDtoPage = transactionRepository.findAllByPayee(id, pageable)
                .map(el -> mapper.map(el, TransactionDTO.class));

        transactionsDtoPage = transactionsDtoPage.map(el ->
                el.add(linkTo(methodOn(TransactionController.class).findById(el.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(CommonUserController.class)
                .getUserTransactions(id, pageable.getPageNumber(), pageable.getPageSize())).withSelfRel();

        return pagedResourcesAssembler.toModel(transactionsDtoPage, link);
    }
}
