package com.fernandogranados.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

final class MariaDbConnection {
  private MariaDbConnection() {}

  static Connection connect(String user, String password) throws SQLException {
    return DriverManager.getConnection(System.getenv().getOrDefault("MARIADB_URL", "jdbc:mariadb://mariadb:3306/admin_bases_datos"), user, password);
  }
}
