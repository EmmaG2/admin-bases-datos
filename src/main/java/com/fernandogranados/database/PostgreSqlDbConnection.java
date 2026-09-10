package com.fernandogranados.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

final class PostgreSqlDbConnection {
  private PostgreSqlDbConnection() {}

  static Connection connect(String user, String password) throws SQLException {
    return DriverManager.getConnection("jdbc:postgresql://postgres:5432/admin_bases_datos", user, password);
  }
}
