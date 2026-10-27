package com.futy.competition.web;

import java.util.List;
import java.util.UUID;

import com.futy.competition.domain.StatusTemporada;
import com.futy.competition.domain.Temporada;
import com.futy.competition.domain.TemporadaRepository;
import com.futy.competition.web.dto.CriarTemporadaRequest;
import com.futy.competition.web.dto.TemporadaResponse;
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
@RequestMapping("/api/ligas/{slug}/temporadas")
public class TemporadaController {

    private final TemporadaRepository temporadaRepository;
    private final LigaAccessPolicy ligaAccessPolicy;

    public TemporadaController(TemporadaRepository temporadaRepository, LigaAccessPolicy ligaAccessPolicy) {
        this.temporadaRepository = temporadaRepository;
        this.ligaAccessPolicy = ligaAccessPolicy;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TemporadaResponse criar(@PathVariable String slug,
                                  @Valid @RequestBody CriarTemporadaRequest request,
                                  Authentication authentication) {
        if (!ligaAccessPolicy.hasAnyRole(slug, authentication, com.futy.identity.domain.Papel.ORGANIZADOR, com.futy.identity.domain.Papel.MESARIO)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }

        Temporada temporada = new Temporada(UUID.randomUUID(), request.nome(), StatusTemporada.PLANEJADA);
        return TemporadaResponse.from(temporadaRepository.save(temporada));
    }

    @GetMapping
    public List<TemporadaResponse> listar(@PathVariable String slug, Authentication authentication) {
        if (!ligaAccessPolicy.canAccess(slug, authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }
        return temporadaRepository.findAllByOrderByCriadaEmDesc().stream()
                .map(TemporadaResponse::from)
                .toList();
    }
}

