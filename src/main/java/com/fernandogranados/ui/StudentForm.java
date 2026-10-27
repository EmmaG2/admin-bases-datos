package com.fernandogranados.ui;

import com.fernandogranados.model.Alumno;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

final class StudentForm extends VBox {
  private final TextField name = new TextField();
  private final TextField control = new TextField();
  private final TextField specialty = new TextField();
  private final TextField entryDate = new TextField();
  private final Label formTitle = new Label("Nuevo alumno");

  StudentForm(TableView<Alumno> table, Runnable createAction, Runnable updateAction,
      Runnable deleteAction, Runnable clearAction, Consumer<Throwable> onError) {
    formTitle.getStyleClass().add("section-title");
    control.setPromptText("2304XXXX");
    entryDate.setPromptText("AAAA-MM-DD");
    Button create = StudentView.button("Registrar alumno", createAction, onError);
    create.getStyleClass().add("primary");
    create.setMaxWidth(Double.MAX_VALUE);
    Button update = StudentView.button("Guardar cambios", updateAction, onError);
    Button delete = StudentView.button("Eliminar alumno", deleteAction, onError);
    update.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
    delete.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
    update.setMaxWidth(Double.MAX_VALUE);
    delete.setMaxWidth(Double.MAX_VALUE);
    setSpacing(18);
    getChildren().addAll(formTitle, field("Nombre", name), field("Matrícula", control),
        field("Especialidad", specialty), field("Fecha de ingreso", entryDate), create, update, delete,
        StudentView.button("Limpiar formulario", clearAction, onError));
    getStyleClass().add("student-form");
  }

  private VBox field(String text, TextField input) {
    Label label = new Label(text);
    label.setLabelFor(input);
    input.setAccessibleText(text);
    return new VBox(8, label, input);
  }

  Alumno student() {
    return new Alumno(0, name.getText(), control.getText().trim(), specialty.getText(), entryDate.getText().trim());
  }

  void edit(Alumno student) {
    formTitle.setText("Editar alumno");
    name.setText(student.getNombre());
    control.setText(student.getMatricula());
    specialty.setText(student.getEspecialidad());
    entryDate.setText(student.getFechaIngreso());
  }

  void clear() {
    name.clear(); control.clear(); specialty.clear(); entryDate.clear();
    formTitle.setText("Nuevo alumno");
  }
}
