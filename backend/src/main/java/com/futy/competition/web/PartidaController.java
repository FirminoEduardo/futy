package com.futy.competition.web;

import java.util.List;
import java.util.UUID;

import com.futy.competition.domain.Partida;
import com.futy.competition.domain.PartidaRepository;
import com.futy.competition.web.dto.PartidaResponse;
import com.futy.identity.security.LigaAccessPolicy;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ligas/{slug}/temporadas/{temporadaId}/partidas")
public class PartidaController {

    private final PartidaRepository partidaRepository;
    private final LigaAccessPolicy ligaAccessPolicy;

    public PartidaController(PartidaRepository partidaRepository, LigaAccessPolicy ligaAccessPolicy) {
        this.partidaRepository = partidaRepository;
        this.ligaAccessPolicy = ligaAccessPolicy;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PartidaResponse criar(@PathVariable String slug,
                                @PathVariable UUID temporadaId,
                                @Valid @RequestBody java.util.Map<String, Object> body,
                                Authentication authentication) {
        if (!ligaAccessPolicy.hasAnyRole(slug, authentication, com.futy.identity.domain.Papel.ORGANIZADOR, com.futy.identity.domain.Papel.MESARIO)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }

        UUID timeCasaId = UUID.fromString(body.get("timeCasaId").toString());
        UUID timeVisitanteId = UUID.fromString(body.get("timeVisitanteId").toString());
        Integer rodada = Integer.valueOf(body.get("rodada").toString());

        Partida partida = new Partida(UUID.randomUUID(), temporadaId, timeCasaId, timeVisitanteId, rodada);
        return PartidaResponse.from(partidaRepository.save(partida));
    }

    @GetMapping
    public List<PartidaResponse> listar(@PathVariable String slug,
                                      @PathVariable UUID temporadaId,
                                      Authentication authentication) {
        if (!ligaAccessPolicy.canAccess(slug, authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }
        return partidaRepository.findByTemporadaIdOrderByRodadaAsc(temporadaId).stream()
                .map(PartidaResponse::from)
                .toList();
    }
}

