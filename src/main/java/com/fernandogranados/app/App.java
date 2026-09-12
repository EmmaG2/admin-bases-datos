package com.fernandogranados.app;

import com.fernandogranados.database.DatabaseFactory;
import com.fernandogranados.database.DatabaseFactory.DatabaseType;
import com.fernandogranados.exception.ErrorWithCode;
import java.sql.Connection;
import java.sql.SQLException;

public final class App {
  public static void main(String[] args) {
    try {
      if (args.length != 0) {
        throw new ErrorWithCode("INVALID_INPUT", "Ejecuta sin argumentos para conectar a los tres motores.");
      }
      for (DatabaseType type : DatabaseType.values()) {
        connectAndClose(type);
      }
    } catch (ErrorWithCode error) {
      System.err.println("[" + error.getCode() + "] " + error.getMessage());
      System.exit(1);
    }
  }

  private static void connectAndClose(DatabaseType type) {
    try (Connection connection = DatabaseFactory.create(type)) {
      System.out.println("[" + type + "] Conectado a admin_bases_datos");
    } catch (SQLException error) {
      throw new ErrorWithCode("DISCONNECTION", "No se pudo cerrar la conexión a " + type + ".", error);
    }
    System.out.println("[" + type + "] Desconectado de admin_bases_datos");
  }
}
