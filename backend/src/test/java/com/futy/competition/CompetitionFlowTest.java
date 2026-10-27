package com.futy.competition;

import com.futy.AbstractIntegrationTest;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class CompetitionFlowTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void fluxoCompletoDeTemporadaEClassificacaoFunciona() throws Exception {
        String token = criarUsuarioELogar("admin@futy.test", "senha123");

        mockMvc.perform(post("/api/ligas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content("""
                                {"slug":"liga-competicao","nome":"Liga Competicao"}
                                """))
                .andExpect(status().isCreated());

        String timeCasaId = criarTime("Time Casa", token);
        String timeVisitanteId = criarTime("Time Visitante", token);

        String temporadaId = criarTemporada(token);

        String calendario = mockMvc.perform(post("/api/ligas/liga-competicao/temporadas/{temporadaId}/gerar-calendario", temporadaId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String partidaId = JsonPath.read(calendario, "$[0].id");

        mockMvc.perform(post("/api/ligas/liga-competicao/temporadas/{temporadaId}/partidas/{partidaId}/resultado", temporadaId, partidaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content("""
                                {"golsCasa":2,"golsVisitante":1}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.golsCasa").value(2))
                .andExpect(jsonPath("$.golsVisitante").value(1));

        mockMvc.perform(get("/api/ligas/liga-competicao/temporadas/{temporadaId}/classificacao", temporadaId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].pontos").value(3));

        mockMvc.perform(post("/api/ligas/liga-competicao/temporadas/{temporadaId}/encerrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content("""
                                {"vencedorId":"%s"}
                                """.formatted(timeCasaId)))
                .andExpect(status().isOk());
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
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.read(response, "$.token");
    }

    private String criarTime(String nome, String token) throws Exception {
        String response = mockMvc.perform(post("/api/ligas/liga-competicao/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content("""
                                {"nome":"%s"}
                                """.formatted(nome)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.read(response, "$.id");
    }

    private String criarTemporada(String token) throws Exception {
        String response = mockMvc.perform(post("/api/ligas/liga-competicao/temporadas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content("""
                                {"nome":"Campeonato 2026"}
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.read(response, "$.id");
    }
}

