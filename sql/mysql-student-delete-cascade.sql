-- Run against the application's existing MySQL database.
ALTER TABLE calificaciones
  DROP FOREIGN KEY fk_calificaciones_alumno,
  ADD CONSTRAINT fk_calificaciones_alumno_cascade
    FOREIGN KEY (id_alumno) REFERENCES alumnos(id) ON DELETE CASCADE;
