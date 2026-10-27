CREATE TABLE liga (
    id          UUID PRIMARY KEY,
    slug        VARCHAR(60)  NOT NULL UNIQUE,
    nome        VARCHAR(120) NOT NULL,
    criada_em   TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE time (
    id          UUID PRIMARY KEY,
    tenant_id   UUID         NOT NULL REFERENCES liga (id),
    nome        VARCHAR(100) NOT NULL,
    criado_em   TIMESTAMPTZ  NOT NULL DEFAULT now(),
    UNIQUE (tenant_id, nome)
);

CREATE TABLE jogador (
    id          UUID PRIMARY KEY,
    tenant_id   UUID         NOT NULL REFERENCES liga (id),
    nome        VARCHAR(120) NOT NULL,
    posicao     VARCHAR(30),
    criado_em   TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_time_tenant    ON time (tenant_id);
CREATE INDEX idx_jogador_tenant ON jogador (tenant_id);
