package com.futy.competition.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RegistrarResultadoRequest(
        @NotNull @Min(0) Integer golsCasa,
        @NotNull @Min(0) Integer golsVisitante
) {
}

