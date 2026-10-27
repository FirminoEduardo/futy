package com.futy.identity.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, UUID> {

    List<Membership> findByUsuarioId(UUID usuarioId);

    Optional<Membership> findByLigaIdAndUsuarioId(UUID ligaId, UUID usuarioId);
}

