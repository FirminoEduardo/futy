# Futy

Futy is a Spring Boot backend for managing football leagues, teams, players, seasons, matches, standings, and match events.

## Architecture

- Layered architecture with Spring Boot
- Multi-tenancy by league slug
- JWT-based authentication and authorization
- JPA + PostgreSQL + Flyway
- Domain-driven structure with clear separation between domain, web, identity and tenancy concerns

## Main modules

- league
  - liga
  - time
  - jogador
- identity
  - usuario
  - membership
  - papel
  - JWT and access policy
- tenancy
  - slug-based tenant resolution
- competition
  - temporada
  - partida
  - classificacao
  - evento_partida

## Features implemented

### Fase 1
- league creation by slug
- team and player CRUD under /api/ligas/{slug}/...
- tenant isolation by league
- Flyway migrations for base schema

### Fase 2
- JWT auth flow
- user registration and login
- league membership and roles
- request validation by slug and membership

### Fase 3
- seasons
- match calendar generation
- standings update
- season closure

### Fase 4
- match events/súmula
- events like goal, yellow card, red card, substitution, etc.
- summary endpoint for season overview

## Base endpoints

### Auth
- POST /api/auth/register
- POST /api/auth/login

### League
- POST /api/ligas
- GET /api/ligas/{slug}/times
- POST /api/ligas/{slug}/times
- GET /api/ligas/{slug}/jogadores
- POST /api/ligas/{slug}/jogadores

### Competition
- POST /api/ligas/{slug}/temporadas
- GET /api/ligas/{slug}/temporadas
- POST /api/ligas/{slug}/temporadas/{temporadaId}/gerar-calendario
- GET /api/ligas/{slug}/temporadas/{temporadaId}/classificacao
- POST /api/ligas/{slug}/temporadas/{temporadaId}/partidas/{partidaId}/resultado
- POST /api/ligas/{slug}/temporadas/{temporadaId}/encerrar
- GET /api/ligas/{slug}/temporadas/{temporadaId}/resumo
- POST /api/ligas/{slug}/temporadas/{temporadaId}/partidas/{partidaId}/eventos
- GET /api/ligas/{slug}/temporadas/{temporadaId}/partidas/{partidaId}/eventos

## Local development

Requirements:
- Java 21+
- Docker Desktop or local PostgreSQL
- Maven

Run tests:

```bash
cd backend
./mvnw test
```

Run app:

```bash
cd backend
./mvnw spring-boot:run
```

## Database

The project uses Flyway for schema versioning. Migrations live under:

- backend/src/main/resources/db/migration

## Notes

The backend is structured as a practical MVP foundation for a football league platform and is ready to evolve toward richer competition features, admin tooling, and external integrations.

