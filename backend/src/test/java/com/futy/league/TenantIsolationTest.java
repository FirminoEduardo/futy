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
		String tokenAlpha = criarUsuarioELogar("alpha@futy.test", "senha123");
		String tokenBeta = criarUsuarioELogar("beta@futy.test", "senha123");

		criarLiga("liga-alpha", "Liga Alpha", tokenAlpha);
		criarLiga("liga-beta", "Liga Beta", tokenBeta);

		MvcResult criado = mockMvc.perform(post("/api/ligas/liga-alpha/times")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + tokenAlpha)
						.content("""
								{"nome":"Time Alpha FC"}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.nome").value("Time Alpha FC"))
				.andReturn();

		String timeId = com.jayway.jsonpath.JsonPath.read(criado.getResponse().getContentAsString(), "$.id");

		mockMvc.perform(get("/api/ligas/liga-alpha/times").header("Authorization", "Bearer " + tokenAlpha))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].nome").value("Time Alpha FC"));

		mockMvc.perform(get("/api/ligas/liga-beta/times").header("Authorization", "Bearer " + tokenBeta))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));

		mockMvc.perform(get("/api/ligas/liga-beta/times/" + timeId).header("Authorization", "Bearer " + tokenBeta))
				.andExpect(status().isNotFound());

		mockMvc.perform(get("/api/ligas/liga-alpha/times/" + timeId).header("Authorization", "Bearer " + tokenAlpha))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Time Alpha FC"));
	}

	@Test
	void jogadorDeUmaLigaNaoApareceNaOutra() throws Exception {
		String tokenGama = criarUsuarioELogar("gama@futy.test", "senha123");
		String tokenDelta = criarUsuarioELogar("delta@futy.test", "senha123");

		criarLiga("liga-gama", "Liga Gama", tokenGama);
		criarLiga("liga-delta", "Liga Delta", tokenDelta);

		MvcResult criado = mockMvc.perform(post("/api/ligas/liga-gama/jogadores")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + tokenGama)
						.content("""
								{"nome":"Jogador Gama","posicao":"ATA"}
								"""))
				.andExpect(status().isCreated())
				.andReturn();

		String jogadorId = com.jayway.jsonpath.JsonPath.read(criado.getResponse().getContentAsString(), "$.id");

		mockMvc.perform(get("/api/ligas/liga-gama/jogadores").header("Authorization", "Bearer " + tokenGama))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));

		mockMvc.perform(get("/api/ligas/liga-delta/jogadores").header("Authorization", "Bearer " + tokenDelta))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));

		mockMvc.perform(get("/api/ligas/liga-delta/jogadores/" + jogadorId).header("Authorization", "Bearer " + tokenDelta))
				.andExpect(status().isNotFound());
	}

	private String criarUsuarioELogar(String email, String senha) throws Exception {
		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"email":"%s","senha":"%s","nome":"%s"}
								""".formatted(email, senha, email.split("@")[0])))
				.andExpect(status().isOk());

		String response = mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"email":"%s","senha":"%s"}
								""".formatted(email, senha)))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		return com.jayway.jsonpath.JsonPath.read(response, "$.token");
	}

	private void criarLiga(String slug, String nome, String token) throws Exception {
		mockMvc.perform(post("/api/ligas")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + token)
						.content("""
								{"slug":"%s","nome":"%s"}
								""".formatted(slug, nome)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.slug").value(slug));
	}
}
