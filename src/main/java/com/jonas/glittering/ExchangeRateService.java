package com.jonas.glittering;

import org.apache.http.client.HttpResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.Clock;

@Service
public class ExchangeRateService {

    final public static int CACHE_INVALIDATION_TIME_SECONDS = 60 * 60;

    final private static String EXCHANGE_RATES_API_KEY = "8e8dac7fabb2d29d97e023c47b05b88b";
    final private static String EXCHANGE_RATES_API_URL = "http://api.exchangeratesapi.io/v1/latest?access_key=" + EXCHANGE_RATES_API_KEY;

    @Autowired
    public ExchangeRateService(ExchangeRateResponse exchangeRateResponse, Clock clock, WebClient webClient) {
        this.cachedExchangeResponse = exchangeRateResponse;
        this.clock = clock;
        this.webClient = webClient;
    }

    private WebClient webClient;

    private ExchangeRateResponse cachedExchangeResponse;

    private Clock clock;

    public long getTimeInSeconds() {
        return clock.instant().getEpochSecond();
    }

    public boolean isCacheValid() {
        return getTimeInSeconds() - cachedExchangeResponse.getTimestamp() < CACHE_INVALIDATION_TIME_SECONDS;
    }

    public ExchangeRateResponse getValidExchangeRates() throws HttpResponseException {
        if (cachedExchangeResponse == null || !isCacheValid()) {
            try {
                cachedExchangeResponse = makeRequestWithType(EXCHANGE_RATES_API_URL, ExchangeRateResponse.class);
            } catch (WebClientException e) {
                throw new HttpResponseException(503, "Exchange rate service unavailable!");
            }
        }
        return cachedExchangeResponse;
    }

    public <T> T makeRequestWithType(String url, Class<T> responseType) throws WebClientException {
        T response = webClient
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
