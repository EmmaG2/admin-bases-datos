CREATE TABLE IF NOT EXISTS alumnos (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  control CHAR(8) NOT NULL UNIQUE,
  epecialidad VARCHAR(80) NOT NULL,
  fecha_ingreso DATE NOT NULL,
  CONSTRAINT chk_alumnos_control CHECK (REGEXP_LIKE(control, '^2304[0-9]{4}$'))
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS materias (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(120) NOT NULL,
  creditos TINYINT UNSIGNED NOT NULL,
  especialidad VARCHAR(80) NOT NULL,
  CONSTRAINT uq_materias_nombre_especialidad UNIQUE (nombre, especialidad),
  CONSTRAINT chk_materias_creditos CHECK (creditos > 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS calificaciones (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  id_alumno INT NOT NULL,
  id_materia INT NOT NULL,
  calificacion SMALLINT NOT NULL,
  periodo SMALLINT NOT NULL,
  CONSTRAINT uq_calificaciones_alumno_materia_periodo UNIQUE (id_alumno, id_materia, periodo),
  CONSTRAINT fk_calificaciones_alumno_cascade FOREIGN KEY (id_alumno) REFERENCES alumnos(id) ON DELETE CASCADE,
  CONSTRAINT fk_calificaciones_materia FOREIGN KEY (id_materia) REFERENCES materias(id),
  CONSTRAINT chk_calificaciones_rango CHECK (calificacion BETWEEN 0 AND 100),
  CONSTRAINT chk_calificaciones_periodo CHECK (periodo > 0)
) ENGINE=InnoDB;

INSERT INTO alumnos (name, control, epecialidad, fecha_ingreso) VALUES
  ('Ana Lopez', '23049000', 'Sistemas', '2023-08-21'),
  ('Luis Garcia', '23049001', 'Industrial', '2023-08-21'),
  ('Sofia Ramirez', '23049002', 'Sistemas', '2023-08-21'),
  ('Diego Torres', '23049003', 'Industrial', '2023-08-21'),
  ('Valeria Cruz', '23049004', 'Sistemas', '2023-08-21'),
  ('Mateo Ruiz', '23049005', 'Industrial', '2023-08-21')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO materias (nombre, creditos, especialidad) VALUES
  ('Bases de Datos', 5, 'Sistemas'),
  ('Programacion', 5, 'Sistemas'),
  ('Matematicas', 5, 'Sistemas'),
  ('Bases de Datos', 5, 'Industrial'),
  ('Programacion', 5, 'Industrial'),
  ('Matematicas', 5, 'Industrial')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO calificaciones (id_alumno, id_materia, calificacion, periodo)
SELECT a.id, m.id, 70 + MOD(CAST(RIGHT(a.control, 2) AS UNSIGNED) * 7 + m.id * 3, 31), 1
FROM alumnos a
JOIN materias m ON m.especialidad = a.epecialidad
WHERE a.control IN ('23049000', '23049001', '23049002', '23049003', '23049004', '23049005')
ON DUPLICATE KEY UPDATE calificaciones.id = calificaciones.id;
