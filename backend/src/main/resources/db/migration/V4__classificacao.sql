CREATE TABLE classificacao (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES liga (id),
    temporada_id UUID NOT NULL REFERENCES temporada (id),
    time_id UUID NOT NULL,
    pontos INT NOT NULL,
    jogos INT NOT NULL,
    vitorias INT NOT NULL,
    empates INT NOT NULL,
    derrotas INT NOT NULL,
    gols_feitos INT NOT NULL,
    gols_sofridos INT NOT NULL,
    saldo_gols INT NOT NULL,
    status VARCHAR(30) NOT NULL,
    UNIQUE (temporada_id, time_id)
);

CREATE INDEX idx_classificacao_tenant ON classificacao (tenant_id);
CREATE INDEX idx_classificacao_temporada ON classificacao (temporada_id);

