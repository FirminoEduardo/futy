package com.futy.competition.web;

import java.util.List;
import java.util.UUID;

import com.futy.competition.domain.Classificacao;
import com.futy.competition.domain.ClassificacaoRepository;
import com.futy.competition.domain.Partida;
import com.futy.competition.domain.PartidaRepository;
import com.futy.competition.service.CalendarioService;
import com.futy.competition.service.ClassificacaoService;
import com.futy.competition.web.dto.ClassificacaoResponse;
import com.futy.competition.web.dto.PartidaResponse;
import com.futy.identity.security.LigaAccessPolicy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ligas/{slug}")
public class CompeticaoController {

    private final CalendarioService calendarioService;
    private final ClassificacaoService classificacaoService;
    private final PartidaRepository partidaRepository;
    private final ClassificacaoRepository classificacaoRepository;
    private final LigaAccessPolicy ligaAccessPolicy;

    public CompeticaoController(CalendarioService calendarioService,
                               ClassificacaoService classificacaoService,
                               PartidaRepository partidaRepository,
                               ClassificacaoRepository classificacaoRepository,
                               LigaAccessPolicy ligaAccessPolicy) {
        this.calendarioService = calendarioService;
        this.classificacaoService = classificacaoService;
        this.partidaRepository = partidaRepository;
        this.classificacaoRepository = classificacaoRepository;
        this.ligaAccessPolicy = ligaAccessPolicy;
    }

    @PostMapping("/temporadas/{temporadaId}/gerar-calendario")
    @ResponseStatus(HttpStatus.CREATED)
    public List<PartidaResponse> gerarCalendario(@PathVariable String slug,
                                               @PathVariable UUID temporadaId,
                                               Authentication authentication) {
        if (!ligaAccessPolicy.hasAnyRole(slug, authentication, com.futy.identity.domain.Papel.ORGANIZADOR, com.futy.identity.domain.Papel.MESARIO)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }

        List<Partida> partidas = calendarioService.gerarCalendario(temporadaId);
        return partidas.stream().map(PartidaResponse::from).toList();
    }

    @PostMapping("/temporadas/{temporadaId}/atualizar-classificacao")
    @ResponseStatus(HttpStatus.OK)
    public List<ClassificacaoResponse> atualizarClassificacao(@PathVariable String slug,
                                                            @PathVariable UUID temporadaId,
                                                            Authentication authentication) {
        if (!ligaAccessPolicy.hasAnyRole(slug, authentication, com.futy.identity.domain.Papel.ORGANIZADOR, com.futy.identity.domain.Papel.MESARIO)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }

        List<Classificacao> classificacoes = classificacaoService.atualizarClassificacao(temporadaId);
        return classificacoes.stream().map(ClassificacaoResponse::from).toList();
    }

    @GetMapping("/temporadas/{temporadaId}/classificacao")
    public List<ClassificacaoResponse> listarClassificacao(@PathVariable String slug,
                                                         @PathVariable UUID temporadaId,
                                                         Authentication authentication) {
        if (!ligaAccessPolicy.canAccess(slug, authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }

        return classificacaoRepository.findByTemporadaIdOrderByPontosDescSaldoGolsDescGolsFeitosDesc(temporadaId)
                .stream()
                .map(ClassificacaoResponse::from)
                .toList();
    }
}

