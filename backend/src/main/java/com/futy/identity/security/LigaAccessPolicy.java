package com.futy.identity.security;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.futy.identity.domain.MembershipRepository;
import com.futy.identity.domain.Papel;
import com.futy.league.domain.LigaRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class LigaAccessPolicy {

    private final LigaRepository ligaRepository;
    private final MembershipRepository membershipRepository;

    public LigaAccessPolicy(LigaRepository ligaRepository, MembershipRepository membershipRepository) {
        this.ligaRepository = ligaRepository;
        this.membershipRepository = membershipRepository;
    }

    public boolean canAccess(String slug, Authentication authentication) {
        if (authentication == null || "anonymousUser".equals(authentication.getName())) {
            return true;
        }

        if (!authentication.isAuthenticated()) {
            return false;
        }

        var liga = ligaRepository.findBySlug(slug);
        if (liga.isEmpty()) {
            return false;
        }

        Claims claims = (Claims) authentication.getDetails();
        if (claims != null) {
            String tenantClaim = claims.get("tenant", String.class);
            if (tenantClaim != null && !tenantClaim.equals(slug)) {
                return false;
            }
        }

        String userId = authentication.getName();
        boolean hasMembership = membershipRepository.findByUsuarioId(UUID.fromString(userId)).stream()
                .anyMatch(m -> m.getLigaId().equals(liga.get().getId()));

        if (hasMembership) {
            return true;
        }

        var roles = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .toList();

        return roles.contains("ROLE_ORGANIZADOR") || roles.contains("ROLE_MESARIO");
    }

    public boolean hasAnyRole(String slug, Authentication authentication, Papel... roles) {
        if (authentication == null || "anonymousUser".equals(authentication.getName())) {
            return true;
        }

        if (!canAccess(slug, authentication)) {
            return false;
        }

        List<String> allowed = Arrays.stream(roles)
                .map(Enum::name)
                .map(role -> "ROLE_" + role)
                .toList();

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> allowed.contains(authority.getAuthority()));
    }
}
