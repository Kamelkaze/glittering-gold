package com.jonas.glittering;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ExchangeRateControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ExchangeRateController controller;

	@MockBean
	private ExchangeRateService service;

	private ExchangeRateResponse response = null;

	@BeforeEach
	void setup() {
		response = new ExchangeRateResponse();
		Map<String, Double> rates = new HashMap<>(){{
			put("EUR", 1.0);
			put("USD", 0.5);
			put("SEK", 0.1);
		}};
		response.setRates(rates);
	}

	@Test
	public void shouldReturnListOfCurrencies() throws Exception {
		String expectedResponse = "[\"EUR\",\"USD\",\"SEK\"]";
		Mockito.when(service.getValidExchangeRates()).thenReturn(response);
		this.mockMvc.perform(get("/currencies")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(expectedResponse));
	}

	@Test
	public void shouldReturnAmountWhenCurrenciesAreFound() throws Exception {
		Mockito.when(service.getValidExchangeRates()).thenReturn(response);
		Mockito.when(service.convertCurrency(anyDouble(), anyDouble(), anyDouble())).thenReturn(5.0);
		this.mockMvc.perform(get("/convertCurrency").
						param("sourceCurrency", "EUR")
						.param("targetCurrency", "USD")
						.param("amount", "1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("5.0"));
	}

	@Test
	public void shouldFailIfACurrencyIsNotFound() throws Exception {
		Mockito.when(service.getValidExchangeRates()).thenReturn(response);
		Mockito.when(service.convertCurrency(anyDouble(), anyDouble(), anyDouble())).thenReturn(5.0);
		this.mockMvc.perform(get("/convertCurrency").
						param("sourceCurrency", "NOK")
						.param("targetCurrency", "USD")
						.param("amount", "1"))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

}
