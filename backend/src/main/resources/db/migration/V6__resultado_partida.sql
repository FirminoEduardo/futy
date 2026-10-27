CREATE TABLE resultado_partida (
    id UUID PRIMARY KEY,
    partida_id UUID NOT NULL UNIQUE REFERENCES partida (id),
    time_casa_id UUID NOT NULL,
    time_visitante_id UUID NOT NULL,
    gols_casa INT NOT NULL,
    gols_visitante INT NOT NULL
);

CREATE INDEX idx_resultado_partida_partida ON resultado_partida (partida_id);

