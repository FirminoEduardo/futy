package com.futy.competition.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoPartidaRepository extends JpaRepository<EventoPartida, UUID> {

    List<EventoPartida> findByPartidaIdOrderByMinutoAsc(UUID partidaId);
}

