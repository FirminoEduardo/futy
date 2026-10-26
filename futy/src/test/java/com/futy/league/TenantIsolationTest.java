package com.futy.league;

import com.futy.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class TenantIsolationTest extends AbstractIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void timeDeUmaLigaNaoApareceNaOutra() throws Exception {
		criarLiga("liga-alpha", "Liga Alpha");
		criarLiga("liga-beta", "Liga Beta");

		MvcResult criado = mockMvc.perform(post("/api/ligas/liga-alpha/times")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"nome":"Time Alpha FC"}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.nome").value("Time Alpha FC"))
				.andReturn();

		String timeId = com.jayway.jsonpath.JsonPath.read(criado.getResponse().getContentAsString(), "$.id");

		mockMvc.perform(get("/api/ligas/liga-alpha/times"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].nome").value("Time Alpha FC"));

		mockMvc.perform(get("/api/ligas/liga-beta/times"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));

		mockMvc.perform(get("/api/ligas/liga-beta/times/" + timeId))
				.andExpect(status().isNotFound());

		mockMvc.perform(get("/api/ligas/liga-alpha/times/" + timeId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Time Alpha FC"));
	}

	@Test
	void jogadorDeUmaLigaNaoApareceNaOutra() throws Exception {
		criarLiga("liga-gama", "Liga Gama");
		criarLiga("liga-delta", "Liga Delta");

		MvcResult criado = mockMvc.perform(post("/api/ligas/liga-gama/jogadores")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"nome":"Jogador Gama","posicao":"ATA"}
								"""))
				.andExpect(status().isCreated())
				.andReturn();

		String jogadorId = com.jayway.jsonpath.JsonPath.read(criado.getResponse().getContentAsString(), "$.id");

		mockMvc.perform(get("/api/ligas/liga-gama/jogadores"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));

		mockMvc.perform(get("/api/ligas/liga-delta/jogadores"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));

		mockMvc.perform(get("/api/ligas/liga-delta/jogadores/" + jogadorId))
				.andExpect(status().isNotFound());
	}

	private void criarLiga(String slug, String nome) throws Exception {
		mockMvc.perform(post("/api/ligas")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"slug":"%s","nome":"%s"}
								""".formatted(slug, nome)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.slug").value(slug));
	}
}
