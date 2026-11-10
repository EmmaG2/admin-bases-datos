-- Add six specialties through 112 fictional students: 11, 13, 17, 19, 23 and 29 each.
-- Reserved controls: 23043001..23043112. Back up alumnos before execution.
-- Repeating matching rows is a no-op; conflicting controls abort without overwriting data.
-- Use the mysql client without --force so an error closes the connection and rolls back.
START TRANSACTION;
CREATE TEMPORARY TABLE specialty_students (
  seq INT PRIMARY KEY, name VARCHAR(120), control CHAR(8) UNIQUE,
  epecialidad VARCHAR(80), fecha_ingreso DATE
);
INSERT INTO specialty_students VALUES
  (1, 'Aitana Acosta Salazar', '23043001', 'Civil', '2025-08-18'),
  (2, 'Bastian Acosta Salazar', '23043002', 'Civil', '2025-08-18'),
  (3, 'Celia Acosta Salazar', '23043003', 'Civil', '2025-08-18'),
  (4, 'Dario Acosta Salazar', '23043004', 'Civil', '2025-08-18'),
  (5, 'Estela Acosta Salazar', '23043005', 'Civil', '2025-08-18'),
  (6, 'Fabian Acosta Salazar', '23043006', 'Civil', '2025-08-18'),
  (7, 'Gisela Acosta Salazar', '23043007', 'Civil', '2025-08-18'),
  (8, 'Hector Acosta Salazar', '23043008', 'Civil', '2025-08-18'),
  (9, 'Ines Acosta Salazar', '23043009', 'Civil', '2025-08-18'),
  (10, 'Joaquin Acosta Salazar', '23043010', 'Civil', '2025-08-18'),
  (11, 'Kiara Acosta Salazar', '23043011', 'Civil', '2025-08-18'),
  (12, 'Aitana Barragan Molina', '23043012', 'Electrica', '2025-08-18'),
  (13, 'Bastian Barragan Molina', '23043013', 'Electrica', '2025-08-18'),
  (14, 'Celia Barragan Molina', '23043014', 'Electrica', '2025-08-18'),
  (15, 'Dario Barragan Molina', '23043015', 'Electrica', '2025-08-18'),
  (16, 'Estela Barragan Molina', '23043016', 'Electrica', '2025-08-18'),
  (17, 'Fabian Barragan Molina', '23043017', 'Electrica', '2025-08-18'),
  (18, 'Gisela Barragan Molina', '23043018', 'Electrica', '2025-08-18'),
  (19, 'Hector Barragan Molina', '23043019', 'Electrica', '2025-08-18'),
  (20, 'Ines Barragan Molina', '23043020', 'Electrica', '2025-08-18'),
  (21, 'Joaquin Barragan Molina', '23043021', 'Electrica', '2025-08-18'),
  (22, 'Kiara Barragan Molina', '23043022', 'Electrica', '2025-08-18'),
  (23, 'Leonardo Barragan Molina', '23043023', 'Electrica', '2025-08-18'),
  (24, 'Mireya Barragan Molina', '23043024', 'Electrica', '2025-08-18'),
  (25, 'Aitana Cordero Navarro', '23043025', 'Electronica', '2025-08-18'),
  (26, 'Bastian Cordero Navarro', '23043026', 'Electronica', '2025-08-18'),
  (27, 'Celia Cordero Navarro', '23043027', 'Electronica', '2025-08-18'),
  (28, 'Dario Cordero Navarro', '23043028', 'Electronica', '2025-08-18'),
  (29, 'Estela Cordero Navarro', '23043029', 'Electronica', '2025-08-18'),
  (30, 'Fabian Cordero Navarro', '23043030', 'Electronica', '2025-08-18'),
  (31, 'Gisela Cordero Navarro', '23043031', 'Electronica', '2025-08-18'),
  (32, 'Hector Cordero Navarro', '23043032', 'Electronica', '2025-08-18'),
  (33, 'Ines Cordero Navarro', '23043033', 'Electronica', '2025-08-18'),
  (34, 'Joaquin Cordero Navarro', '23043034', 'Electronica', '2025-08-18'),
  (35, 'Kiara Cordero Navarro', '23043035', 'Electronica', '2025-08-18'),
  (36, 'Leonardo Cordero Navarro', '23043036', 'Electronica', '2025-08-18'),
  (37, 'Mireya Cordero Navarro', '23043037', 'Electronica', '2025-08-18'),
  (38, 'Noe Cordero Navarro', '23043038', 'Electronica', '2025-08-18'),
  (39, 'Olivia Cordero Navarro', '23043039', 'Electronica', '2025-08-18'),
  (40, 'Patricio Cordero Navarro', '23043040', 'Electronica', '2025-08-18'),
  (41, 'Renata Cordero Navarro', '23043041', 'Electronica', '2025-08-18'),
  (42, 'Aitana Delgado Ponce', '23043042', 'Mecanica', '2025-08-18'),
  (43, 'Bastian Delgado Ponce', '23043043', 'Mecanica', '2025-08-18'),
  (44, 'Celia Delgado Ponce', '23043044', 'Mecanica', '2025-08-18'),
  (45, 'Dario Delgado Ponce', '23043045', 'Mecanica', '2025-08-18'),
  (46, 'Estela Delgado Ponce', '23043046', 'Mecanica', '2025-08-18'),
  (47, 'Fabian Delgado Ponce', '23043047', 'Mecanica', '2025-08-18'),
  (48, 'Gisela Delgado Ponce', '23043048', 'Mecanica', '2025-08-18'),
  (49, 'Hector Delgado Ponce', '23043049', 'Mecanica', '2025-08-18'),
  (50, 'Ines Delgado Ponce', '23043050', 'Mecanica', '2025-08-18'),
  (51, 'Joaquin Delgado Ponce', '23043051', 'Mecanica', '2025-08-18'),
  (52, 'Kiara Delgado Ponce', '23043052', 'Mecanica', '2025-08-18'),
  (53, 'Leonardo Delgado Ponce', '23043053', 'Mecanica', '2025-08-18'),
  (54, 'Mireya Delgado Ponce', '23043054', 'Mecanica', '2025-08-18'),
  (55, 'Noe Delgado Ponce', '23043055', 'Mecanica', '2025-08-18'),
  (56, 'Olivia Delgado Ponce', '23043056', 'Mecanica', '2025-08-18'),
  (57, 'Patricio Delgado Ponce', '23043057', 'Mecanica', '2025-08-18'),
  (58, 'Renata Delgado Ponce', '23043058', 'Mecanica', '2025-08-18'),
  (59, 'Samuel Delgado Ponce', '23043059', 'Mecanica', '2025-08-18'),
  (60, 'Tamara Delgado Ponce', '23043060', 'Mecanica', '2025-08-18'),
  (61, 'Aitana Esquivel Rocha', '23043061', 'Mecatronica', '2025-08-18'),
  (62, 'Bastian Esquivel Rocha', '23043062', 'Mecatronica', '2025-08-18'),
  (63, 'Celia Esquivel Rocha', '23043063', 'Mecatronica', '2025-08-18'),
  (64, 'Dario Esquivel Rocha', '23043064', 'Mecatronica', '2025-08-18'),
  (65, 'Estela Esquivel Rocha', '23043065', 'Mecatronica', '2025-08-18'),
  (66, 'Fabian Esquivel Rocha', '23043066', 'Mecatronica', '2025-08-18'),
  (67, 'Gisela Esquivel Rocha', '23043067', 'Mecatronica', '2025-08-18'),
  (68, 'Hector Esquivel Rocha', '23043068', 'Mecatronica', '2025-08-18'),
  (69, 'Ines Esquivel Rocha', '23043069', 'Mecatronica', '2025-08-18'),
  (70, 'Joaquin Esquivel Rocha', '23043070', 'Mecatronica', '2025-08-18'),
  (71, 'Kiara Esquivel Rocha', '23043071', 'Mecatronica', '2025-08-18'),
  (72, 'Leonardo Esquivel Rocha', '23043072', 'Mecatronica', '2025-08-18'),
  (73, 'Mireya Esquivel Rocha', '23043073', 'Mecatronica', '2025-08-18'),
  (74, 'Noe Esquivel Rocha', '23043074', 'Mecatronica', '2025-08-18'),
  (75, 'Olivia Esquivel Rocha', '23043075', 'Mecatronica', '2025-08-18'),
  (76, 'Patricio Esquivel Rocha', '23043076', 'Mecatronica', '2025-08-18'),
  (77, 'Renata Esquivel Rocha', '23043077', 'Mecatronica', '2025-08-18'),
  (78, 'Samuel Esquivel Rocha', '23043078', 'Mecatronica', '2025-08-18'),
  (79, 'Tamara Esquivel Rocha', '23043079', 'Mecatronica', '2025-08-18'),
  (80, 'Ulises Esquivel Rocha', '23043080', 'Mecatronica', '2025-08-18'),
  (81, 'Valentina Esquivel Rocha', '23043081', 'Mecatronica', '2025-08-18'),
  (82, 'Walter Esquivel Rocha', '23043082', 'Mecatronica', '2025-08-18'),
  (83, 'Ximena Esquivel Rocha', '23043083', 'Mecatronica', '2025-08-18'),
  (84, 'Aitana Figueroa Tapia', '23043084', 'Quimica', '2025-08-18'),
  (85, 'Bastian Figueroa Tapia', '23043085', 'Quimica', '2025-08-18'),
  (86, 'Celia Figueroa Tapia', '23043086', 'Quimica', '2025-08-18'),
  (87, 'Dario Figueroa Tapia', '23043087', 'Quimica', '2025-08-18'),
  (88, 'Estela Figueroa Tapia', '23043088', 'Quimica', '2025-08-18'),
  (89, 'Fabian Figueroa Tapia', '23043089', 'Quimica', '2025-08-18'),
  (90, 'Gisela Figueroa Tapia', '23043090', 'Quimica', '2025-08-18'),
  (91, 'Hector Figueroa Tapia', '23043091', 'Quimica', '2025-08-18'),
  (92, 'Ines Figueroa Tapia', '23043092', 'Quimica', '2025-08-18'),
  (93, 'Joaquin Figueroa Tapia', '23043093', 'Quimica', '2025-08-18'),
  (94, 'Kiara Figueroa Tapia', '23043094', 'Quimica', '2025-08-18'),
  (95, 'Leonardo Figueroa Tapia', '23043095', 'Quimica', '2025-08-18'),
  (96, 'Mireya Figueroa Tapia', '23043096', 'Quimica', '2025-08-18'),
  (97, 'Noe Figueroa Tapia', '23043097', 'Quimica', '2025-08-18'),
  (98, 'Olivia Figueroa Tapia', '23043098', 'Quimica', '2025-08-18'),
  (99, 'Patricio Figueroa Tapia', '23043099', 'Quimica', '2025-08-18'),
  (100, 'Renata Figueroa Tapia', '23043100', 'Quimica', '2025-08-18'),
  (101, 'Samuel Figueroa Tapia', '23043101', 'Quimica', '2025-08-18'),
  (102, 'Tamara Figueroa Tapia', '23043102', 'Quimica', '2025-08-18'),
  (103, 'Ulises Figueroa Tapia', '23043103', 'Quimica', '2025-08-18'),
  (104, 'Valentina Figueroa Tapia', '23043104', 'Quimica', '2025-08-18'),
  (105, 'Walter Figueroa Tapia', '23043105', 'Quimica', '2025-08-18'),
  (106, 'Ximena Figueroa Tapia', '23043106', 'Quimica', '2025-08-18'),
  (107, 'Yahir Figueroa Tapia', '23043107', 'Quimica', '2025-08-18'),
  (108, 'Zoe Figueroa Tapia', '23043108', 'Quimica', '2025-08-18'),
  (109, 'Alonso Figueroa Tapia', '23043109', 'Quimica', '2025-08-18'),
  (110, 'Beatriz Figueroa Tapia', '23043110', 'Quimica', '2025-08-18'),
  (111, 'Camilo Figueroa Tapia', '23043111', 'Quimica', '2025-08-18'),
  (112, 'Daniela Figueroa Tapia', '23043112', 'Quimica', '2025-08-18');
CREATE TEMPORARY TABLE specialty_guard (ok TINYINT NOT NULL CHECK (ok = 1));
INSERT INTO specialty_guard
SELECT IF(COUNT(*) = 0, 1, 0)
FROM alumnos a JOIN specialty_students s ON a.control = s.control
WHERE a.name <> s.name OR a.epecialidad <> s.epecialidad OR a.fecha_ingreso <> s.fecha_ingreso;
-- Keep each new specialty's count exact, including on a repeated run.
INSERT INTO specialty_guard
SELECT IF(COUNT(*) = 0, 1, 0)
FROM alumnos a
WHERE a.epecialidad IN ('Civil', 'Electrica', 'Electronica', 'Mecanica', 'Mecatronica', 'Quimica')
  AND NOT EXISTS (SELECT 1 FROM specialty_students b WHERE b.control = a.control);
-- New fictional names must not identify a different existing student.
INSERT INTO specialty_guard
SELECT IF(COUNT(*) = 0, 1, 0)
FROM alumnos a JOIN specialty_students s ON a.name = s.name
WHERE a.control <> s.control;
INSERT INTO alumnos (name, control, epecialidad, fecha_ingreso)
SELECT s.name, s.control, s.epecialidad, s.fecha_ingreso
FROM specialty_students s
WHERE NOT EXISTS (SELECT 1 FROM alumnos a WHERE a.control = s.control)
ORDER BY s.seq;
INSERT INTO specialty_guard
SELECT IF(COUNT(*) = 112, 1, 0)
FROM alumnos a JOIN specialty_students s ON s.control = a.control;
DROP TEMPORARY TABLE specialty_guard, specialty_students;
COMMIT;
