package com.futy.competition.web;

import java.util.UUID;

import com.futy.competition.domain.StatusTemporada;
import com.futy.competition.domain.Temporada;
import com.futy.competition.domain.TemporadaRepository;
import com.futy.competition.web.dto.EncerrarTemporadaRequest;
import com.futy.identity.security.LigaAccessPolicy;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ligas/{slug}")
public class EncerramentoTemporadaController {

    private final TemporadaRepository temporadaRepository;
    private final LigaAccessPolicy ligaAccessPolicy;

    public EncerramentoTemporadaController(TemporadaRepository temporadaRepository, LigaAccessPolicy ligaAccessPolicy) {
        this.temporadaRepository = temporadaRepository;
        this.ligaAccessPolicy = ligaAccessPolicy;
    }

    @PostMapping("/temporadas/{temporadaId}/encerrar")
    @ResponseStatus(HttpStatus.OK)
    public Temporada encerrar(@PathVariable String slug,
                             @PathVariable UUID temporadaId,
                             @Valid @RequestBody EncerrarTemporadaRequest request,
                             Authentication authentication) {
        if (!ligaAccessPolicy.hasAnyRole(slug, authentication, com.futy.identity.domain.Papel.ORGANIZADOR, com.futy.identity.domain.Papel.MESARIO)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }

        Temporada temporada = temporadaRepository.findById(temporadaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Temporada nao encontrada"));

        temporada.setStatus(StatusTemporada.ENCERRADA);
        return temporadaRepository.save(temporada);
    }
}

