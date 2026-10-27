package com.futy.league.web.dto;

import java.time.Instant;
import java.util.UUID;

import com.futy.league.domain.Time;

public record TimeResponse(UUID id, UUID tenantId, String nome, Instant criadoEm) {

	public static TimeResponse from(Time time) {
		return new TimeResponse(time.getId(), time.getTenantId(), time.getNome(), time.getCriadoEm());
	}
}
