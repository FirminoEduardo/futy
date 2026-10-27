package com.futy.competition.web.dto;

import java.util.List;
import java.util.UUID;

public record ResumoTemporadaResponse(
        UUID temporadaId,
        String nome,
        String status,
        List<ClassificacaoResponse> classificacao,
        UUID campeaoId
) {
}

