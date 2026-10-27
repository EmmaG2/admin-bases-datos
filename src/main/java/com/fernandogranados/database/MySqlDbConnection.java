package com.fernandogranados.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

final class MySqlDbConnection {
  private MySqlDbConnection() {}

  static Connection connect(String user, String password) throws SQLException {
    return DriverManager.getConnection(System.getenv().getOrDefault("MYSQL_URL", "jdbc:mysql://db:3306/admin_bases_datos?sslMode=REQUIRED"), user, password);
  }
}
