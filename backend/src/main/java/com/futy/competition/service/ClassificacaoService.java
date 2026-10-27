package com.futy.competition.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.futy.competition.domain.Classificacao;
import com.futy.competition.domain.ClassificacaoRepository;
import com.futy.competition.domain.Partida;
import com.futy.competition.domain.PartidaRepository;
import com.futy.league.domain.Time;
import com.futy.league.domain.TimeRepository;
import org.springframework.stereotype.Service;

@Service
public class ClassificacaoService {

    private final TimeRepository timeRepository;
    private final PartidaRepository partidaRepository;
    private final ClassificacaoRepository classificacaoRepository;

    public ClassificacaoService(TimeRepository timeRepository,
                               PartidaRepository partidaRepository,
                               ClassificacaoRepository classificacaoRepository) {
        this.timeRepository = timeRepository;
        this.partidaRepository = partidaRepository;
        this.classificacaoRepository = classificacaoRepository;
    }

    public List<Classificacao> atualizarClassificacao(UUID temporadaId) {
        List<Time> times = timeRepository.findAll();
        List<Classificacao> classificacoes = new ArrayList<>();

        for (Time time : times) {
            classificacoes.add(new Classificacao(UUID.randomUUID(), temporadaId, time.getId()));
        }

        List<Partida> partidas = partidaRepository.findByTemporadaIdOrderByRodadaAsc(temporadaId);
        for (Partida partida : partidas) {
            if (partida.getStatus() != com.futy.competition.domain.StatusPartida.CONCLUIDA) {
                continue;
            }

            Classificacao casa = classificacoes.stream()
                    .filter(c -> c.getTimeId().equals(partida.getTimeCasaId()))
                    .findFirst()
                    .orElse(null);
            Classificacao visitante = classificacoes.stream()
                    .filter(c -> c.getTimeId().equals(partida.getTimeVisitanteId()))
                    .findFirst()
                    .orElse(null);

            if (casa == null || visitante == null) {
                continue;
            }

            casa.aplicarResultado(partida.getGolsCasa(), partida.getGolsVisitante());
            visitante.aplicarResultado(partida.getGolsVisitante(), partida.getGolsCasa());
        }

        classificacoes.sort(Comparator
                .comparing(Classificacao::getPontos).reversed()
                .thenComparing(Classificacao::getSaldoGols).reversed()
                .thenComparing(Classificacao::getGolsFeitos).reversed());

        return classificacaoRepository.saveAll(classificacoes);
    }
}
