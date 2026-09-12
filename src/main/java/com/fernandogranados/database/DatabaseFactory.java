package com.fernandogranados.database;

import com.fernandogranados.exception.ErrorWithCode;
import java.sql.Connection;
import java.sql.SQLException;

public final class DatabaseFactory {
  public enum DatabaseType { MYSQL, POSTGRESQL, MARIADB }

  private DatabaseFactory() {}

  public static Connection create(DatabaseType type) {
    if (type == null) throw new ErrorWithCode("INVALID_DATABASE", "Selecciona un motor de base de datos.");
    String user = System.getenv().getOrDefault("DB_USER", "app");
    String password = System.getenv("DB_PASSWORD");
    if (password == null || password.isBlank()) {
      throw new ErrorWithCode("CONFIGURATION", "Define DB_PASSWORD.");
    }
    try {
      return switch (type) {
        case MYSQL -> MySqlDbConnection.connect(user, password);
        case POSTGRESQL -> PostgreSqlDbConnection.connect(user, password);
        case MARIADB -> MariaDbConnection.connect(user, password);
      };
    } catch (SQLException error) {
      throw new ErrorWithCode("CONNECTION", "No se pudo conectar a " + type + ". Revisa el servicio y las credenciales.", error);
    }
  }
}
