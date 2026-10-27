# Student CRUD with Swing

Objective: Provide a local Swing application in `app` to create, list, update, and delete students in MySQL `segunda_unidad`.

Problem: Existing command-line and generic database paths do not expose the live MySQL `alumnos` schema through a visual CRUD interface.

Scope: Move the MySQL-specific JDBC CRUD into `app`, make `App.main` launch Swing, and point the default and `make run` MySQL URLs at `segunda_unidad`. Do not alter schema or seed data.

Constraints: Use `PreparedStatement` and `ResultSet`; target `alumnos(id,name,control,epecialidad,fecha_ingreso DATE)` from `sql/mysql-academic-schema.sql`; control format `2304XXXX`; preserve FK-protected grades; no new dependencies; no testing per project instructions.

Credential choice: At the user's explicit request after replacing the container and validating the connection, the Swing MySQL connection uses literal user `root` and password `1234`, ignoring `DB_USER` and `DB_PASSWORD`. These credentials are exposed in source code and require a code change if they rotate.

Authorized scope: Local source, Makefile, and feature-document changes only. User explicitly superseded the prior standalone-entrypoint scope. No remote operations or database mutations.

Acceptance: Running the app/JAR launches the Swing CRUD in `app` against MySQL `segunda_unidad`; a desktop user can create, list, select, update, and delete students with clear feedback and validation; existing rows are not changed on launch.

TDD: off (source: project AGENTS.md); test runner: N/A. Checks: `mvn -DskipTests package`; manual GUI and live database checks pending.

Route: delegated direct. Trigger: two non-trivial source files and reading that prepares a write.

Delivery: ask-on-risk. Forecast: ~340 authored changed lines; current new-source count before correction: 284 lines. Branch: `feat/student-crud-swing` intended but creation blocked by read-only `.git` refs. Engram mirror `odd/student-crud-swing/tasks`: pending (runtime session identity unavailable).

## Tasks
- [ ] T1: Add MySQL JDBC student CRUD with validation, prepared statements, result mapping, and FK-safe error reporting. Check: Maven package.
- [ ] T2: Add standalone Swing form/table with CRUD actions, selection, confirmation, refresh, and feedback. Check: Maven package; manual GUI pending.
- [ ] T3: Put JDBC CRUD in `app`, launch Swing from `App.main`, and set default/Makefile MySQL URL to `segunda_unidad`. Check: Java 21 compilation; manual GUI/live MySQL pending.
- [ ] T4: Use the user-specified literal `root`/`1234` credentials for the Swing MySQL connection. Check: Java 21 compilation; live authentication pending.

Progress: T1, T2, T3, and T4 source implemented; task closure pending GUI/live database checks and commit. All Java sources compiled successfully with Java 21 and local Lombok 1.18.46 on 2026-10-08 after correcting a pre-existing `System.getenv` invocation typo in `DatabaseFactory.java`. `mvn package` unavailable (`mvn: command not found`). Manual GUI/live DB check pending. User confirmed exact database name `segunda_unidad`; its live schema remains unverified because local port 3306 is unavailable. App/JAR entry now delegates to Swing instead of running the former seeding demo. RDD assessment unavailable (`gentle-ai` not installed); no review performed. No tests run per project instruction. Git branch creation and work-unit commit blocked by read-only `.git` refs; commit identity pending. Existing database data remains unchanged on launch. Engram mirror pending.
Next step: Run the desktop app against local MySQL, then package and commit once Maven and Git write access are available.
