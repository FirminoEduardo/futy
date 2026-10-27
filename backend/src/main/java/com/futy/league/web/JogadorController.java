package com.futy.league.web;

import java.util.List;
import java.util.UUID;

import com.futy.identity.security.LigaAccessPolicy;
import com.futy.league.domain.Jogador;
import com.futy.league.domain.JogadorRepository;
import com.futy.league.web.dto.CriarJogadorRequest;
import com.futy.league.web.dto.JogadorResponse;
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
@RequestMapping("/api/ligas/{slug}/jogadores")
public class JogadorController {

    private final JogadorRepository jogadorRepository;
    private final LigaAccessPolicy ligaAccessPolicy;

    public JogadorController(JogadorRepository jogadorRepository, LigaAccessPolicy ligaAccessPolicy) {
        this.jogadorRepository = jogadorRepository;
        this.ligaAccessPolicy = ligaAccessPolicy;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JogadorResponse criar(@PathVariable String slug, @Valid @RequestBody CriarJogadorRequest request, Authentication authentication) {
        if (!ligaAccessPolicy.canAccess(slug, authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }
        Jogador jogador = new Jogador(UUID.randomUUID(), request.nome(), request.posicao());
        return JogadorResponse.from(jogadorRepository.save(jogador));
    }

    @GetMapping
    public List<JogadorResponse> listar(@PathVariable String slug, Authentication authentication) {
        if (!ligaAccessPolicy.canAccess(slug, authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }
        return jogadorRepository.findAll().stream().map(JogadorResponse::from).toList();
    }

    @GetMapping("/{id}")
    public JogadorResponse buscar(@PathVariable String slug, @PathVariable UUID id, Authentication authentication) {
        if (!ligaAccessPolicy.canAccess(slug, authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }
        return jogadorRepository.findById(id)
                .map(JogadorResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador nao encontrado"));
    }
}
