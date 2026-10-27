package com.futy.competition.web;

import java.util.UUID;

import com.futy.competition.domain.Partida;
import com.futy.competition.domain.PartidaRepository;
import com.futy.competition.service.ClassificacaoService;
import com.futy.competition.web.dto.PartidaResponse;
import com.futy.competition.web.dto.RegistrarResultadoRequest;
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
@RequestMapping("/api/ligas/{slug}/temporadas/{temporadaId}/partidas")
public class ResultadoPartidaController {

    private final PartidaRepository partidaRepository;
    private final ClassificacaoService classificacaoService;
    private final LigaAccessPolicy ligaAccessPolicy;

    public ResultadoPartidaController(PartidaRepository partidaRepository,
                                     ClassificacaoService classificacaoService,
                                     LigaAccessPolicy ligaAccessPolicy) {
        this.partidaRepository = partidaRepository;
        this.classificacaoService = classificacaoService;
        this.ligaAccessPolicy = ligaAccessPolicy;
    }

    @PostMapping("/{partidaId}/resultado")
    @ResponseStatus(HttpStatus.OK)
    public PartidaResponse registrarResultado(@PathVariable String slug,
                                            @PathVariable UUID temporadaId,
                                            @PathVariable UUID partidaId,
                                            @Valid @RequestBody RegistrarResultadoRequest request,
                                            Authentication authentication) {
        if (!ligaAccessPolicy.hasAnyRole(slug, authentication, com.futy.identity.domain.Papel.ORGANIZADOR, com.futy.identity.domain.Papel.MESARIO)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }

        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida nao encontrada"));

        if (!partida.getTemporadaId().equals(temporadaId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Partida nao pertence a temporada informada");
        }

        partida.registrarResultado(request.golsCasa(), request.golsVisitante());
        partidaRepository.save(partida);
        classificacaoService.atualizarClassificacao(temporadaId);

        return PartidaResponse.from(partida);
    }
}

