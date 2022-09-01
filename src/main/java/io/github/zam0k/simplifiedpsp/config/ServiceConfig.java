package io.github.zam0k.simplifiedpsp.config;

import io.github.zam0k.simplifiedpsp.utils.PaymentNotifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public PaymentNotifier paymentNotifier() {
        return new PaymentNotifier();
    }
}
