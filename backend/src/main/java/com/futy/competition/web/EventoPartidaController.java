package com.futy.competition.web;

import java.util.List;
import java.util.UUID;

import com.futy.competition.domain.EventoPartida;
import com.futy.competition.domain.EventoPartidaRepository;
import com.futy.competition.domain.Partida;
import com.futy.competition.domain.PartidaRepository;
import com.futy.competition.domain.TipoEventoPartida;
import com.futy.competition.web.dto.EventoPartidaRequest;
import com.futy.competition.web.dto.EventoPartidaResponse;
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
@RequestMapping("/api/ligas/{slug}/temporadas/{temporadaId}/partidas/{partidaId}")
public class EventoPartidaController {

    private final EventoPartidaRepository eventoPartidaRepository;
    private final PartidaRepository partidaRepository;
    private final LigaAccessPolicy ligaAccessPolicy;

    public EventoPartidaController(EventoPartidaRepository eventoPartidaRepository,
                                  PartidaRepository partidaRepository,
                                  LigaAccessPolicy ligaAccessPolicy) {
        this.eventoPartidaRepository = eventoPartidaRepository;
        this.partidaRepository = partidaRepository;
        this.ligaAccessPolicy = ligaAccessPolicy;
    }

    @PostMapping("/eventos")
    @ResponseStatus(HttpStatus.CREATED)
    public EventoPartidaResponse criar(@PathVariable String slug,
                                      @PathVariable UUID temporadaId,
                                      @PathVariable UUID partidaId,
                                      @Valid @RequestBody EventoPartidaRequest request,
                                      Authentication authentication) {
        if (!ligaAccessPolicy.hasAnyRole(slug, authentication, com.futy.identity.domain.Papel.ORGANIZADOR, com.futy.identity.domain.Papel.MESARIO)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }

        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida nao encontrada"));
        if (!partida.getTemporadaId().equals(temporadaId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Partida nao pertence a temporada informada");
        }
        if (request.tipo() == TipoEventoPartida.GOL && request.jogadorId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Jogador obrigatorio para evento de gol");
        }

        EventoPartida evento = new EventoPartida(
                UUID.randomUUID(),
                partidaId,
                request.timeId(),
                request.jogadorId(),
                request.minuto(),
                request.tipo(),
                request.descricao()
        );

        return EventoPartidaResponse.from(eventoPartidaRepository.save(evento));
    }

    @GetMapping("/eventos")
    public List<EventoPartidaResponse> listar(@PathVariable String slug,
                                            @PathVariable UUID temporadaId,
                                            @PathVariable UUID partidaId,
                                            Authentication authentication) {
        if (!ligaAccessPolicy.canAccess(slug, authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }

        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida nao encontrada"));
        if (!partida.getTemporadaId().equals(temporadaId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Partida nao pertence a temporada informada");
        }

        return eventoPartidaRepository.findByPartidaIdOrderByMinutoAsc(partidaId).stream()
                .map(EventoPartidaResponse::from)
                .toList();
    }
}

