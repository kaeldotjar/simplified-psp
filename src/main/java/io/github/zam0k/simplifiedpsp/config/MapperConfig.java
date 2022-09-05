package io.github.zam0k.simplifiedpsp.config;

import io.github.zam0k.simplifiedpsp.controllers.dto.CommonUserDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperUserDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import io.github.zam0k.simplifiedpsp.domain.CommonUser;
import io.github.zam0k.simplifiedpsp.domain.ShopkeeperUser;
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
        modelMapper.createTypeMap(CommonUser.class, CommonUserDTO.class)
                .addMapping(CommonUser::getId, CommonUserDTO::setKey);

        modelMapper.createTypeMap(CommonUserDTO.class, CommonUser.class)
                .addMapping(CommonUserDTO::getKey, CommonUser::setId);
    }

    @Bean
    public void configureShopkeeperUserMapper() {
        modelMapper.createTypeMap(ShopkeeperUser.class, ShopkeeperUserDTO.class)
                .addMapping(ShopkeeperUser::getId, ShopkeeperUserDTO::setKey);

        modelMapper.createTypeMap(ShopkeeperUserDTO.class, ShopkeeperUser.class)
                .addMapping(ShopkeeperUserDTO::getKey, ShopkeeperUser::setId);
    }

    @Bean
    public void configureTransactionMapper() {
        modelMapper.createTypeMap(Transaction.class, TransactionDTO.class)
                .addMapping(Transaction::getId, TransactionDTO::setKey);

        modelMapper.createTypeMap(TransactionDTO.class, Transaction.class)
                .addMapping(TransactionDTO::getKey, Transaction::setId);
    }
}
