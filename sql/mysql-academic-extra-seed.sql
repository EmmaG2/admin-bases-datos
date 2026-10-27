-- Run after mysql-academic-schema.sql in the selected MySQL database.
-- Existing records are preserved when this script is run again.
START TRANSACTION;

INSERT INTO alumnos (name, control, epecialidad, fecha_ingreso) VALUES
  ('Camila Hernandez', '23049100', 'Sistemas', '2023-08-21'),
  ('Sebastian Mendoza', '23049101', 'Sistemas', '2023-08-21'),
  ('Mariana Flores', '23049102', 'Sistemas', '2023-08-21'),
  ('Leonardo Vargas', '23049103', 'Sistemas', '2023-08-21'),
  ('Daniela Castillo', '23049104', 'Sistemas', '2023-08-21'),
  ('Nicolas Romero', '23049105', 'Sistemas', '2024-01-15'),
  ('Renata Morales', '23049106', 'Sistemas', '2024-01-15'),
  ('Gabriel Ortiz', '23049107', 'Sistemas', '2024-01-15'),
  ('Regina Navarro', '23049108', 'Sistemas', '2024-08-19'),
  ('Emiliano Reyes', '23049109', 'Sistemas', '2024-08-19'),
  ('Fernanda Silva', '23049110', 'Industrial', '2023-08-21'),
  ('Santiago Rojas', '23049111', 'Industrial', '2023-08-21'),
  ('Ximena Aguilar', '23049112', 'Industrial', '2023-08-21'),
  ('Alejandro Medina', '23049113', 'Industrial', '2023-08-21'),
  ('Paula Delgado', '23049114', 'Industrial', '2023-08-21'),
  ('Andres Campos', '23049115', 'Industrial', '2024-01-15'),
  ('Victoria Salazar', '23049116', 'Industrial', '2024-01-15'),
  ('Rodrigo Pineda', '23049117', 'Industrial', '2024-01-15'),
  ('Lucia Cabrera', '23049118', 'Industrial', '2024-08-19'),
  ('Daniel Fuentes', '23049119', 'Industrial', '2024-08-19')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO materias (nombre, creditos, especialidad) VALUES
  ('Estructuras de Datos', 5, 'Sistemas'),
  ('Redes de Computadoras', 5, 'Sistemas'),
  ('Sistemas Operativos', 4, 'Sistemas'),
  ('Ingenieria de Software', 6, 'Sistemas'),
  ('Estadistica Aplicada', 5, 'Industrial'),
  ('Investigacion de Operaciones', 6, 'Industrial'),
  ('Control de Calidad', 4, 'Industrial'),
  ('Procesos de Manufactura', 5, 'Industrial')
ON DUPLICATE KEY UPDATE id = id;

-- Each new student receives four subject grades in each of two periods.
INSERT INTO calificaciones (id_alumno, id_materia, calificacion, periodo)
SELECT a.id, m.id,
       45 + MOD(CAST(RIGHT(a.control, 2) AS UNSIGNED) * 7 + m.id * 3 + p.periodo * 11, 56),
       p.periodo
FROM alumnos a
JOIN materias m ON m.especialidad = a.epecialidad
CROSS JOIN (SELECT 1 AS periodo UNION ALL SELECT 2) p
WHERE a.control BETWEEN '23049100' AND '23049119'
  AND m.nombre IN (
    'Estructuras de Datos', 'Redes de Computadoras',
    'Sistemas Operativos', 'Ingenieria de Software',
    'Estadistica Aplicada', 'Investigacion de Operaciones',
    'Control de Calidad', 'Procesos de Manufactura'
  )
ON DUPLICATE KEY UPDATE calificaciones.id = calificaciones.id;

COMMIT;
