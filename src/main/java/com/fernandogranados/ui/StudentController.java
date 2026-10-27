package com.fernandogranados.ui;

import com.fernandogranados.model.Alumno;
import com.fernandogranados.repository.StudentRepository;
import com.fernandogranados.model.ReportCard;
import com.fernandogranados.pdf.ReportCardPdfExporter;
import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

public final class StudentController {
  private final ExecutorService database = Executors.newSingleThreadExecutor();
  private Connection connection;
  private StudentRepository students;
  private StudentView view;
  private Stage stage;
  private boolean closing;
  private boolean refreshing;
  private long reportRequest;
  private ReportCard currentReport;

  public void start(Stage stage) {
    this.stage = stage;
    view = new StudentView(stage, () -> load(null, null, "La lista de alumnos se actualizó."),
        this::create, this::update, this::delete, this::clearForm, this::loadReportCard,
        this::exportPdf, this::showError);
    load(null, null, "La lista de alumnos se cargó.");
  }

  private int selectedId() {
    Alumno student = view.table.getSelectionModel().getSelectedItem();
    if (student == null) throw new IllegalArgumentException("Selecciona un alumno primero.");
    return student.getId();
  }

  private void create() {
    Alumno student = view.form.student();
    load(() -> students.create(student), null, "El alumno se registró.");
  }

  private void update() {
    int id = selectedId();
    Alumno student = view.form.student();
    load(() -> {
      if (!students.update(id, student)) throw new IllegalArgumentException("El alumno ya no existe.");
      return id;
    }, id, "Los datos del alumno se guardaron.");
  }

  private void delete() {
    int id = selectedId();
    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
        "¿Eliminar definitivamente al alumno " + id + " y todas sus calificaciones? Esta acción no se puede deshacer.",
        ButtonType.CANCEL, ButtonType.OK);
    configureAlert(confirmation, "Confirmar eliminación");
    ((Button) confirmation.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Cancelar");
    ((Button) confirmation.getDialogPane().lookupButton(ButtonType.OK)).setText("Eliminar");
    if (confirmation.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;
    load(() -> {
      if (!students.delete(id)) throw new IllegalArgumentException("El alumno ya no existe.");
      return null;
    }, null, "El alumno se eliminó.");
  }

  private void load(DatabaseAction action, Integer selected, String success) {
    if (closing) return;
    Alumno previousReportStudent = view.gradeStudent.getValue();
    refreshing = true;
    reportRequest++;
    currentReport = null;
    view.clearReport("Cargando alumnos…");
    view.content.setDisable(true);
    view.status.setText("Procesando…");
    Task<List<Alumno>> task = new Task<>() {
      private Integer selectedId = selected;

      @Override protected List<Alumno> call() throws SQLException {
        if (connection == null) {
          connection = DriverManager.getConnection(System.getenv().getOrDefault("MYSQL_URL",
              "jdbc:mysql://127.0.0.1:3306/segunda_unidad"), "root", "1234");
          students = new StudentRepository(connection);
        }
        if (action != null) selectedId = action.run();
        return students.findAll();
      }

      @Override protected void succeeded() {
        if (closing) return;
        view.table.getItems().setAll(getValue());
        clearForm();
        if (selectedId != null) {
          view.table.getItems().stream().filter(student -> student.getId() == selectedId).findFirst()
              .ifPresent(student -> { view.table.getSelectionModel().select(student); view.table.scrollTo(student); });
        }
        view.count.setText(getValue().size() + (getValue().size() == 1 ? " alumno" : " alumnos"));
        view.gradeStudent.getItems().setAll(getValue());
        Integer reportStudentId = selectedId != null ? selectedId
            : previousReportStudent == null ? null : previousReportStudent.getId();
        Alumno reportStudent = getValue().stream()
            .filter(student -> reportStudentId != null && student.getId() == reportStudentId)
            .findFirst().orElse(getValue().isEmpty() ? null : getValue().getFirst());
        view.gradeStudent.setValue(reportStudent);
        refreshing = false;
        view.content.setDisable(false);
        view.status.setText(success);
        loadReportCard();
      }

      @Override protected void failed() {
        if (closing) return;
        refreshing = false;
        view.clearReport("No se pudo cargar la lista de alumnos. Pulsa Actualizar para intentarlo de nuevo.");
        view.content.setDisable(false);
        view.status.setText("La operación falló. Pulsa Actualizar antes de intentar otro cambio.");
        showError(getException());
      }
    };
    database.execute(task);
  }

  private void loadReportCard() {
    if (closing || refreshing) return;
    currentReport = null;
    long request = ++reportRequest;
    Alumno student = view.gradeStudent.getValue();
    view.clearReport(student == null ? "No hay alumnos registrados. Registra un alumno primero." : "Cargando calificaciones…");
    if (student == null || students == null) return;
    Task<ReportCard> task = new Task<>() {
      @Override protected ReportCard call() throws SQLException {
        return students.findReportCard(student.getId(), "Sistemas");
      }

      @Override protected void succeeded() {
        if (closing || request != reportRequest) return;
        currentReport = getValue();
        view.showReport(student, getValue());
      }

      @Override protected void failed() {
        if (closing || request != reportRequest) return;
        view.clearReport("No se pudieron cargar las calificaciones. Pulsa Actualizar para intentarlo de nuevo.");
        showError(getException());
      }
    };
    database.execute(task);
  }

  private void exportPdf() {
    Alumno student = view.gradeStudent.getValue();
    ReportCard report = currentReport;
    if (closing || student == null || report == null) return;
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Exportar PDF");
    chooser.setInitialFileName("boleta-" + student.getMatricula() + ".pdf");
    chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documentos PDF", "*.pdf"));
    File file = chooser.showSaveDialog(stage);
    if (file == null) return;
    Path destination = file.toPath();
    view.content.setDisable(true);
    view.status.setText("Exportando PDF…");
    Task<Void> task = new Task<>() {
      @Override protected Void call() throws Exception {
        new ReportCardPdfExporter().export(student, report, destination);
        return null;
      }

      @Override protected void succeeded() {
        if (closing) return;
        view.content.setDisable(false);
        view.status.setText("PDF exportado: " + destination);
      }

      @Override protected void failed() {
        if (closing) return;
        view.content.setDisable(false);
        view.status.setText("No se pudo exportar el PDF.");
        showError(getException());
      }
    };
    database.execute(task);
  }

  private void clearForm() {
    view.table.getSelectionModel().clearSelection();
    view.form.clear();
  }

  private void showError(Throwable error) {
    error.printStackTrace();
    String message = error instanceof IllegalArgumentException
        ? error.getMessage() : "No se pudo completar la operación. Revisa los datos y vuelve a intentarlo.";
    if (error instanceof SQLException sql) {
      message = sql.getSQLState() != null && sql.getSQLState().startsWith("23")
          ? "No se pudo guardar o eliminar al alumno: la matrícula está duplicada o existen calificaciones relacionadas."
          : "No se pudo realizar la operación en la base de datos. Revisa la conexión e inténtalo de nuevo.";
    }
    Alert alert = new Alert(error instanceof IllegalArgumentException ? Alert.AlertType.WARNING : Alert.AlertType.ERROR);
    alert.setContentText(message == null ? "No se pudo completar la operación." : message);
    configureAlert(alert, error instanceof IllegalArgumentException ? "Datos inválidos" : "La operación falló");
    ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Aceptar");
    alert.showAndWait();
  }

  private void configureAlert(Alert alert, String title) {
    alert.initOwner(stage);
    alert.setTitle(title);
    alert.setHeaderText(title);
    alert.getDialogPane().getStylesheets().add(
        Objects.requireNonNull(StudentController.class.getResource("/styles/students.css")).toExternalForm());
  }

  public void stop() {
    closing = true;
    database.execute(() -> {
      if (connection == null) return;
      try {
        connection.close();
      } catch (SQLException error) {
        System.err.println("No se pudo cerrar la conexión con la base de datos: " + error.getMessage());
      }
    });
    database.shutdown();
  }

  @FunctionalInterface private interface DatabaseAction { Integer run() throws SQLException; }
}
