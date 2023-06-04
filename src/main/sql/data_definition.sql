--- Sentencias SQL para la creación del esquema de parranderos
--- Las tablas tienen prefijo A_ para facilitar su acceso desde SQL Developer

-- USO
-- Copie el contenido de este archivo en una pestaña SQL de SQL Developer
-- Ejecútelo como un script - Utilice el botón correspondiente de la pestaña utilizada

-- Creación del secuenciador
CREATE SEQUENCE parranderos_sequence;

-- Creaación de la tabla TIPOBEBIDA y especificación de sus restricciones
CREATE TABLE a_tipobebida
(
    id     NUMBER,
    nombre VARCHAR2(255 BYTE),
    CONSTRAINT a_tipobebida_pk PRIMARY KEY (id)
);

ALTER TABLE a_tipobebida
    ADD CONSTRAINT un_tipobeb_nombre
        UNIQUE (nombre)
            ENABLE;

-- Creaación de la tabla BEBIDA y especificación de sus restricciones
CREATE TABLE a_bebida
(
    id           NUMBER,
    nombre       VARCHAR2(20 BYTE),
    idtipobebida NUMBER,
    gradoalcohol NUMBER,
    CONSTRAINT a_bebida_pk PRIMARY KEY (id)
);

ALTER TABLE a_bebida
    ADD CONSTRAINT fk_a_tipobebida
        FOREIGN KEY (idtipobebida)
            REFERENCES a_tipobebida (id)
                ENABLE;

-- Creaación de la tabla BAR y especificación de sus restricciones
CREATE TABLE a_bar
(
    id          NUMBER,
    cantsedes   NUMBER(3, 0),
    ciudad      VARCHAR2(255 BYTE),
    nombre      VARCHAR2(255 BYTE),
    presupuesto VARCHAR2(255 BYTE),
    CONSTRAINT a_bar_pk PRIMARY KEY (id)
);

ALTER TABLE a_bar
    ADD CONSTRAINT ck_bar_ppto
        CHECK (presupuesto IN ('Alto', 'Medio', 'Bajo'))
            ENABLE;

-- Creaación de la tabla BEBEDOR y especificación de sus restricciones
CREATE TABLE a_bebedor
(
    id          NUMBER,
    ciudad      VARCHAR2(255 BYTE),
    nombre      VARCHAR2(255 BYTE),
    presupuesto VARCHAR2(255 BYTE),
    CONSTRAINT a_bebedor_pk PRIMARY KEY (id)
);

ALTER TABLE a_bebedor
    ADD CONSTRAINT ck_bdor_ppto
        CHECK (presupuesto IN ('Alto', 'Medio', 'Bajo'))
            ENABLE;

-- Creaación de la tabla GUSTAN y especificación de sus restricciones
CREATE TABLE a_gustan
(
    idbebedor NUMBER,
    idbebida  NUMBER,
    CONSTRAINT a_gustan_pk PRIMARY KEY (idbebedor, idbebida)
);

ALTER TABLE a_gustan
    ADD CONSTRAINT fk_g_bebedor
        FOREIGN KEY (idbebedor)
            REFERENCES a_bebedor (id)
                ENABLE;

ALTER TABLE a_gustan
    ADD CONSTRAINT fk_g_bebida
        FOREIGN KEY (idbebida)
            REFERENCES a_bebida (id)
                ENABLE;

-- Creaación de la tabla SIRVEN y especificación de sus restricciones
CREATE TABLE a_sirven
(
    idbar    NUMBER,
    idbebida NUMBER,
    horario  VARCHAR2(20 BYTE),
    CONSTRAINT a_sirven_pk PRIMARY KEY (idbar, idbebida, horario)
);

ALTER TABLE a_sirven
    ADD CONSTRAINT fk_s_bar
        FOREIGN KEY (idbar)
            REFERENCES a_bar (id)
                ENABLE;

ALTER TABLE a_sirven
    ADD CONSTRAINT fk_s_bebida
        FOREIGN KEY (idbebida)
            REFERENCES a_bebida (id)
                ENABLE;

ALTER TABLE a_sirven
    ADD CONSTRAINT ck_s_horario
        CHECK (horario IN ('diurno', 'nocturno', 'todos'))
            ENABLE;

-- Creaación de la tabla VISITAN y especificación de sus restricciones
CREATE TABLE a_visitan
(
    idbar       NUMBER,
    idbebedor   NUMBER,
    horario     VARCHAR2(20 BYTE),
    fechavisita DATE,
    CONSTRAINT a_visitan_pk PRIMARY KEY (idbar, idbebedor, fechavisita, horario)
);

ALTER TABLE a_visitan
    ADD CONSTRAINT fk_v_bebedor
        FOREIGN KEY (idbebedor)
            REFERENCES a_bebedor (id)
                ENABLE;

ALTER TABLE a_visitan
    ADD CONSTRAINT fk_v_bar
        FOREIGN KEY (idbar)
            REFERENCES a_bar (id)
                ENABLE;

ALTER TABLE a_visitan
    ADD CONSTRAINT ck_v_horario
        CHECK (horario IN ('diurno', 'nocturno', 'todos'))
            ENABLE;

COMMIT;
