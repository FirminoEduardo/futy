package com.futy.league.web;

import java.util.List;
import java.util.UUID;

import com.futy.identity.security.LigaAccessPolicy;
import com.futy.league.domain.Time;
import com.futy.league.domain.TimeRepository;
import com.futy.league.web.dto.CriarTimeRequest;
import com.futy.league.web.dto.TimeResponse;
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
@RequestMapping("/api/ligas/{slug}/times")
public class TimeController {

    private final TimeRepository timeRepository;
    private final LigaAccessPolicy ligaAccessPolicy;

    public TimeController(TimeRepository timeRepository, LigaAccessPolicy ligaAccessPolicy) {
        this.timeRepository = timeRepository;
        this.ligaAccessPolicy = ligaAccessPolicy;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TimeResponse criar(@PathVariable String slug, @Valid @RequestBody CriarTimeRequest request, Authentication authentication) {
        if (!ligaAccessPolicy.canAccess(slug, authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }
        Time time = new Time(UUID.randomUUID(), request.nome());
        return TimeResponse.from(timeRepository.save(time));
    }

    @GetMapping
    public List<TimeResponse> listar(@PathVariable String slug, Authentication authentication) {
        if (!ligaAccessPolicy.canAccess(slug, authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }
        return timeRepository.findAll().stream().map(TimeResponse::from).toList();
    }

    @GetMapping("/{id}")
    public TimeResponse buscar(@PathVariable String slug, @PathVariable UUID id, Authentication authentication) {
        if (!ligaAccessPolicy.canAccess(slug, authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para a liga " + slug);
        }
        return timeRepository.findById(id)
                .map(TimeResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Time nao encontrado"));
    }
}
