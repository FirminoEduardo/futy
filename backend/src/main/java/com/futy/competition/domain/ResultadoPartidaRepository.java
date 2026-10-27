package com.futy.competition.domain;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultadoPartidaRepository extends JpaRepository<ResultadoPartida, UUID> {

    Optional<ResultadoPartida> findByPartidaId(UUID partidaId);
}

