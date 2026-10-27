package com.futy.competition.web;

import java.util.List;
import java.util.UUID;

import com.futy.competition.domain.Classificacao;
import com.futy.competition.domain.ClassificacaoRepository;
import com.futy.competition.domain.Temporada;
import com.futy.competition.domain.TemporadaRepository;
import com.futy.competition.web.dto.ClassificacaoResponse;
import com.futy.competition.web.dto.ResumoTemporadaResponse;
import com.futy.identity.security.LigaAccessPolicy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ligas/{slug}")
public class ResumoTemporadaController {

    private final TemporadaRepository temporadaRepository;
    private final ClassificacaoRepository classificacaoRepository;
    private final LigaAccessPolicy ligaAccessPolicy;

    public ResumoTemporadaController(TemporadaRepository temporadaRepository,
                                    ClassificacaoRepository classificacaoRepository,
                                    LigaAccessPolicy ligaAccessPolicy) {
        this.temporadaRepository = temporadaRepository;
        this.classificacaoRepository = classificacaoRepository;
        this.ligaAccessPolicy = ligaAccessPolicy;
    }

    @GetMapping("/temporadas/{temporadaId}/resumo")
    public ResumoTemporadaResponse resumo(@PathVariable String slug,
                                         @PathVariable UUID temporadaId,
                                         Authentication authentication) {
        if (!ligaAccessPolicy.canAccess(slug, authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }

        Temporada temporada = temporadaRepository.findById(temporadaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Temporada nao encontrada"));

        List<Classificacao> classificacao = classificacaoRepository.findByTemporadaIdOrderByPontosDescSaldoGolsDescGolsFeitosDesc(temporadaId);
        UUID campeaoId = classificacao.isEmpty() ? null : classificacao.getFirst().getTimeId();

        return new ResumoTemporadaResponse(
                temporada.getId(),
                temporada.getNome(),
                temporada.getStatus().name(),
                classificacao.stream().map(ClassificacaoResponse::from).toList(),
                campeaoId
        );
    }
}

