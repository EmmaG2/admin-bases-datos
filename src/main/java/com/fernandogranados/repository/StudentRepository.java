package com.fernandogranados.repository;

import com.fernandogranados.model.Alumno;
import com.fernandogranados.model.Calificaciones;
import com.fernandogranados.model.ReportCard;
import com.fernandogranados.model.StudentReport;
import com.fernandogranados.model.ReportCard.Grade;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public final class StudentRepository {
  private static final String COLUMNS = "id, name, control, epecialidad, fecha_ingreso";
  private final Connection connection;

  public StudentRepository(Connection connection) {
    this.connection = Objects.requireNonNull(connection);
  }

  public int create(Alumno student) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        "INSERT INTO alumnos (name, control, epecialidad, fecha_ingreso) VALUES (?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS)) {
      bind(statement, student);
      statement.executeUpdate();
      try (ResultSet keys = statement.getGeneratedKeys()) {
        if (keys.next()) {
          return keys.getInt(1);
        }

        throw new SQLException("La base de datos no devolvió el identificador del alumno.");
      }
    }
  }

  public int createWithInitialGrade(Alumno student, Calificaciones grade) throws SQLException {
    Connection con = connection;
    try {
      con.setAutoCommit(false);

      String sqlAlumno = "INSERT INTO alumnos (name, control, epecialidad, fecha_ingreso) VALUES (?, ?, ?, ?)";
      PreparedStatement psAlumno = con.prepareStatement(sqlAlumno, Statement.RETURN_GENERATED_KEYS);
      bind(psAlumno, student);
      psAlumno.executeUpdate();

      ResultSet keys = psAlumno.getGeneratedKeys();
      if (!keys.next()) {
        throw new SQLException("La base de datos no devolvió el identificador del alumno.");
      }
      int studentId = keys.getInt(1);

      String sqlCalificacion = "INSERT INTO calificaciones (id_alumno, id_materia, calificacion, periodo) VALUES (?, ?, ?, ?)";
      PreparedStatement psCalificacion = con.prepareStatement(sqlCalificacion);
      psCalificacion.setInt(1, studentId);
      psCalificacion.setInt(2, grade.getIdMateria());
      psCalificacion.setShort(3, grade.getCalificacion());
      psCalificacion.setString(4, grade.getPeriodo());
      psCalificacion.executeUpdate();

      con.commit();
      log.info("Transacción exitosa");
      return studentId;
    } catch (Exception error) {
      if (con != null) {
        con.rollback();
      }
      log.error("Se canceló la operación", error);
      throw new SQLException("No se pudo registrar el alumno y su calificación.", error);
    }
  }

  public Optional<Alumno> findById(int id) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        "SELECT " + COLUMNS + " FROM alumnos WHERE id = ?")) {
      statement.setInt(1, id);
      try (ResultSet rows = statement.executeQuery()) {
        if (rows.next()) {
          return Optional.of(map(rows));
        }

        return Optional.empty();
      }
    }
  }

  public List<Alumno> findAll() throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        "SELECT " + COLUMNS + " FROM alumnos ORDER BY id");
        ResultSet rows = statement.executeQuery()) {
      List<Alumno> students = new ArrayList<>();
      while (rows.next()) {
        students.add(map(rows));
      }

      return students;
    }
  }

  public StudentReport findStudentReport() throws SQLException {
    List<Alumno> allStudents = findAll();
    List<StudentReport.SubjectCount> subjects = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement("""
        SELECT m.id, m.clave, m.nombre, m.especialidad,
               COUNT(DISTINCT c.id_alumno) AS alumnos
        FROM materias m
        LEFT JOIN calificaciones c ON c.id_materia = m.id
        GROUP BY m.id, m.clave, m.nombre, m.especialidad
        ORDER BY m.especialidad, m.nombre, m.id
        """); ResultSet rows = statement.executeQuery()) {
      while (rows.next()) {
        String code = rows.getString("clave");
        String name = rows.getString("nombre");
        String specialty = rows.getString("especialidad");
        long studentCount = rows.getLong("alumnos");

        subjects.add(new StudentReport.SubjectCount(code, name, specialty, studentCount));
      }
    }
    return new StudentReport(allStudents, subjects);
  }

  public ReportCard findReportCard(int studentId, String specialty) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement("""
        SELECT m.nombre AS materia, m.clave, c.periodo, c.calificacion,
          (SELECT AVG(g.calificacion)
           FROM calificaciones g WHERE g.id_alumno = c.id_alumno) AS promedio
        FROM calificaciones c
        INNER JOIN materias m ON m.id = c.id_materia
        WHERE c.id_alumno = ? AND m.especialidad = ?
        ORDER BY CASE c.periodo WHEN 'Ene/Ago' THEN 1 ELSE 2 END, m.nombre, m.id
        """)) {
      statement.setInt(1, studentId);
      statement.setString(2, specialty);
      try (ResultSet rows = statement.executeQuery()) {
        List<Grade> grades = new ArrayList<>();
        Double average = null;
        while (rows.next()) {
          String subject = rows.getString("materia");
          String code = rows.getString("clave");
          String period = rows.getString("periodo");
          short grade = rows.getShort("calificacion");

          grades.add(new Grade(subject, code, period, grade));

          double value = rows.getDouble("promedio");
          if (rows.wasNull()) {
            average = null;
          } else {
            average = Math.round(value * 100.0) / 100.0;
          }
        }
        return new ReportCard(List.copyOf(grades), average);
      }
    }
  }

  public boolean update(int id, Alumno student) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        "UPDATE alumnos SET name = ?, control = ?, epecialidad = ?, fecha_ingreso = ? WHERE id = ?")) {
      bind(statement, student);
      statement.setInt(5, id);

      int rows = statement.executeUpdate();
      log.info("Updated rows: {}", rows);
      return rows > 0;
    }
  }

  public boolean delete(int id) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement("DELETE FROM alumnos WHERE id = ?")) {
      statement.setInt(1, id);
      return statement.executeUpdate() > 0;
    }
  }

  private static void bind(PreparedStatement statement, Alumno student) throws SQLException {
    statement.setString(1, student.getNombre().trim());
    statement.setString(2, student.getMatricula());
    statement.setString(3, student.getEspecialidad().trim());
    statement.setDate(4, Date.valueOf(student.getFechaIngreso()));
  }

  private static Alumno map(ResultSet row) throws SQLException {
    return new Alumno(row.getInt("id"), row.getString("name"), row.getString("control"),
        row.getString("epecialidad"), row.getDate("fecha_ingreso").toString());
  }
}
