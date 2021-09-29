package com.jonas.glittering;

import org.apache.http.client.HttpResponseException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.Clock;
import java.time.Instant;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class ExchangeRateServiceTests {

    private ExchangeRateResponse exchangeRateResponse;

    private Clock clock;

    private ExchangeRateService service;

    @MockBean
    private WebClient webClient;

    private void setupServiceFromTime(long currentTime, long exchangeRateTimestamp) {
        exchangeRateResponse = new ExchangeRateResponse();
        exchangeRateResponse.setTimestamp(exchangeRateTimestamp);
        clock = Clock.fixed(Instant.ofEpochSecond(currentTime), TimeZone.getDefault().toZoneId());
        service = new ExchangeRateService(exchangeRateResponse, clock, null);
    }

    @Test
    public void shouldReturnFalseWhenCacheIsOutdated() throws Exception {
        setupServiceFromTime(ExchangeRateService.CACHE_INVALIDATION_TIME_SECONDS + 1, 0);
        assertFalse(service.isCacheValid());
    }

    @Test
    public void shouldReturnTrueWhenCacheIsNotOutdated() throws Exception {
        setupServiceFromTime(ExchangeRateService.CACHE_INVALIDATION_TIME_SECONDS - 1, 0);
        assertTrue(service.isCacheValid());
    }

    @Test
    public void shouldBeDoubleAmount() {
        service = new ExchangeRateService(null, null, null);
        double result = service.convertCurrency(10, 0.5, 2);
        System.out.println(result);
        assertTrue(result == 40);
    }

    @Test
    public void shouldThrowExceptionWhenUnableToUpdateExchangeRates() {
        service = new ExchangeRateService(null, null, webClient);
        Mockito.when(webClient.get()).thenThrow(new WebClientException("") {});
        assertThrows(HttpResponseException.class, () -> {
            service.getValidExchangeRates();
        });
    }
}