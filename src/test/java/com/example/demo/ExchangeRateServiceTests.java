package com.example.demo;

import org.apache.http.client.HttpResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
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
@AutoConfigureMockMvc
class ExchangeRateServiceTests {

    String testJson = "{\"success\":true,\"timestamp\":1628021764,\"base\":\"EUR\",\"date\":\"2021-08-03\",\"rates\":{\"AED\":4.356844,\"AFN\":94.835396,\"ALL\":122.236082,\"AMD\":582.333942,\"ANG\":2.129224,\"AOA\":757.451496,\"ARS\":114.83837,\"AUD\":1.604471,\"AWG\":2.135726,\"AZN\":2.014759,\"BAM\":1.951544,\"BBD\":2.39504,\"BDT\":100.612247,\"BGN\":1.952932,\"BHD\":0.447234,\"BIF\":2356.949661,\"BMD\":1.186185,\"BND\":1.601766,\"BOB\":8.190613,\"BRL\":6.161282,\"BSD\":1.186195,\"BTC\":3.1252203e-5,\"BTN\":88.039358,\"BWP\":12.992268,\"BYN\":2.972472,\"BYR\":23249.226648,\"BZD\":2.390949,\"CAD\":1.487417,\"CDF\":2372.369688,\"CHF\":1.072596,\"CLF\":0.033369,\"CLP\":920.728983,\"CNY\":7.674968,\"COP\":4637.686933,\"CRC\":735.674394,\"CUC\":1.186185,\"CUP\":31.433903,\"CVE\":110.023379,\"CZK\":25.460159,\"DJF\":210.808443,\"DKK\":7.437144,\"DOP\":67.671388,\"DZD\":159.599458,\"EGP\":18.623456,\"ERN\":17.797816,\"ETB\":52.536037,\"EUR\":1,\"FJD\":2.480072,\"FKP\":0.857328,\"GBP\":0.852512,\"GEL\":3.659418,\"GGP\":0.857328,\"GHS\":7.117326,\"GIP\":0.857328,\"GMD\":60.67312,\"GNF\":11570.09106,\"GTQ\":9.194708,\"GYD\":248.165008,\"HKD\":9.227274,\"HNL\":28.149182,\"HRK\":7.499537,\"HTG\":115.395581,\"HUF\":355.74994,\"IDR\":16983.619375,\"ILS\":3.810667,\"IMP\":0.857328,\"INR\":88.02964,\"IQD\":1731.830148,\"IRR\":49944.320707,\"ISK\":146.991708,\"JEP\":0.857328,\"JMD\":183.265533,\"JOD\":0.841019,\"JPY\":129.362993,\"KES\":128.87897,\"KGS\":100.476158,\"KHR\":4839.63451,\"KMF\":493.453399,\"KPW\":1067.566537,\"KRW\":1362.641821,\"KWD\":0.356208,\"KYD\":0.988529,\"KZT\":503.389051,\"LAK\":11345.860154,\"LBP\":1796.447267,\"LKR\":236.612063,\"LRD\":203.608737,\"LSL\":17.602672,\"LTL\":3.502496,\"LVL\":0.717511,\"LYD\":5.349965,\"MAD\":10.588479,\"MDL\":21.209023,\"MGA\":4495.547381,\"MKD\":61.476882,\"MMK\":1952.197877,\"MNT\":3378.390874,\"MOP\":9.501053,\"MRO\":423.467853,\"MUR\":50.472555,\"MVR\":18.326788,\"MWK\":948.947841,\"MXN\":23.582533,\"MYR\":5.022305,\"MZN\":75.47719,\"NAD\":17.603018,\"NGN\":488.113937,\"NIO\":41.652527,\"NOK\":10.457603,\"NPR\":140.843697,\"NZD\":1.691292,\"OMR\":0.456703,\"PAB\":1.186195,\"PEN\":4.65756,\"PGK\":4.165191,\"PHP\":59.02451,\"PKR\":191.920926,\"PLN\":4.553361,\"PYG\":8196.699325,\"QAR\":4.31886,\"RON\":4.917969,\"RSD\":117.306451,\"RUB\":86.520458,\"RWF\":1180.254108,\"SAR\":4.448531,\"SBD\":9.562278,\"SCR\":18.705241,\"SDG\":529.591489,\"SEK\":10.20297,\"SGD\":1.603133,\"SHP\":0.857328,\"SLL\":12164.3279,\"SOS\":693.918927,\"SRD\":25.411049,\"STD\":24435.20295,\"SVC\":10.379206,\"SYP\":1491.468427,\"SZL\":17.603197,\"THB\":39.180023,\"TJS\":13.527807,\"TMT\":4.163509,\"TND\":3.310049,\"TOP\":2.6822,\"TRY\":9.964903,\"TTD\":8.044533,\"TWD\":33.095741,\"TZS\":2750.759603,\"UAH\":31.81583,\"UGX\":4213.347118,\"USD\":1.186185,\"UYU\":51.854124,\"UZS\":12628.696269,\"VEF\":253642209349.70703,\"VND\":27228.877434,\"VUV\":130.621155,\"WST\":3.026736,\"XAF\":654.431537,\"XAG\":0.046438,\"XAU\":0.000655,\"XCD\":3.205724,\"XDR\":0.830769,\"XOF\":654.519617,\"XPF\":120.338525,\"YER\":296.665278,\"ZAR\":16.972651,\"ZMK\":10677.086822,\"ZMW\":22.786658,\"ZWL\":381.951864}}";

    @Autowired
    private MockMvc mockMvc;

    private ExchangeRateResponse exchangeRateResponse;

    private Clock clock;

    private ExchangeRateService service;

    @MockBean
    private WebClient webClient;

    @BeforeEach
    void setup() {

    }

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