# Conexión a bases de datos

Aplicación Java 21 que abre y cierra conexiones JDBC a MySQL 9.7.2 LTS,
PostgreSQL 18.6 y MariaDB 12.3.3 LTS. No crea tablas ni consulta o modifica registros.

## Funcionamiento

1. `App` recorre los motores en orden: MySQL, PostgreSQL y MariaDB.
2. `DatabaseFactory.create(type)` valida el motor y obtiene las credenciales
   del entorno. `DB_USER` usa `app` por defecto; `DB_PASSWORD` es obligatoria
   y no puede estar vacía.
3. La factory delega en `MySqlDbConnection`, `PostgreSqlDbConnection` o
   `MariaDbConnection`. Cada clase define su URL y abre la conexión con
   `DriverManager.getConnection(url, user, password)`.
4. La factory devuelve directamente una `java.sql.Connection`.
5. `App` muestra la conexión exitosa. Al salir de `try-with-resources`, Java
   llama automáticamente a `Connection.close()`, incluso si el bloque falla.
   La confirmación de desconexión se imprime solo después de cerrar correctamente.

Las clases de conexión son internas al paquete `database`. El punto de entrada
es `DatabaseFactory`; quien obtiene una conexión debe cerrarla usando
`try-with-resources`.

| Motor      | Clase                    | Dirección dentro de Docker |
| ---------- | ------------------------ | -------------------------- |
| MySQL      | `MySqlDbConnection`      | `db:3306`                  |
| PostgreSQL | `PostgreSqlDbConnection` | `postgres:5432`            |
| MariaDB    | `MariaDbConnection`      | `mariadb:3306`             |

Todas usan la base `admin_bases_datos`. MySQL exige TLS con
`sslMode=REQUIRED`. Las URLs apuntan a los servicios de `compose.yaml`.

## Ejecutar

Solo necesitas Docker con Docker Compose (por ejemplo, Docker Desktop).
No necesitas instalar Nix, Make, Java ni Maven en tu máquina.

Después de clonar el repositorio, abre una terminal en su carpeta. Si ya existe
`.env`, conserva sus credenciales. En una instalación nueva, crea ese archivo
con tu editor y define una contraseña propia, larga y sin espacios:

```dotenv
DB_PASSWORD=reemplaza_esto_por_tu_contrasena
```

```sh
docker compose up --build -d
docker compose logs app
```

Docker descarga la imagen `nixos/nix:2.35.2` para compilar la aplicación.
Dentro de ella, `nix-shell` carga `shell.nix` e instala las herramientas declaradas,
incluidos Java 21 y Maven. Maven genera el JAR y descarga los conectores JDBC.
La etapa final copia esos archivos a una imagen con Java 21 para ejecutarlos.
Nix y las herramientas de compilación quedan dentro de la etapa de construcción.
La primera compilación necesita Internet y puede tardar varios minutos;
Docker reutiliza la capa del entorno mientras `shell.nix` no cambie.

Docker inicializa cada motor con su base y credenciales. La aplicación se ejecuta
sin argumentos, abre y cierra cada conexión y termina.

Salida esperada con los tres motores disponibles:

```text
[MYSQL] Conectado a admin_bases_datos
[MYSQL] Desconectado de admin_bases_datos
[POSTGRESQL] Conectado a admin_bases_datos
[POSTGRESQL] Desconectado de admin_bases_datos
[MARIADB] Conectado a admin_bases_datos
[MARIADB] Desconectado de admin_bases_datos
```

Para repetir la ejecución con los servicios en marcha:

```sh
docker compose run --rm app
```

## Operaciones desde Java

La conexión se obtiene con `DatabaseFactory.create(...)`. El mismo código JDBC
sirve para los tres motores; cambia el valor de `DatabaseType`:

| Base | Conexión |
| --- | --- |
| MySQL | `DatabaseFactory.create(DatabaseType.MYSQL)` |
| PostgreSQL | `DatabaseFactory.create(DatabaseType.POSTGRESQL)` |
| MariaDB | `DatabaseFactory.create(DatabaseType.MARIADB)` |

Las consultas SQL van dentro del código Java. Los valores se envían mediante
parámetros de `PreparedStatement`, sin concatenar entradas del usuario.

### Ejemplo completo

Este ejemplo didáctico crea una tabla de práctica, inserta un registro, lo
consulta, lo actualiza y lo elimina usando una única conexión. La tabla queda
vacía al terminar. Usa una base de práctica: si la tabla ya existe, la creación
falla para evitar modificar datos anteriores.

Para probarlo, puedes reemplazar temporalmente el contenido de
`src/main/java/com/fernandogranados/app/App.java` por este ejemplo. El código
del repositorio sigue dedicado únicamente a conectar y desconectar.

```java
package com.fernandogranados.app;

import com.fernandogranados.database.DatabaseFactory;
import com.fernandogranados.database.DatabaseFactory.DatabaseType;
import com.fernandogranados.exception.ErrorWithCode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class App {
  public static void main(String[] args) {
    DatabaseType type = DatabaseType.MYSQL;

    try (Connection connection = DatabaseFactory.create(type)) {
      try (Statement statement = connection.createStatement()) {
        statement.executeUpdate("""
            CREATE TABLE ejemplo_java (
              id INTEGER PRIMARY KEY,
              nombre VARCHAR(100) NOT NULL
            )
            """);
      }

      try (PreparedStatement insert = connection.prepareStatement(
          "INSERT INTO ejemplo_java (id, nombre) VALUES (?, ?)")) {
        insert.setInt(1, 1);
        insert.setString(2, "Ana");
        System.out.println("Insertados: " + insert.executeUpdate());
      }

      try (PreparedStatement select = connection.prepareStatement(
          "SELECT id, nombre FROM ejemplo_java WHERE id = ?")) {
        select.setInt(1, 1);
        try (ResultSet rows = select.executeQuery()) {
          while (rows.next()) {
            System.out.println(rows.getInt("id") + ": " + rows.getString("nombre"));
          }
        }
      }

      try (PreparedStatement update = connection.prepareStatement(
          "UPDATE ejemplo_java SET nombre = ? WHERE id = ?")) {
        update.setString(1, "Ana García");
        update.setInt(2, 1);
        System.out.println("Actualizados: " + update.executeUpdate());
      }

      try (PreparedStatement delete = connection.prepareStatement(
          "DELETE FROM ejemplo_java WHERE id = ?")) {
        delete.setInt(1, 1);
        System.out.println("Eliminados: " + delete.executeUpdate());
      }
    } catch (SQLException error) {
      throw new ErrorWithCode("DATABASE",
          "No se pudo completar el ejemplo en " + type + ".", error);
    }
  }
}
```

Para PostgreSQL cambia la asignación a `DatabaseType.POSTGRESQL`; para MariaDB
usa `DatabaseType.MARIADB`. Cada motor tiene datos independientes.

Después de guardar el ejemplo, compílalo y ejecútalo dentro de Docker:

```sh
docker compose up -d db postgres mariadb
docker compose build app
docker compose run --rm app
```

Espera a que los motores estén disponibles antes de ejecutar la aplicación.
Compose proporciona las credenciales de `.env`. No necesitas Java ni Maven
instalados en tu máquina. Al terminar la práctica, restaura el `App.java`
original y vuelve a compilar la imagen.

### Cómo funcionan las operaciones

- `executeUpdate()` ejecuta la creación de la tabla y las escrituras; en
  `INSERT`, `UPDATE` y `DELETE` devuelve el número de filas afectadas.
- `executeQuery()` devuelve un `ResultSet`; `next()` recorre los resultados.
- `setInt` y `setString` asignan los parámetros `?`, numerados desde 1.
- `try-with-resources` cierra resultados, sentencias y conexión incluso ante errores.

Las operaciones del ejemplo se confirman individualmente por el modo
`autoCommit` inicial. Si una operación posterior falla, las anteriores permanecen.
Los IDs se asignan manualmente para que el ejemplo sirva en los tres motores.
Mantén el `WHERE` en actualizaciones y eliminaciones para limitar las filas afectadas.

### Eliminar la tabla de práctica desde Java

Para repetir el ejemplo en el mismo motor, primero elimina únicamente su tabla
de práctica. Este fragmento se coloca dentro de un método Java y elimina también
cualquier registro que permanezca en esa tabla:

```java
try (Connection connection = DatabaseFactory.create(DatabaseType.MYSQL);
     Statement statement = connection.createStatement()) {
  statement.executeUpdate("DROP TABLE ejemplo_java");
} catch (SQLException error) {
  throw new ErrorWithCode("DATABASE", "No se pudo eliminar la tabla de práctica.", error);
}
```

Selecciona el mismo motor que utilizaste para crearla. Este paso es manual y
no forma parte del arranque normal de la aplicación.

Referencia: [JDBC PreparedStatement, Java 21](https://docs.oracle.com/en/java/javase/21/docs/api/java.sql/java/sql/PreparedStatement.html).

## Errores

`ErrorWithCode` identifica configuración incompleta (`CONFIGURATION`), motor
inválido (`INVALID_DATABASE`), apertura fallida (`CONNECTION`), cierre fallido
(`DISCONNECTION`) y argumentos no admitidos (`INVALID_INPUT`).
La aplicación muestra el código y un mensaje en español, termina con código 1
y no continúa con los motores restantes. Conserva la excepción JDBC como causa
sin imprimir las credenciales.

El servicio `app` usa `restart: on-failure`: Docker vuelve a ejecutarlo si una
base todavía está iniciando o ante otro fallo. Revisa los registros si persiste.

## Compilar y detener

Para compilar únicamente con Docker:

```sh
docker compose build app
```

El JAR y los conectores quedan dentro de la imagen. Si tienes Nix instalado,
también puedes compilar localmente de forma opcional:

```sh
nix-shell --run 'mvn -Dmaven.test.skip=true clean package'
```

El JAR queda en `target/admin-bases-datos-1.0.0-SNAPSHOT.jar` y los conectores
en `target/lib`. Para ejecutarlo necesita las variables de entorno y acceso
a los nombres internos de Docker; la ejecución habitual es mediante Compose.

```sh
docker compose down
```

Este comando detiene los servicios conservando sus volúmenes. La limpieza del
código no elimina las tablas ni los datos existentes.
