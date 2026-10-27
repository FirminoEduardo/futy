CREATE TABLE usuario (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    nome VARCHAR(120) NOT NULL,
    criado_em TIMESTAMP NOT NULL
);

CREATE TABLE liga_membro (
    id UUID PRIMARY KEY,
    liga_id UUID NOT NULL,
    usuario_id UUID NOT NULL,
    papel VARCHAR(30) NOT NULL,
    criado_em TIMESTAMP NOT NULL,
    UNIQUE (liga_id, usuario_id)
);

ALTER TABLE liga_membro
    ADD CONSTRAINT fk_liga_membro_liga
    FOREIGN KEY (liga_id) REFERENCES liga(id);

ALTER TABLE liga_membro
    ADD CONSTRAINT fk_liga_membro_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuario(id);

