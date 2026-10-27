package com.futy.league.domain;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LigaRepository extends JpaRepository<Liga, UUID> {

	Optional<Liga> findBySlug(String slug);

	boolean existsBySlug(String slug);
}
