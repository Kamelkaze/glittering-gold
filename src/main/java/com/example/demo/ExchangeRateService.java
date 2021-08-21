package com.example.demo;

import org.apache.http.client.HttpResponseException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;

@Service
public class ExchangeRateService {

    final private static int CACHE_INVALIDATION_TIME_SECONDS = 60 * 60;

    final private static String EXCHANGE_RATES_API_URL = "http://api.exchangeratesapi.io/v1/latest?access_key=8e8dac7fabb2d29d97e023c47b05b88b";

    private ExchangeRateResponse cachedExchangeResponse = null;

    public long getCacheInvalidationTimeInSeconds() {
        return CACHE_INVALIDATION_TIME_SECONDS;
    }

    public long getTimeStamp() {
        return Instant.now().getEpochSecond();
    }

    public boolean isCacheOutdated() {
        return getTimeStamp() - cachedExchangeResponse.getTimestamp() >= getCacheInvalidationTimeInSeconds();
    }

    public ExchangeRateResponse getValidExchangeRates() throws HttpResponseException {
        if (cachedExchangeResponse == null || isCacheOutdated()) {
            try {
                cachedExchangeResponse = makeRequestWithType(EXCHANGE_RATES_API_URL, ExchangeRateResponse.class);
            } catch (Exception e) {
                throw new HttpResponseException(503, "Exchange rate service unavailable!");
            }
        }
        return cachedExchangeResponse;
    }

    public <T> T makeRequestWithType(String url, Class<T> responseType) {
        T response = WebClient.create()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(responseType).block();

        return response;
    }

    public double convertCurrency(double amount, double sourceRate, double targetRate) {
        return amount / sourceRate * targetRate;
    }

}
