package com.futy.league.web;

import java.util.UUID;

import com.futy.league.domain.Liga;
import com.futy.league.domain.LigaRepository;
import com.futy.league.web.dto.CriarLigaRequest;
import com.futy.league.web.dto.LigaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ligas")
public class LigaController {

	private final LigaRepository ligaRepository;

	public LigaController(LigaRepository ligaRepository) {
		this.ligaRepository = ligaRepository;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public LigaResponse criar(@Valid @RequestBody CriarLigaRequest request) {
		if (ligaRepository.existsBySlug(request.slug())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Slug ja em uso: " + request.slug());
		}
		Liga liga = new Liga(UUID.randomUUID(), request.slug(), request.nome());
		return LigaResponse.from(ligaRepository.save(liga));
	}
}
