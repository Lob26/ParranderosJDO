--- Sentencias SQL para la creación del esquema de parranderos
--- Las tablas tienen prefijo A_ para facilitar su acceso desde SQL Developer

-- USO
-- Copie el contenido deseado de este archivo en una pestaña SQL de SQL Developer
-- Ejecútelo como un script - Utilice el botón correspondiente de la pestaña utilizada

-- Eliminar todas las tablas de la base de datos
DROP TABLE "A_BAR" CASCADE CONSTRAINTS;
DROP TABLE "A_BEBIDA" CASCADE CONSTRAINTS;
DROP TABLE "A_BEBEDOR" CASCADE CONSTRAINTS;
DROP TABLE "A_TIPOBEBIDA" CASCADE CONSTRAINTS;
DROP TABLE "A_SIRVEN" CASCADE CONSTRAINTS;
DROP TABLE "A_GUSTAN" CASCADE CONSTRAINTS;
DROP TABLE "A_VISITAN" CASCADE CONSTRAINTS;
COMMIT;

-- Eliminar el contenido de todas las tablas de la base de datos
-- El orden es importante. Por qué?
-- TRUNCATE TABLE hace un borrado rápido del contenido de la tabla, similar a DELETE FROM sin WHERE
TRUNCATE TABLE a_gustan;
TRUNCATE TABLE a_sirven;
TRUNCATE TABLE a_visitan;
TRUNCATE TABLE a_bebida;
TRUNCATE TABLE a_tipobebida;
TRUNCATE TABLE a_bebedor;
TRUNCATE TABLE a_bar;
commit;
