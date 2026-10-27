package com.futy.competition.web.dto;

import java.util.UUID;

import com.futy.competition.domain.Classificacao;

public record ClassificacaoResponse(UUID timeId, Integer pontos, Integer jogos, Integer vitorias, Integer empates,
                                   Integer derrotas, Integer golsFeitos, Integer golsSofridos, Integer saldoGols) {

    public static ClassificacaoResponse from(Classificacao classificacao) {
        return new ClassificacaoResponse(
                classificacao.getTimeId(),
                classificacao.getPontos(),
                classificacao.getJogos(),
                classificacao.getVitorias(),
                classificacao.getEmpates(),
                classificacao.getDerrotas(),
                classificacao.getGolsFeitos(),
                classificacao.getGolsSofridos(),
                classificacao.getSaldoGols()
        );
    }
}

