package com.fl.service.config.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FootballApiClientConfig {

    @Bean
    public FootballApiClient constructFootballApiClient() {
        return new FootballApiClient();
    }

}
