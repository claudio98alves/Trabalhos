CREATE TABLE utilizadores (
	nome	 VARCHAR(50) NOT NULL,
	password varchar(50) NOT NULL,
	permissao	 BOOL NOT NULL DEFAULT false,
	PRIMARY KEY(nome),
	CONSTRAINT nome_tamanho CHECK ((char_length(nome))>0),
	CONSTRAINT password_tamanho CHECK ((char_length(password))>0)
);

CREATE TABLE artistas (
	idartist BIGSERIAL,
	nome	 VARCHAR(50) NOT NULL,
	historia VARCHAR(300),
	PRIMARY KEY(idartist),
	CONSTRAINT nome_tamanho CHECK ((char_length(nome))>0)
);

CREATE TABLE concertos (
	lugar		 VARCHAR(50) NOT NULL,
	data		 DATE NOT NULL,
	artistas_idartist BIGINT,
	PRIMARY KEY(lugar,data,artistas_idartist),
	CONSTRAINT concertos_fk1 FOREIGN KEY (artistas_idartist) REFERENCES artistas(idartist),
	CONSTRAINT lugar_tamanho CHECK ((char_length(lugar))>0)
);

CREATE TABLE musicos (
	artistas_idartist BIGINT,
	PRIMARY KEY(artistas_idartist),
	CONSTRAINT musicos_fk1 FOREIGN KEY (artistas_idartist) REFERENCES artistas(idartist)
);

CREATE TABLE compositores (
	musicos_artistas_idartist BIGINT,
	PRIMARY KEY(musicos_artistas_idartist),
	CONSTRAINT compositores_fk1 FOREIGN KEY (musicos_artistas_idartist) REFERENCES musicos(artistas_idartist)
);

CREATE TABLE bandas (
	inicio		 DATE NOT NULL,
	fim		 DATE,
	artistas_idartist BIGINT,
	PRIMARY KEY(artistas_idartist),
	CONSTRAINT bandas_fk1 FOREIGN KEY (artistas_idartist) REFERENCES artistas(idartist),
	CONSTRAINT data_inicio_fim CHECK ((fim>inicio) or (fim is null)),
	CONSTRAINT data_inicio CHECK (inicio<=current_date),
	CONSTRAINT data_fim CHECK ((fim<=current_date) or (fim is null))
);

CREATE TABLE bandas_musicos (
	bandas_artistas_idartist	 BIGINT,
	musicos_artistas_idartist BIGINT,
	PRIMARY KEY(bandas_artistas_idartist,musicos_artistas_idartist),
	CONSTRAINT bandas_musicos_fk1 FOREIGN KEY (bandas_artistas_idartist) REFERENCES bandas(artistas_idartist),
	CONSTRAINT bandas_musicos_fk2 FOREIGN KEY (musicos_artistas_idartist) REFERENCES musicos(artistas_idartist)
);

CREATE TABLE musicas (
	idmusicas				 BIGSERIAL,
	letra					 VARCHAR(600) NOT NULL,
	nome					 VARCHAR(50) NOT NULL,
	duracao				 TIME NOT NULL,
	compositores_musicos_artistas_idartist BIGINT NOT NULL,
	PRIMARY KEY(idmusicas),
	CONSTRAINT musicas_fk1 FOREIGN KEY (compositores_musicos_artistas_idartist) REFERENCES compositores(musicos_artistas_idartist),
	CONSTRAINT letra_tamanho CHECK ((char_length(letra))>0),
	CONSTRAINT nome_tamanho CHECK ((char_length(nome))>0),
	CONSTRAINT duracao_tempo CHECK ((extract(hour from duracao)>0) or (extract(minute from duracao)>0) or (extract(second from duracao)>0))
);

CREATE TABLE editoras (
	nome VARCHAR(50),
	PRIMARY KEY(nome),
	CONSTRAINT nome_tamanho CHECK ((char_length(nome))>0)
);

CREATE TABLE albuns (
	idalbuns		 BIGSERIAL,
	nome		 VARCHAR(50) NOT NULL,
	genero		 VARCHAR(20) NOT NULL,
	datalancamento	 DATE NOT NULL,
	historia		 VARCHAR(300),
	editoras_nome	 VARCHAR(50) NOT NULL,
	artistas_idartist BIGINT NOT NULL,
	PRIMARY KEY(idalbuns),
	CONSTRAINT albuns_fk1 FOREIGN KEY (editoras_nome) REFERENCES editoras(nome),
	CONSTRAINT albuns_fk2 FOREIGN KEY (artistas_idartist) REFERENCES artistas(idartist),
	CONSTRAINT nome_tamanho CHECK ((char_length(nome))>0),
	CONSTRAINT data_lancamento CHECK (datalancamento<=current_date),
	CONSTRAINT genero_tamanho CHECK ((char_length(genero))>0)
);

CREATE TABLE albuns_musicas (
	albuns_idalbuns	 BIGINT,
	musicas_idmusicas BIGINT,
	PRIMARY KEY(albuns_idalbuns,musicas_idmusicas),
	CONSTRAINT albuns_musicas_fk1 FOREIGN KEY (albuns_idalbuns) REFERENCES albuns(idalbuns),
	CONSTRAINT albuns_musicas_fk2 FOREIGN KEY (musicas_idmusicas) REFERENCES musicas(idmusicas)
);

CREATE TABLE criticas (
	idcritica		 BIGSERIAL,
	rate			 DOUBLE PRECISION NOT NULL,
	texto			 VARCHAR(300) NOT NULL,
	utilizadores_nome VARCHAR(50) NOT NULL,
	albuns_idalbuns		 BIGINT NOT NULL,
	PRIMARY KEY(idcritica,albuns_idalbuns,utilizadores_nome),
	CONSTRAINT criticas_fk1 FOREIGN KEY (utilizadores_nome) REFERENCES utilizadores(nome),
	CONSTRAINT criticas_fk2 FOREIGN KEY (albuns_idalbuns) REFERENCES albuns(idalbuns),
	CONSTRAINT texto_tamanho CHECK (char_length(texto) > 0),
	CONSTRAINT rate_entre CHECK ((rate>=0.0) and (rate<=10.0))
);

CREATE TABLE playlists (
	nome			 VARCHAR(50) NOT NULL,
	privada			 BOOL NOT NULL DEFAULT false,
	utilizadores_nome VARCHAR(50),
	PRIMARY KEY(nome,utilizadores_nome),
	CONSTRAINT playlists_fk1 FOREIGN KEY (utilizadores_nome) REFERENCES utilizadores(nome),
	CONSTRAINT nome_tamanho CHECK ((char_length(nome))>0)
);

CREATE TABLE playlists_musicas (
	playlists_nome			 VARCHAR(50),
	playlists_utilizadores_nome VARCHAR(50),
	musicas_idmusicas			 BIGINT,
	PRIMARY KEY(playlists_nome,playlists_utilizadores_nome,musicas_idmusicas),
	CONSTRAINT playlists_musicas_fk1 FOREIGN KEY (playlists_nome,playlists_utilizadores_nome) REFERENCES playlists(nome,utilizadores_nome) on update cascade,
	CONSTRAINT playlists_musicas_fk2 FOREIGN KEY (musicas_idmusicas) REFERENCES musicas(idmusicas)
);
