# Admin Bases de Datos

`App.main` abre la interfaz JavaFX para administrar alumnos en MySQL mediante
`StudentRepository`. La interfaz permite crear, consultar, actualizar y eliminar
alumnos con matrículas de formato `2304XXXX`. El diseño usa fondo blanco,
acciones principales negras y estilos en `src/main/resources/styles/students.css`.
Las operaciones JDBC se ejecutan en segundo plano para mantener la interfaz disponible.

## Exportar boleta a PDF

En la pestaña **Calificaciones**, selecciona un alumno, espera a que cargue su boleta y pulsa
**Exportar PDF** para elegir dónde guardarla. Se exporta la boleta visible; usa
**Actualizar** antes si necesitas consultar cambios recientes. La consulta existente
muestra materias de Sistemas y calcula el promedio general de todas las materias
y periodos del alumno, sin ponderar. El PDF identifica ambos alcances y también
permite exportar una boleta sin calificaciones.

`StudentRepository` conserva la consulta SQL y devuelve `model.ReportCard`.
`pdf.ReportCardPdfExporter` recibe los datos ya consultados, sin conexiones JDBC
ni dependencia del repositorio; todo el código de iText está en el paquete `pdf`.
La exportación se ejecuta en segundo plano y genera primero un archivo temporal
para no sobrescribir una boleta existente si falla la generación.

Se usa [iText Core 9.8.0](https://kb.itextpdf.com/itext/release-itext-core-9-8-0),
mediante `com.itextpdf:layout` (incluye `kernel` e `io` transitivamente).
Antes de distribuir o usar en producción, revisa su
[licencia AGPL/comercial](https://itextpdf.com/how-buy/AGPLv3-license):
la dependencia no concede por sí sola una licencia comercial.

Java 21 con MySQL 8.4, PostgreSQL 17 y MariaDB 11.4. Cada motor tiene su propia
conexión JDBC, sus tablas y un volumen independiente.

## Ejecutar

Este entorno ya tiene las credenciales en `.env`. Para una instalación nueva:

```sh
(umask 077; test -f .env || printf 'DB_PASSWORD=%s\n' "$(openssl rand -hex 32)" > .env)
make db
make run
```

`make db` levanta MySQL, PostgreSQL y MariaDB con Docker Compose. `make run`
compila y ejecuta JavaFX mediante Maven (`compile javafx:run`) en la máquina local.
Se requiere Java 21 y un entorno gráfico; Maven descarga JavaFX 21 y las bibliotecas
nativas de tu plataforma. En Linux se requiere GTK 3. No se debe ejecutar este flujo con
`java -jar`: se debe usar `make run` para configurar los módulos JavaFX.
La interfaz conserva la conexión MySQL existente (`MYSQL_URL`, usuario `root`). No regeneres `.env`
con las bases ya creadas; ese archivo está excluido de Git.

## Responsabilidades de acceso a datos

La interfaz abre una conexión JDBC a MySQL y la entrega a `StudentRepository`,
que concentra el CRUD de alumnos y la consulta de la boleta de calificaciones.
Las tablas ya deben existir en el servidor: la aplicación no las crea ni inserta
datos iniciales automáticamente.

`Database` conserva únicamente el cierre de su conexión y el reporte de errores.
`DatabaseFactory` y los conectores por motor se conservan, pero la interfaz actual
no los utiliza. No ofrecen un segundo CRUD.

En MySQL, eliminar un alumno elimina permanentemente sus calificaciones mediante
`ON DELETE CASCADE`. Para eliminar una materia, elimina primero sus calificaciones.

### Migración de alumnos existentes en MySQL

Cambiar `mysql-academic-schema.sql` no actualiza tablas ya creadas. Aplica la migración
a la base que usa la aplicación (`segunda_unidad` en `make run`):

```sh
mysql -h 127.0.0.1 -P 3306 -u root -p segunda_unidad < sql/mysql-student-delete-cascade.sql
```

Aplica esta migración una sola vez: reemplaza `fk_calificaciones_alumno` por
`fk_calificaciones_alumno_cascade` conservando los registros existentes. Los borrados
posteriores de alumnos eliminan también sus calificaciones, sin posibilidad de
recuperarlas mediante rollback del esquema. Para desactivar la cascada, ejecuta
un `ALTER TABLE` que reemplace `fk_calificaciones_alumno_cascade` por una clave
sin `ON DELETE CASCADE`; esto no restaura datos eliminados.

`docker compose down` detiene los servicios conservando los datos.

Con Nix, `nix-shell` y `make build` compilan el JAR y los conectores en `target/lib`.
Las URLs de `make run` apuntan a los puertos locales publicados por Docker.

Documentación oficial: [MySQL JDBC](https://dev.mysql.com/doc/connector-j/en/connector-j-reference-jdbc-url-format.html),
[PostgreSQL JDBC](https://jdbc.postgresql.org/documentation/use/) y
[MariaDB JDBC](https://github.com/mariadb-corporation/mariadb-connector-j).

## CRUD con StudentRepository

`StudentRepository(Connection connection)` recibe una conexión JDBC abierta.
`create(Alumno student)` inserta un alumno y devuelve el ID generado;
`findById(int id)` y `findAll()` consultan alumnos; `update(int id, Alumno student)`
y `delete(int id)` devuelven si se modificó un registro.

Las consultas usan `PreparedStatement` y cierran sus sentencias automáticamente.
El llamador conserva el control de la conexión y la transacción. Los fallos SQL
se propagan como `SQLException`; los datos inválidos generan
`IllegalArgumentException`.


## Migración de la interfaz

La migración a JavaFX conserva `StudentRepository`, las validaciones, los mensajes
de error y la confirmación antes de eliminar. No modifica el esquema ni los datos.
Para volver a Swing, se deben revertir únicamente los cambios de la interfaz,
el CSS y la configuración de lanzamiento, sin realizar cambios en MySQL.

Referencia: [JavaFX con Maven](https://openjfx.io/openjfx-docs/#maven).
