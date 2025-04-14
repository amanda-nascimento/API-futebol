CREATE TABLE clube (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       nome VARCHAR(50) NOT NULL,
                       estado CHAR(2) NOT NULL,
                       fundacao DATE NOT NULL,
                       ativo BOOLEAN
);

CREATE TABLE estadio (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         nome VARCHAR(255) NOT NULL
);


CREATE TABLE partida (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         data_hora_partida TIMESTAMP NOT NULL,
                         id_estadio BIGINT NOT NULL,
                         id_time_casa BIGINT NOT NULL,
                         id_time_visitante BIGINT NOT NULL,
                         saldo_gol_time_casa INT NOT NULL,
                         saldo_gol_time_visitante INT NOT NULL,
                         resultado VARCHAR(15) NOT NULL,

                         CONSTRAINT fk_partida_estadio FOREIGN KEY (id_estadio) REFERENCES estadio(id),
                         CONSTRAINT fk_partida_time_casa FOREIGN KEY (id_time_casa) REFERENCES clube(id),
                         CONSTRAINT fk_partida_time_visitante FOREIGN KEY (id_time_visitante) REFERENCES clube(id)
);

CREATE TABLE retrospecto (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             id_clube BIGINT NOT NULL,
                             vitorias INT DEFAULT 0 NOT NULL,
                             empates INT DEFAULT 0 NOT NULL,
                             derrotas INT DEFAULT 0 NOT NULL,
                             gols_feitos INT DEFAULT 0 NOT NULL,
                             gols_sofridos INT DEFAULT 0 NOT NULL,
                             CONSTRAINT fk_retrospecto_clube FOREIGN KEY (id_clube) REFERENCES clube(id)
);


