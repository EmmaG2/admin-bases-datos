package com.fernandogranados.app;

import com.fernandogranados.ui.StudentController;
import java.util.Locale;
import javafx.application.Application;
import javafx.stage.Stage;

public final class StudentCrudApp extends Application {
  private final StudentController controller = new StudentController();

  @Override public void start(Stage stage) {
    controller.start(stage);
  }

  @Override public void stop() {
    controller.stop();
  }

  public static void main(String[] args) {
    Locale.setDefault(Locale.forLanguageTag("es-MX"));
    launch(args);
  }
}
