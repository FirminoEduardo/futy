package com.futy.competition.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassificacaoRepository extends JpaRepository<Classificacao, UUID> {

    List<Classificacao> findByTemporadaIdOrderByPontosDescSaldoGolsDescGolsFeitosDesc(UUID temporadaId);
}

