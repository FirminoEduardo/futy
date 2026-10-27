package com.futy.identity.web;

import java.util.List;
import java.util.UUID;

import com.futy.identity.domain.Membership;
import com.futy.identity.domain.MembershipRepository;
import com.futy.identity.domain.Papel;
import com.futy.identity.domain.Usuario;
import com.futy.identity.domain.UsuarioRepository;
import com.futy.identity.security.JwtService;
import com.futy.league.domain.Liga;
import com.futy.league.domain.LigaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final MembershipRepository membershipRepository;
    private final LigaRepository ligaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository usuarioRepository,
                         MembershipRepository membershipRepository,
                         LigaRepository ligaRepository,
                         PasswordEncoder passwordEncoder,
                         JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.membershipRepository = membershipRepository;
        this.ligaRepository = ligaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas"));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas");
        }

        List<Papel> papeis = membershipRepository.findByUsuarioId(usuario.getId()).stream()
                .map(Membership::getPapel)
                .distinct()
                .toList();

        String tenantSlug = membershipRepository.findByUsuarioId(usuario.getId()).stream()
                .findFirst()
                .map(Membership::getLigaId)
                .flatMap(ligaId -> ligaRepository.findById(ligaId))
                .map(Liga::getSlug)
                .orElse(null);

        if (papeis.isEmpty()) {
            papeis = List.of(Papel.OBSERVADOR);
        }

        String token = jwtService.generateToken(usuario, papeis, tenantSlug);
        return AuthResponse.from(token, usuario.getEmail(), usuario.getNome());
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja cadastrado");
        }

        Usuario usuario = new Usuario(UUID.randomUUID(), request.email(), passwordEncoder.encode(request.senha()), request.nome());
        usuario = usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario, List.of(Papel.ORGANIZADOR), null);
        return AuthResponse.from(token, usuario.getEmail(), usuario.getNome());
    }
}
