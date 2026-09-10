# Admin Bases de Datos

Java 21 con MySQL 8.4, PostgreSQL 17 y MariaDB 11.4. Cada motor tiene su propia
conexión JDBC, sus tablas y un volumen independiente.

## Ejecutar

Este entorno ya tiene las credenciales en `.env`. Para una instalación nueva:

```sh
(umask 077; test -f .env || printf 'DB_PASSWORD=%s\n' "$(openssl rand -hex 32)" > .env)
docker compose up --build -d
docker compose logs app
```

La aplicación conecta a los tres motores, crea las tablas si no existen, muestra
los registros y termina. Si un motor todavía está iniciando, Docker reinicia la
aplicación. El volumen MySQL existente se conserva. No regeneres `.env` con las
bases ya creadas; ese archivo está excluido de Git.

## Factory y CRUD

```java
import com.fernandogranados.database.Database;
import com.fernandogranados.database.DatabaseFactory;
import com.fernandogranados.database.DatabaseFactory.DatabaseType;
import com.fernandogranados.model.Alumno;
import com.fernandogranados.repository.CrudRepository;

try (Database db = DatabaseFactory.create(DatabaseType.POSTGRESQL)) {
  CrudRepository<Alumno> alumnos = db.alumnos();
  int id = alumnos.create(new Alumno(0, "Fernando", "A001", "IA", "Agosto 2026"));
  System.out.println("Guardado en " + db.type() + " con ID " + id);
}
```

Cambia `POSTGRESQL` por `MYSQL` o `MARIADB` para guardar en otra base.
`DatabaseFactory` valida las credenciales y delega la apertura a
`MySqlDbConnection`, `PostgreSqlDbConnection` o `MariaDbConnection`.
Cada clase define la URL de su motor y abre la conexión con `DriverManager`.

`try-with-resources` llama a `Database.close()` al salir del bloque, incluso si
una operación falla. Este método cierra la conexión JDBC y reporta los errores
de cierre mediante `ErrorWithCode`. La consola confirma la desconexión cuando
la operación y el cierre terminan correctamente.

`db.alumnos()`, `db.materias()` y `db.calificaciones()` ofrecen la interfaz
`CrudRepository<T>` con `create`, `findById`, `findAll`, `update` y `delete`.
`create` devuelve el ID generado; `update(id, entidad)` reemplaza sus campos;
`delete(id)` elimina el registro. Cada escritura se guarda inmediatamente.

## Operaciones por consola

El primer argumento selecciona el motor. Cada comando imprime dónde opera:

```sh
docker compose run --rm app POSTGRESQL alumnos create "Fernando" PG001 IA "Agosto 2026"
docker compose run --rm app POSTGRESQL alumnos list
docker compose run --rm app POSTGRESQL alumnos read 1
docker compose run --rm app POSTGRESQL alumnos update 1 "Fernando Granados" PG001 IA "Agosto 2026"
docker compose run --rm app POSTGRESQL alumnos delete 1
docker compose run --rm app help
```

Sustituye `1` por el ID devuelto al crear. Para las otras tablas, los campos son:

- `materias`: nombre, creditos, especialidad.
- `calificaciones`: idAlumno, idMateria, calificacion, periodo.

Las calificaciones requieren un alumno y una materia existentes en la misma base.
Elimina primero sus calificaciones para poder borrar esos registros.

`docker compose down` detiene los servicios conservando los datos.

Con Nix, `nix-shell` y `make build` compilan el JAR y los conectores en `target/lib`.
Las URLs actuales apuntan a los servicios internos de Docker.

Documentación oficial: [MySQL JDBC](https://dev.mysql.com/doc/connector-j/en/connector-j-reference-jdbc-url-format.html),
[PostgreSQL JDBC](https://jdbc.postgresql.org/documentation/use/) y
[MariaDB JDBC](https://github.com/mariadb-corporation/mariadb-connector-j).
