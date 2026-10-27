package com.futy.competition.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.futy.competition.domain.Partida;
import com.futy.competition.domain.PartidaRepository;
import com.futy.league.domain.Time;
import com.futy.league.domain.TimeRepository;
import org.springframework.stereotype.Service;

@Service
public class CalendarioService {

    private final TimeRepository timeRepository;
    private final PartidaRepository partidaRepository;

    public CalendarioService(TimeRepository timeRepository, PartidaRepository partidaRepository) {
        this.timeRepository = timeRepository;
        this.partidaRepository = partidaRepository;
    }

    public List<Partida> gerarCalendario(UUID temporadaId) {
        List<Partida> partidasExistentes = partidaRepository.findByTemporadaIdOrderByRodadaAsc(temporadaId);
        if (!partidasExistentes.isEmpty()) {
            return partidasExistentes;
        }

        List<Time> times = timeRepository.findAll();
        if (times.size() < 2) {
            return List.of();
        }

        List<UUID> ids = new ArrayList<>();
        for (Time time : times) {
            ids.add(time.getId());
        }

        List<UUID> timesEmbaralhados = new ArrayList<>(ids);
        if (timesEmbaralhados.size() % 2 != 0) {
            timesEmbaralhados.add(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        }
        Collections.shuffle(timesEmbaralhados);

        List<Partida> partidasGeradas = new ArrayList<>();
        int totalRodadas = timesEmbaralhados.size() - 1;
        for (int rodadaIndex = 0; rodadaIndex < totalRodadas; rodadaIndex++) {
            List<UUID> casa = new ArrayList<>();
            List<UUID> fora = new ArrayList<>();
            for (int i = 0; i < timesEmbaralhados.size() / 2; i++) {
                casa.add(timesEmbaralhados.get(i));
                fora.add(timesEmbaralhados.get(timesEmbaralhados.size() - 1 - i));
            }

            for (int i = 0; i < casa.size(); i++) {
                UUID timeCasa = casa.get(i);
                UUID timeVisitante = fora.get(i);
                if (timeCasa.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                        || timeVisitante.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
                    continue;
                }
                partidasGeradas.add(new Partida(UUID.randomUUID(), temporadaId, timeCasa, timeVisitante, rodadaIndex + 1));
            }

            Collections.rotate(timesEmbaralhados.subList(1, timesEmbaralhados.size()), -1);
        }

        return partidaRepository.saveAll(partidasGeradas);
    }
}
