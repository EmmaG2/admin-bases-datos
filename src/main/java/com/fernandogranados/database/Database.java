package com.fernandogranados.database;

import com.fernandogranados.database.DatabaseFactory.DatabaseType;
import com.fernandogranados.exception.ErrorWithCode;
import java.sql.Connection;
import java.sql.SQLException;

public final class Database implements AutoCloseable {
  private final Connection connection;
  private final DatabaseType type;

  Database(DatabaseType type, Connection connection) {
    this.type = type;
    this.connection = connection;
  }

  @Override
  public void close() {
    try {
      connection.close();
    } catch (SQLException error) {
      throw new ErrorWithCode("DATABASE",
          "[" + type + "] No se pudo cerrar conexión. SQLState: " + error.getSQLState(), error);
    }
  }
}
