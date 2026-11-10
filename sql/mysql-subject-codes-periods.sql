-- Run once against the existing academic database, with application writes stopped.
-- Back up alumnos, materias and calificaciones first: ALTER TABLE commits implicitly.
-- Existing periods must be 1 (Ene/Ago) or 2 (Ago/Dic), and subject IDs 1..99999.
-- A repeated run stops at the existing clave column without changing any data.
ALTER TABLE materias
  ADD COLUMN clave VARCHAR(10) CHARACTER SET ascii COLLATE ascii_bin NULL AFTER nombre;

START TRANSACTION;
UPDATE materias
SET clave = CONCAT(
  CASE nombre
    WHEN 'Bases de Datos' THEN 'BADA'
    WHEN 'Programacion' THEN 'PROG'
    WHEN 'Matematicas' THEN 'MATE'
    WHEN 'Estructuras de Datos' THEN 'ESDA'
    WHEN 'Redes de Computadoras' THEN 'RECO'
    WHEN 'Sistemas Operativos' THEN 'SIOP'
    WHEN 'Ingenieria de Software' THEN 'INSO'
    WHEN 'Estadistica Aplicada' THEN 'ESAP'
    WHEN 'Investigacion de Operaciones' THEN 'INOP'
    WHEN 'Control de Calidad' THEN 'COCA'
    WHEN 'Procesos de Manufactura' THEN 'PRMA'
    ELSE 'ASIG'
  END, '-', LPAD(id, 5, '0'));
COMMIT;

ALTER TABLE materias
  MODIFY clave VARCHAR(10) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  ADD CONSTRAINT uq_materias_clave UNIQUE (clave),
  ADD CONSTRAINT chk_materias_clave CHECK (
    REGEXP_LIKE(clave, '^[A-Z]{4}-[0-9]{5}$', 'c') AND RIGHT(clave, 5) <> '00000');

ALTER TABLE calificaciones
  DROP CHECK chk_calificaciones_periodo,
  MODIFY periodo VARCHAR(7) CHARACTER SET ascii COLLATE ascii_bin NOT NULL;

START TRANSACTION;
UPDATE calificaciones
SET periodo = CASE periodo WHEN '1' THEN 'Ene/Ago' WHEN '2' THEN 'Ago/Dic' ELSE periodo END;
COMMIT;

ALTER TABLE calificaciones
  ADD CONSTRAINT chk_calificaciones_periodo CHECK (periodo IN ('Ago/Dic', 'Ene/Ago'));
