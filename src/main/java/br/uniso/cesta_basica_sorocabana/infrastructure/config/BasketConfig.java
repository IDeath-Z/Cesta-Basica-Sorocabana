package br.uniso.cesta_basica_sorocabana.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.uniso.cesta_basica_sorocabana.application.usecases.basket.FetchStoreBasketUseCase;
import br.uniso.cesta_basica_sorocabana.domain.ports.BasketQuery;
import br.uniso.cesta_basica_sorocabana.domain.ports.StoreRepository;

@Configuration
public class BasketConfig {

    @Bean
    public FetchStoreBasketUseCase fetchStoreBasketUseCase(StoreRepository storeRepository, BasketQuery basketQuery) {
        return new FetchStoreBasketUseCase(storeRepository, basketQuery);
    }
}
