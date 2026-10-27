package com.futy.identity.security;

import java.util.Date;
import java.util.List;

import com.futy.identity.domain.Papel;
import com.futy.identity.domain.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey key;

    public JwtService(@Value("${app.jwt.secret:change-me-please-change-me-please-change-me-123}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Usuario usuario, List<Papel> papeis) {
        return generateToken(usuario, papeis, null);
    }

    public String generateToken(Usuario usuario, List<Papel> papeis, String tenantSlug) {
        return Jwts.builder()
                .subject(usuario.getId().toString())
                .claim("email", usuario.getEmail())
                .claim("nome", usuario.getNome())
                .claim("roles", papeis.stream().map(Enum::name).toList())
                .claim("tenant", tenantSlug)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 8))
                .signWith(key)
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
