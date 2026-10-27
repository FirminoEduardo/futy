package com.futy.league.web;

import java.util.UUID;

import com.futy.identity.domain.Membership;
import com.futy.identity.domain.MembershipRepository;
import com.futy.identity.domain.Papel;
import com.futy.league.domain.Liga;
import com.futy.league.domain.LigaRepository;
import com.futy.league.web.dto.CriarLigaRequest;
import com.futy.league.web.dto.LigaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
    private final MembershipRepository membershipRepository;

    public LigaController(LigaRepository ligaRepository, MembershipRepository membershipRepository) {
        this.ligaRepository = ligaRepository;
        this.membershipRepository = membershipRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LigaResponse criar(@Valid @RequestBody CriarLigaRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticacao obrigatoria");
        }

        if (ligaRepository.existsBySlug(request.slug())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slug ja em uso: " + request.slug());
        }

        Liga liga = new Liga(UUID.randomUUID(), request.slug(), request.nome());
        Liga saved = ligaRepository.save(liga);

        UUID userId = UUID.fromString(authentication.getName());
        Membership membership = new Membership(UUID.randomUUID(), saved.getId(), userId, Papel.ORGANIZADOR);
        membershipRepository.save(membership);

        return LigaResponse.from(saved);
    }
}
