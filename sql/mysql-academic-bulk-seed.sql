-- Add 150 fictional students, 30 subjects and 1800 grades without replacing existing rows.
-- Run against the selected academic database; repeating the script leaves matching rows unchanged.
-- Reserved controls: 23042001..23042150. Keys: SIST-10001..10015 and INDU-10001..10015.
-- Back up the three academic tables first. Any conflicting reserved data aborts the transaction.
-- Use the mysql client without --force so an error closes the connection and rolls back.
START TRANSACTION;

CREATE TEMPORARY TABLE bulk_alumnos (
  seq INT PRIMARY KEY, name VARCHAR(120), control CHAR(8) UNIQUE,
  epecialidad VARCHAR(80), fecha_ingreso DATE
);
INSERT INTO bulk_alumnos VALUES
  (1, 'Adriana Alvarez', '23042001', 'Sistemas', '2023-08-21'),
  (2, 'Adriana Benitez', '23042002', 'Sistemas', '2024-01-15'),
  (3, 'Adriana Cervantes', '23042003', 'Sistemas', '2024-08-19'),
  (4, 'Adriana Dominguez', '23042004', 'Sistemas', '2025-01-20'),
  (5, 'Adriana Escobar', '23042005', 'Sistemas', '2025-08-18'),
  (6, 'Adriana Fernandez', '23042006', 'Sistemas', '2023-08-21'),
  (7, 'Adriana Gutierrez', '23042007', 'Sistemas', '2024-01-15'),
  (8, 'Adriana Herrera', '23042008', 'Sistemas', '2024-08-19'),
  (9, 'Adriana Ibarra', '23042009', 'Sistemas', '2025-01-20'),
  (10, 'Adriana Jimenez', '23042010', 'Sistemas', '2025-08-18'),
  (11, 'Bruno Alvarez', '23042011', 'Sistemas', '2023-08-21'),
  (12, 'Bruno Benitez', '23042012', 'Sistemas', '2024-01-15'),
  (13, 'Bruno Cervantes', '23042013', 'Sistemas', '2024-08-19'),
  (14, 'Bruno Dominguez', '23042014', 'Sistemas', '2025-01-20'),
  (15, 'Bruno Escobar', '23042015', 'Sistemas', '2025-08-18'),
  (16, 'Bruno Fernandez', '23042016', 'Sistemas', '2023-08-21'),
  (17, 'Bruno Gutierrez', '23042017', 'Sistemas', '2024-01-15'),
  (18, 'Bruno Herrera', '23042018', 'Sistemas', '2024-08-19'),
  (19, 'Bruno Ibarra', '23042019', 'Sistemas', '2025-01-20'),
  (20, 'Bruno Jimenez', '23042020', 'Sistemas', '2025-08-18'),
  (21, 'Carolina Alvarez', '23042021', 'Sistemas', '2023-08-21'),
  (22, 'Carolina Benitez', '23042022', 'Sistemas', '2024-01-15'),
  (23, 'Carolina Cervantes', '23042023', 'Sistemas', '2024-08-19'),
  (24, 'Carolina Dominguez', '23042024', 'Sistemas', '2025-01-20'),
  (25, 'Carolina Escobar', '23042025', 'Sistemas', '2025-08-18'),
  (26, 'Carolina Fernandez', '23042026', 'Sistemas', '2023-08-21'),
  (27, 'Carolina Gutierrez', '23042027', 'Sistemas', '2024-01-15'),
  (28, 'Carolina Herrera', '23042028', 'Sistemas', '2024-08-19'),
  (29, 'Carolina Ibarra', '23042029', 'Sistemas', '2025-01-20'),
  (30, 'Carolina Jimenez', '23042030', 'Sistemas', '2025-08-18'),
  (31, 'Diego Alvarez', '23042031', 'Sistemas', '2023-08-21'),
  (32, 'Diego Benitez', '23042032', 'Sistemas', '2024-01-15'),
  (33, 'Diego Cervantes', '23042033', 'Sistemas', '2024-08-19'),
  (34, 'Diego Dominguez', '23042034', 'Sistemas', '2025-01-20'),
  (35, 'Diego Escobar', '23042035', 'Sistemas', '2025-08-18'),
  (36, 'Diego Fernandez', '23042036', 'Sistemas', '2023-08-21'),
  (37, 'Diego Gutierrez', '23042037', 'Sistemas', '2024-01-15'),
  (38, 'Diego Herrera', '23042038', 'Sistemas', '2024-08-19'),
  (39, 'Diego Ibarra', '23042039', 'Sistemas', '2025-01-20'),
  (40, 'Diego Jimenez', '23042040', 'Sistemas', '2025-08-18'),
  (41, 'Elena Alvarez', '23042041', 'Sistemas', '2023-08-21'),
  (42, 'Elena Benitez', '23042042', 'Sistemas', '2024-01-15'),
  (43, 'Elena Cervantes', '23042043', 'Sistemas', '2024-08-19'),
  (44, 'Elena Dominguez', '23042044', 'Sistemas', '2025-01-20'),
  (45, 'Elena Escobar', '23042045', 'Sistemas', '2025-08-18'),
  (46, 'Elena Fernandez', '23042046', 'Sistemas', '2023-08-21'),
  (47, 'Elena Gutierrez', '23042047', 'Sistemas', '2024-01-15'),
  (48, 'Elena Herrera', '23042048', 'Sistemas', '2024-08-19'),
  (49, 'Elena Ibarra', '23042049', 'Sistemas', '2025-01-20'),
  (50, 'Elena Jimenez', '23042050', 'Sistemas', '2025-08-18'),
  (51, 'Felipe Alvarez', '23042051', 'Sistemas', '2023-08-21'),
  (52, 'Felipe Benitez', '23042052', 'Sistemas', '2024-01-15'),
  (53, 'Felipe Cervantes', '23042053', 'Sistemas', '2024-08-19'),
  (54, 'Felipe Dominguez', '23042054', 'Sistemas', '2025-01-20'),
  (55, 'Felipe Escobar', '23042055', 'Sistemas', '2025-08-18'),
  (56, 'Felipe Fernandez', '23042056', 'Sistemas', '2023-08-21'),
  (57, 'Felipe Gutierrez', '23042057', 'Sistemas', '2024-01-15'),
  (58, 'Felipe Herrera', '23042058', 'Sistemas', '2024-08-19'),
  (59, 'Felipe Ibarra', '23042059', 'Sistemas', '2025-01-20'),
  (60, 'Felipe Jimenez', '23042060', 'Sistemas', '2025-08-18'),
  (61, 'Gabriela Alvarez', '23042061', 'Sistemas', '2023-08-21'),
  (62, 'Gabriela Benitez', '23042062', 'Sistemas', '2024-01-15'),
  (63, 'Gabriela Cervantes', '23042063', 'Sistemas', '2024-08-19'),
  (64, 'Gabriela Dominguez', '23042064', 'Sistemas', '2025-01-20'),
  (65, 'Gabriela Escobar', '23042065', 'Sistemas', '2025-08-18'),
  (66, 'Gabriela Fernandez', '23042066', 'Sistemas', '2023-08-21'),
  (67, 'Gabriela Gutierrez', '23042067', 'Sistemas', '2024-01-15'),
  (68, 'Gabriela Herrera', '23042068', 'Sistemas', '2024-08-19'),
  (69, 'Gabriela Ibarra', '23042069', 'Sistemas', '2025-01-20'),
  (70, 'Gabriela Jimenez', '23042070', 'Sistemas', '2025-08-18'),
  (71, 'Hugo Alvarez', '23042071', 'Sistemas', '2023-08-21'),
  (72, 'Hugo Benitez', '23042072', 'Sistemas', '2024-01-15'),
  (73, 'Hugo Cervantes', '23042073', 'Sistemas', '2024-08-19'),
  (74, 'Hugo Dominguez', '23042074', 'Sistemas', '2025-01-20'),
  (75, 'Hugo Escobar', '23042075', 'Sistemas', '2025-08-18'),
  (76, 'Hugo Fernandez', '23042076', 'Industrial', '2023-08-21'),
  (77, 'Hugo Gutierrez', '23042077', 'Industrial', '2024-01-15'),
  (78, 'Hugo Herrera', '23042078', 'Industrial', '2024-08-19'),
  (79, 'Hugo Ibarra', '23042079', 'Industrial', '2025-01-20'),
  (80, 'Hugo Jimenez', '23042080', 'Industrial', '2025-08-18'),
  (81, 'Isabel Alvarez', '23042081', 'Industrial', '2023-08-21'),
  (82, 'Isabel Benitez', '23042082', 'Industrial', '2024-01-15'),
  (83, 'Isabel Cervantes', '23042083', 'Industrial', '2024-08-19'),
  (84, 'Isabel Dominguez', '23042084', 'Industrial', '2025-01-20'),
  (85, 'Isabel Escobar', '23042085', 'Industrial', '2025-08-18'),
  (86, 'Isabel Fernandez', '23042086', 'Industrial', '2023-08-21'),
  (87, 'Isabel Gutierrez', '23042087', 'Industrial', '2024-01-15'),
  (88, 'Isabel Herrera', '23042088', 'Industrial', '2024-08-19'),
  (89, 'Isabel Ibarra', '23042089', 'Industrial', '2025-01-20'),
  (90, 'Isabel Jimenez', '23042090', 'Industrial', '2025-08-18'),
  (91, 'Javier Alvarez', '23042091', 'Industrial', '2023-08-21'),
  (92, 'Javier Benitez', '23042092', 'Industrial', '2024-01-15'),
  (93, 'Javier Cervantes', '23042093', 'Industrial', '2024-08-19'),
  (94, 'Javier Dominguez', '23042094', 'Industrial', '2025-01-20'),
  (95, 'Javier Escobar', '23042095', 'Industrial', '2025-08-18'),
  (96, 'Javier Fernandez', '23042096', 'Industrial', '2023-08-21'),
  (97, 'Javier Gutierrez', '23042097', 'Industrial', '2024-01-15'),
  (98, 'Javier Herrera', '23042098', 'Industrial', '2024-08-19'),
  (99, 'Javier Ibarra', '23042099', 'Industrial', '2025-01-20'),
  (100, 'Javier Jimenez', '23042100', 'Industrial', '2025-08-18'),
  (101, 'Karla Alvarez', '23042101', 'Industrial', '2023-08-21'),
  (102, 'Karla Benitez', '23042102', 'Industrial', '2024-01-15'),
  (103, 'Karla Cervantes', '23042103', 'Industrial', '2024-08-19'),
  (104, 'Karla Dominguez', '23042104', 'Industrial', '2025-01-20'),
  (105, 'Karla Escobar', '23042105', 'Industrial', '2025-08-18'),
  (106, 'Karla Fernandez', '23042106', 'Industrial', '2023-08-21'),
  (107, 'Karla Gutierrez', '23042107', 'Industrial', '2024-01-15'),
  (108, 'Karla Herrera', '23042108', 'Industrial', '2024-08-19'),
  (109, 'Karla Ibarra', '23042109', 'Industrial', '2025-01-20'),
  (110, 'Karla Jimenez', '23042110', 'Industrial', '2025-08-18'),
  (111, 'Luis Alvarez', '23042111', 'Industrial', '2023-08-21'),
  (112, 'Luis Benitez', '23042112', 'Industrial', '2024-01-15'),
  (113, 'Luis Cervantes', '23042113', 'Industrial', '2024-08-19'),
  (114, 'Luis Dominguez', '23042114', 'Industrial', '2025-01-20'),
  (115, 'Luis Escobar', '23042115', 'Industrial', '2025-08-18'),
  (116, 'Luis Fernandez', '23042116', 'Industrial', '2023-08-21'),
  (117, 'Luis Gutierrez', '23042117', 'Industrial', '2024-01-15'),
  (118, 'Luis Herrera', '23042118', 'Industrial', '2024-08-19'),
  (119, 'Luis Ibarra', '23042119', 'Industrial', '2025-01-20'),
  (120, 'Luis Jimenez', '23042120', 'Industrial', '2025-08-18'),
  (121, 'Mariana Alvarez', '23042121', 'Industrial', '2023-08-21'),
  (122, 'Mariana Benitez', '23042122', 'Industrial', '2024-01-15'),
  (123, 'Mariana Cervantes', '23042123', 'Industrial', '2024-08-19'),
  (124, 'Mariana Dominguez', '23042124', 'Industrial', '2025-01-20'),
  (125, 'Mariana Escobar', '23042125', 'Industrial', '2025-08-18'),
  (126, 'Mariana Fernandez', '23042126', 'Industrial', '2023-08-21'),
  (127, 'Mariana Gutierrez', '23042127', 'Industrial', '2024-01-15'),
  (128, 'Mariana Herrera', '23042128', 'Industrial', '2024-08-19'),
  (129, 'Mariana Ibarra', '23042129', 'Industrial', '2025-01-20'),
  (130, 'Mariana Jimenez', '23042130', 'Industrial', '2025-08-18'),
  (131, 'Nicolas Alvarez', '23042131', 'Industrial', '2023-08-21'),
  (132, 'Nicolas Benitez', '23042132', 'Industrial', '2024-01-15'),
  (133, 'Nicolas Cervantes', '23042133', 'Industrial', '2024-08-19'),
  (134, 'Nicolas Dominguez', '23042134', 'Industrial', '2025-01-20'),
  (135, 'Nicolas Escobar', '23042135', 'Industrial', '2025-08-18'),
  (136, 'Nicolas Fernandez', '23042136', 'Industrial', '2023-08-21'),
  (137, 'Nicolas Gutierrez', '23042137', 'Industrial', '2024-01-15'),
  (138, 'Nicolas Herrera', '23042138', 'Industrial', '2024-08-19'),
  (139, 'Nicolas Ibarra', '23042139', 'Industrial', '2025-01-20'),
  (140, 'Nicolas Jimenez', '23042140', 'Industrial', '2025-08-18'),
  (141, 'Olivia Alvarez', '23042141', 'Industrial', '2023-08-21'),
  (142, 'Olivia Benitez', '23042142', 'Industrial', '2024-01-15'),
  (143, 'Olivia Cervantes', '23042143', 'Industrial', '2024-08-19'),
  (144, 'Olivia Dominguez', '23042144', 'Industrial', '2025-01-20'),
  (145, 'Olivia Escobar', '23042145', 'Industrial', '2025-08-18'),
  (146, 'Olivia Fernandez', '23042146', 'Industrial', '2023-08-21'),
  (147, 'Olivia Gutierrez', '23042147', 'Industrial', '2024-01-15'),
  (148, 'Olivia Herrera', '23042148', 'Industrial', '2024-08-19'),
  (149, 'Olivia Ibarra', '23042149', 'Industrial', '2025-01-20'),
  (150, 'Olivia Jimenez', '23042150', 'Industrial', '2025-08-18');

CREATE TEMPORARY TABLE bulk_materias (
  seq INT, nombre VARCHAR(120), clave VARCHAR(10) CHARACTER SET ascii COLLATE ascii_bin PRIMARY KEY,
  creditos TINYINT UNSIGNED, especialidad VARCHAR(80)
);
INSERT INTO bulk_materias VALUES
  (1, 'Inteligencia Artificial', 'SIST-10001', 4, 'Sistemas'),
  (2, 'Aprendizaje Automatico', 'SIST-10002', 5, 'Sistemas'),
  (3, 'Mineria de Datos', 'SIST-10003', 6, 'Sistemas'),
  (4, 'Computacion en la Nube', 'SIST-10004', 4, 'Sistemas'),
  (5, 'Seguridad Informatica', 'SIST-10005', 5, 'Sistemas'),
  (6, 'Desarrollo Web', 'SIST-10006', 6, 'Sistemas'),
  (7, 'Desarrollo Movil', 'SIST-10007', 4, 'Sistemas'),
  (8, 'Arquitectura de Computadoras', 'SIST-10008', 5, 'Sistemas'),
  (9, 'Compiladores', 'SIST-10009', 6, 'Sistemas'),
  (10, 'Teoria de la Computacion', 'SIST-10010', 4, 'Sistemas'),
  (11, 'Graficacion', 'SIST-10011', 5, 'Sistemas'),
  (12, 'Programacion Concurrente', 'SIST-10012', 6, 'Sistemas'),
  (13, 'Administracion de Redes', 'SIST-10013', 4, 'Sistemas'),
  (14, 'Bases de Datos Distribuidas', 'SIST-10014', 5, 'Sistemas'),
  (15, 'Internet de las Cosas', 'SIST-10015', 6, 'Sistemas'),
  (1, 'Ergonomia', 'INDU-10001', 4, 'Industrial'),
  (2, 'Logistica', 'INDU-10002', 5, 'Industrial'),
  (3, 'Cadena de Suministro', 'INDU-10003', 6, 'Industrial'),
  (4, 'Planeacion de la Produccion', 'INDU-10004', 4, 'Industrial'),
  (5, 'Seguridad Industrial', 'INDU-10005', 5, 'Industrial'),
  (6, 'Gestion de Proyectos', 'INDU-10006', 6, 'Industrial'),
  (7, 'Ingenieria Economica', 'INDU-10007', 4, 'Industrial'),
  (8, 'Metrologia', 'INDU-10008', 5, 'Industrial'),
  (9, 'Diseno de Instalaciones', 'INDU-10009', 6, 'Industrial'),
  (10, 'Simulacion de Procesos', 'INDU-10010', 4, 'Industrial'),
  (11, 'Estudio del Trabajo', 'INDU-10011', 5, 'Industrial'),
  (12, 'Mantenimiento Industrial', 'INDU-10012', 6, 'Industrial'),
  (13, 'Gestion Ambiental', 'INDU-10013', 4, 'Industrial'),
  (14, 'Manufactura Esbelta', 'INDU-10014', 5, 'Industrial'),
  (15, 'Automatizacion Industrial', 'INDU-10015', 6, 'Industrial');

-- Reject collisions rather than silently retaining unrelated data or overwriting it.
CREATE TEMPORARY TABLE bulk_guard (ok TINYINT NOT NULL CHECK (ok = 1));
INSERT INTO bulk_guard
SELECT IF(COUNT(*) = 0, 1, 0)
FROM alumnos a JOIN bulk_alumnos b ON b.control = a.control
WHERE a.name <> b.name OR a.epecialidad <> b.epecialidad OR a.fecha_ingreso <> b.fecha_ingreso;
INSERT INTO bulk_guard
SELECT IF(COUNT(*) = 0, 1, 0)
FROM materias m JOIN bulk_materias b
  ON m.clave = b.clave OR (m.nombre = b.nombre AND m.especialidad = b.especialidad)
WHERE m.clave <> b.clave OR m.nombre <> b.nombre
   OR m.especialidad <> b.especialidad OR m.creditos <> b.creditos;

INSERT INTO alumnos (name, control, epecialidad, fecha_ingreso)
SELECT b.name, b.control, b.epecialidad, b.fecha_ingreso
FROM bulk_alumnos b
WHERE NOT EXISTS (SELECT 1 FROM alumnos a WHERE a.control = b.control)
ORDER BY b.seq;

INSERT INTO materias (nombre, clave, creditos, especialidad)
SELECT b.nombre, b.clave, b.creditos, b.especialidad
FROM bulk_materias b
WHERE NOT EXISTS (SELECT 1 FROM materias m WHERE m.clave = b.clave)
ORDER BY b.especialidad, b.seq;

CREATE TEMPORARY TABLE bulk_calificaciones (
  id_alumno INT, id_materia INT, calificacion SMALLINT,
  periodo VARCHAR(7) CHARACTER SET ascii COLLATE ascii_bin,
  PRIMARY KEY (id_alumno, id_materia, periodo)
);
-- Rotate six of the fifteen subjects in each specialty; include both allowed periods.
INSERT INTO bulk_calificaciones
SELECT a.id, m.id, 40 + MOD(b.seq * 7 + s.seq * 3 + p.numero * 11, 61),
       CASE p.numero WHEN 1 THEN 'Ene/Ago' ELSE 'Ago/Dic' END
FROM bulk_alumnos b
JOIN alumnos a ON a.control = b.control
JOIN bulk_materias s ON s.especialidad = b.epecialidad AND MOD(s.seq + b.seq, 15) < 6
JOIN materias m ON m.clave = s.clave
CROSS JOIN (SELECT 1 AS numero UNION ALL SELECT 2) p;

INSERT INTO bulk_guard
SELECT IF(COUNT(*) = 0, 1, 0)
FROM calificaciones c JOIN bulk_calificaciones b
  ON b.id_alumno = c.id_alumno AND b.id_materia = c.id_materia AND b.periodo = c.periodo
WHERE c.calificacion <> b.calificacion;

INSERT INTO calificaciones (id_alumno, id_materia, calificacion, periodo)
SELECT b.id_alumno, b.id_materia, b.calificacion, b.periodo
FROM bulk_calificaciones b
WHERE NOT EXISTS (
  SELECT 1 FROM calificaciones c
  WHERE c.id_alumno = b.id_alumno AND c.id_materia = b.id_materia AND c.periodo = b.periodo
);

INSERT INTO bulk_guard SELECT IF(COUNT(*) = 1800, 1, 0) FROM bulk_calificaciones;
DROP TEMPORARY TABLE bulk_calificaciones, bulk_guard, bulk_materias, bulk_alumnos;
COMMIT;
