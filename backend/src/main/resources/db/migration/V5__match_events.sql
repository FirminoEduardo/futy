CREATE TABLE evento_partida (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES liga (id),
    partida_id UUID NOT NULL REFERENCES partida (id),
    time_id UUID NOT NULL,
    jogador_id UUID,
    minuto INT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    descricao VARCHAR(255),
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_evento_partida_tenant ON evento_partida (tenant_id);
CREATE INDEX idx_evento_partida_partida ON evento_partida (partida_id);

