package com.fernandogranados.repository;

import com.fernandogranados.model.Alumno;
import com.fernandogranados.model.ReportCard;
import com.fernandogranados.model.ReportCard.Grade;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class StudentRepository {
  private static final String COLUMNS = "id, name, control, epecialidad, fecha_ingreso";
  private final Connection connection;

  public StudentRepository(Connection connection) {
    this.connection = java.util.Objects.requireNonNull(connection);
  }

  public int create(Alumno student) throws SQLException {
    validate(student);
    try (PreparedStatement statement = connection.prepareStatement(
        "INSERT INTO alumnos (name, control, epecialidad, fecha_ingreso) VALUES (?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS)) {
      bind(statement, student);
      statement.executeUpdate();
      try (ResultSet keys = statement.getGeneratedKeys()) {
        if (!keys.next()) throw new SQLException("La base de datos no devolvió el identificador del alumno.");
        return keys.getInt(1);
      }
    }
  }

  public Optional<Alumno> findById(int id) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        "SELECT " + COLUMNS + " FROM alumnos WHERE id = ?")) {
      statement.setInt(1, id);
      try (ResultSet rows = statement.executeQuery()) {
        return rows.next() ? Optional.of(map(rows)) : Optional.empty();
      }
    }
  }

  public List<Alumno> findAll() throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        "SELECT " + COLUMNS + " FROM alumnos ORDER BY id");
         ResultSet rows = statement.executeQuery()) {
      List<Alumno> students = new ArrayList<>();
      while (rows.next()) students.add(map(rows));
      return students;
    }
  }

  public ReportCard findReportCard(int studentId, String specialty) throws SQLException {
    validateId(studentId);
    try (PreparedStatement statement = connection.prepareStatement("""
        SELECT m.nombre AS materia, c.periodo, c.calificacion,
          (SELECT AVG(g.calificacion)
           FROM calificaciones g WHERE g.id_alumno = c.id_alumno) AS promedio
        FROM calificaciones c
        INNER JOIN materias m ON m.id = c.id_materia
        WHERE c.id_alumno = ? AND m.especialidad = ?
        ORDER BY c.periodo, m.nombre, m.id
        """)) {
      statement.setInt(1, studentId);
      statement.setString(2, specialty);
      try (ResultSet rows = statement.executeQuery()) {
        List<Grade> grades = new ArrayList<>();
        Double average = null;
        while (rows.next()) {
          grades.add(new Grade(rows.getString("materia"), rows.getShort("periodo"),
              rows.getShort("calificacion")));
          double value = rows.getDouble("promedio");
          average = rows.wasNull() ? null : Math.round(value * 100.0) / 100.0;
        }
        return new ReportCard(List.copyOf(grades), average);
      }
    }
  }

  public boolean update(int id, Alumno student) throws SQLException {
    validateId(id);
    validate(student);
    try (PreparedStatement statement = connection.prepareStatement(
        "UPDATE alumnos SET name = ?, control = ?, epecialidad = ?, fecha_ingreso = ? WHERE id = ?")) {
      bind(statement, student);
      statement.setInt(5, id);
      return statement.executeUpdate() > 0;
    }
  }

  public boolean delete(int id) throws SQLException {
    validateId(id);
    try (PreparedStatement statement = connection.prepareStatement("DELETE FROM alumnos WHERE id = ?")) {
      statement.setInt(1, id);
      return statement.executeUpdate() > 0;
    }
  }

  private static void validateId(int id) {
    if (id <= 0) throw new IllegalArgumentException("Selecciona un alumno primero.");
  }

  private static void validate(Alumno student) {
    if (student == null) throw new IllegalArgumentException("El alumno es obligatorio.");
    if (student.getNombre() == null || student.getNombre().isBlank()
        || student.getNombre().length() > 80) {
      throw new IllegalArgumentException("El nombre es obligatorio y debe tener como máximo 80 caracteres.");
    }
    if (student.getMatricula() == null || !student.getMatricula().matches("2304[0-9]{4}")) {
      throw new IllegalArgumentException("La matrícula debe tener el formato 2304XXXX, donde cada X es un dígito.");
    }
    if (student.getEspecialidad() == null || student.getEspecialidad().isBlank()
        || student.getEspecialidad().length() > 80) {
      throw new IllegalArgumentException("La especialidad es obligatoria y debe tener como máximo 80 caracteres.");
    }
    if (student.getFechaIngreso() == null) throw new IllegalArgumentException("Ingresa una fecha válida con el formato AAAA-MM-DD.");
    try {
      LocalDate.parse(student.getFechaIngreso());
    } catch (DateTimeParseException error) {
      throw new IllegalArgumentException("Ingresa una fecha válida con el formato AAAA-MM-DD.", error);
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
