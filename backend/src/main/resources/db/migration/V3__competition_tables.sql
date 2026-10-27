CREATE TABLE temporada (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES liga (id),
    nome VARCHAR(120) NOT NULL,
    status VARCHAR(30) NOT NULL,
    criada_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE partida (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES liga (id),
    temporada_id UUID NOT NULL REFERENCES temporada (id),
    time_casa_id UUID NOT NULL,
    time_visitante_id UUID NOT NULL,
    rodada INT NOT NULL,
    gols_casa INT NOT NULL DEFAULT 0,
    gols_visitante INT NOT NULL DEFAULT 0,
    status VARCHAR(30) NOT NULL,
    criada_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_temporada_tenant ON temporada (tenant_id);
CREATE INDEX idx_partida_tenant ON partida (tenant_id);
CREATE INDEX idx_partida_temporada ON partida (temporada_id);
