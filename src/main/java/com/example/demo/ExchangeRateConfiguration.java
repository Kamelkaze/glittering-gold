package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Clock;

@Configuration
public class ExchangeRateConfiguration {

    @Bean
    public ExchangeRateResponse exchangeRateResponse() {
        return new ExchangeRateResponse();
    }

    @Bean
    public Clock clock(){
        return Clock.systemDefaultZone();
    }

    @Bean
    public WebClient webClient() {return WebClient.create();}
}
