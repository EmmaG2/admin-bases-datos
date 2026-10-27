package com.fernandogranados.ui;

import com.fernandogranados.model.Alumno;
import com.fernandogranados.model.ReportCard.Grade;
import com.fernandogranados.model.ReportCard;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

final class StudentView {
  final TableView<Alumno> table = new TableView<>();
  final Label status = new Label("Conectando con MySQL…");
  final Label count = new Label();
  final BorderPane content;
  final StudentForm form;
  final ComboBox<Alumno> gradeStudent = new ComboBox<>();
  final Button exportPdf;
  private final TableView<Grade> grades = new TableView<>();
  private final Label reportName = new Label();
  private final Label reportDetails = new Label();
  private final Label average = new Label("No disponible");
  private final Label gradeStatus = new Label("Selecciona un alumno para consultar sus calificaciones.");

  StudentView(Stage stage, Runnable refreshAction, Runnable createAction,
      Runnable updateAction, Runnable deleteAction, Runnable clearAction,
      Runnable gradeAction, Runnable exportAction, Consumer<Throwable> onError) {
    exportPdf = button("Exportar PDF", exportAction, onError);
    exportPdf.getStyleClass().add("primary");
    exportPdf.setDisable(true);
    Label title = new Label("Alumnos");
    title.getStyleClass().add("page-title");
    VBox heading = new VBox(6, title);
    Button refresh = button("Actualizar", refreshAction, onError);
    HBox header = new HBox(24, heading, refresh);
    header.setAlignment(Pos.CENTER_LEFT);
    HBox.setHgrow(heading, Priority.ALWAYS);
    header.getStyleClass().add("page-header");

    addColumn("ID", student -> String.valueOf(student.getId()), 60);
    addColumn("Nombre", Alumno::getNombre, 190);
    addColumn("Matrícula", Alumno::getMatricula, 140);
    addColumn("Especialidad", Alumno::getEspecialidad, 150);
    addColumn("Fecha de ingreso", Alumno::getFechaIngreso, 120);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    table.setPlaceholder(new Label("No hay alumnos registrados. Registra un alumno para comenzar."));
    form = new StudentForm(table, createAction, updateAction, deleteAction, clearAction, onError);
    table.getSelectionModel().selectedItemProperty().addListener((observable, previous, student) -> {
      if (student != null) {
        form.edit(student);
        gradeStudent.getSelectionModel().select(student);
      }
    });
    count.getStyleClass().add("section-title");
    VBox records = new VBox(16, count, table);
    records.getStyleClass().add("records");
    VBox.setVgrow(table, Priority.ALWAYS);

    ScrollPane formScroll = new ScrollPane(form);
    formScroll.setFitToWidth(true);
    formScroll.setPrefWidth(300);
    formScroll.setMinWidth(260);
    formScroll.getStyleClass().add("form-scroll");

    Tab studentsTab = new Tab("Alumnos", new BorderPane(records, header, formScroll, null, null));
    Tab gradesTab = new Tab("Calificaciones", reportCard(refreshAction, gradeAction, onError));
    TabPane tabs = new TabPane(studentsTab, gradesTab);
    tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    tabs.getStyleClass().add("student-tabs");
    content = new BorderPane(tabs);
    status.getStyleClass().add("status");
    BorderPane root = new BorderPane(content);
    root.setBottom(status);
    Scene scene = new Scene(root, 1120, 740);
    scene.getStylesheets().add(StudentView.class.getResource("/styles/students.css").toExternalForm());
    stage.setTitle("Administración de alumnos");
    stage.setMinWidth(880);
    stage.setMinHeight(620);
    stage.setScene(scene);
    stage.show();
  }

  private BorderPane reportCard(Runnable refreshAction, Runnable gradeAction, Consumer<Throwable> onError) {
    Label title = new Label("Boleta de calificaciones");
    title.getStyleClass().add("page-title");
    HBox.setHgrow(title, Priority.ALWAYS);
    title.setMaxWidth(Double.MAX_VALUE);
    HBox header = new HBox(24, title, exportPdf, button("Actualizar", refreshAction, onError));
    header.setAlignment(Pos.CENTER_LEFT);
    header.getStyleClass().add("page-header");
    gradeStudent.setMaxWidth(Double.MAX_VALUE);
    gradeStudent.setPromptText("Selecciona un alumno");
    gradeStudent.setConverter(new StringConverter<>() {
      @Override public String toString(Alumno student) {
        return student == null ? "" : student.getNombre() + " · " + student.getMatricula();
      }
      @Override public Alumno fromString(String text) { return null; }
    });
    gradeStudent.valueProperty().addListener((observable, previous, student) -> gradeAction.run());
    Label studentLabel = new Label("Alumno");
    studentLabel.setLabelFor(gradeStudent);
    gradeStudent.setAccessibleText("Boleta de calificaciones del alumno");
    reportName.getStyleClass().add("section-title");
    reportName.setWrapText(true);
    reportDetails.getStyleClass().add("muted");
    reportDetails.setWrapText(true);
    average.getStyleClass().add("page-title");
    Label averageLabel = new Label("Promedio general / 100");
    Label averageScope = new Label("Todas las materias y periodos · Promedio no ponderado");
    averageScope.getStyleClass().add("muted");
    VBox summary = new VBox(6, reportName, reportDetails, averageLabel, average, averageScope);
    summary.getStyleClass().add("report-summary");
    addGradeColumn("Materia", Grade::subject, 420);
    addGradeColumn("Periodo", grade -> String.valueOf(grade.period()), 140);
    addGradeColumn("Calificación / 100", grade -> String.valueOf(grade.grade()), 140);
    grades.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    grades.setPlaceholder(gradeStatus);
    VBox.setVgrow(grades, Priority.ALWAYS);
    VBox report = new VBox(16, new VBox(8, studentLabel, gradeStudent), summary, grades);
    report.getStyleClass().add("records");
    return new BorderPane(report, header, null, null, null);
  }

  void clearReport(String message) {
    exportPdf.setDisable(true);
    grades.getItems().clear();
    average.setText("No disponible");
    reportName.setText("");
    reportDetails.setText("");
    gradeStatus.setText(message);
  }

  void showReport(Alumno student, ReportCard report) {
    exportPdf.setDisable(false);
    reportName.setText(student.getNombre());
    reportDetails.setText("Matrícula: " + student.getMatricula() + " · Especialidad: " + student.getEspecialidad());
    grades.getItems().setAll(report.grades());
    average.setText(report.average() == null ? "No disponible"
        : String.format(Locale.forLanguageTag("es-MX"), "%.2f", report.average()));
    gradeStatus.setText("No hay calificaciones registradas en las materias de Sistemas.");
  }

  private void addGradeColumn(String title, Function<Grade, String> value, double width) {
    TableColumn<Grade, String> column = new TableColumn<>(title);
    column.setCellValueFactory(cell -> new ReadOnlyStringWrapper(value.apply(cell.getValue())));
    column.setPrefWidth(width);
    grades.getColumns().add(column);
  }

  static Button button(String text, Runnable action, Consumer<Throwable> onError) {
    Button button = new Button(text);
    button.setOnAction(event -> {
      try {
        action.run();
      } catch (IllegalArgumentException error) {
        onError.accept(error);
      }
    });
    return button;
  }

  private void addColumn(String title, Function<Alumno, String> value, double width) {
    TableColumn<Alumno, String> column = new TableColumn<>(title);
    column.setCellValueFactory(cell -> new ReadOnlyStringWrapper(value.apply(cell.getValue())));
    column.setPrefWidth(width);
    table.getColumns().add(column);
  }
}
