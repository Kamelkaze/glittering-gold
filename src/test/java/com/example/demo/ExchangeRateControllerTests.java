package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;

import java.net.http.HttpResponse;
import java.time.Clock;
import java.time.Instant;

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

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

	@Test
	public void shouldReturnDefaultMessage() throws Exception {
		Mockito.when(service.getTimeStamp()).thenReturn((long)0);
		this.mockMvc.perform(get("/time")).andDo(print());
	}



}
