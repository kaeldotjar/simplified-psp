package io.github.zam0k.simplifiedpsp.config;

import io.github.zam0k.simplifiedpsp.controllers.dto.CommonUserDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import io.github.zam0k.simplifiedpsp.domain.CommonUser;
import io.github.zam0k.simplifiedpsp.domain.Shopkeeper;
import io.github.zam0k.simplifiedpsp.domain.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

  ModelMapper modelMapper;

  public MapperConfig() {
    modelMapper = new ModelMapper();
  }

  @Bean
  public ModelMapper modelMapper() {
    return modelMapper;
  }

  @Bean
  public void configureCommonUserMapper() {
    modelMapper
        .createTypeMap(CommonUser.class, CommonUserDTO.class)
        .addMapping(CommonUser::getId, CommonUserDTO::setKey);

    modelMapper
        .createTypeMap(CommonUserDTO.class, CommonUser.class)
        .addMapping(CommonUserDTO::getKey, CommonUser::setId);
  }

  @Bean
  public void configureShopkeeperMapper() {
    modelMapper
        .createTypeMap(Shopkeeper.class, ShopkeeperDTO.class)
        .addMapping(Shopkeeper::getId, ShopkeeperDTO::setKey);

    modelMapper
        .createTypeMap(ShopkeeperDTO.class, Shopkeeper.class)
        .addMapping(ShopkeeperDTO::getKey, Shopkeeper::setId);
  }

  @Bean
  public void configureTransactionMapper() {
    modelMapper
        .createTypeMap(Transaction.class, TransactionDTO.class)
        .addMapping(Transaction::getId, TransactionDTO::setKey);

    modelMapper
        .createTypeMap(TransactionDTO.class, Transaction.class)
        .addMapping(TransactionDTO::getKey, Transaction::setId);
  }
}
