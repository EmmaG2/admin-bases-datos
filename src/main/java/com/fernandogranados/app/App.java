package com.fernandogranados.app;

import com.fernandogranados.database.Database;
import com.fernandogranados.database.DatabaseFactory;
import com.fernandogranados.database.DatabaseFactory.DatabaseType;
import com.fernandogranados.exception.ErrorWithCode;
import com.fernandogranados.model.Alumno;
import com.fernandogranados.model.Calificaciones;
import com.fernandogranados.model.Materia;
import com.fernandogranados.repository.CrudRepository;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Function;

public final class App {
  public static void main(String[] args) {
    try {
      run(args);
    } catch (ErrorWithCode error) {
      System.err.println("[" + error.getCode() + "] " + error.getMessage());
      System.exit(1);
    } catch (IllegalArgumentException error) {
      System.err.println("[INVALID_INPUT] Revisa el motor, los números y sus rangos. Ejecuta help para ver el uso.");
      System.exit(1);
    }
  }

  private static void run(String[] args) {
    if (args.length == 1 && args[0].equals("help")) {
      System.out.println("""
          Uso: <MYSQL|POSTGRESQL|MARIADB> <alumnos|materias|calificaciones> <operación> [campos]
            create <campos>       Crea un registro y devuelve su ID.
            list                  Lista los registros.
            read <id>             Consulta un registro.
            update <id> <campos>  Reemplaza sus campos.
            delete <id>           Elimina un registro.
          Campos por tabla (en este orden):
            alumnos: nombre matricula especialidad fechaIngreso
            materias: nombre creditos especialidad
            calificaciones: idAlumno idMateria calificacion periodo
          Usa comillas para campos con espacios. Calificación: 0–100; créditos: 0–127; periodo: 1–32767.
          Sin argumentos: conecta a los tres motores y muestra sus registros.
          """);
      return;
    }
    if (args.length == 0) {
      for (DatabaseType type : DatabaseType.values()) {
        try (Database database = DatabaseFactory.create(type)) {
          System.out.println("[" + type + "] Conectado a admin_bases_datos");
          System.out.println("Alumnos: " + database.alumnos().findAll());
          System.out.println("Materias: " + database.materias().findAll());
          System.out.println("Calificaciones: " + database.calificaciones().findAll());
        }
        System.out.println("[" + type + "] Desconectado de admin_bases_datos");
      }
      return;
    }
    if (args.length < 3)
      throw usage();
    DatabaseType type = DatabaseType.valueOf(args[0].toUpperCase(Locale.ROOT));
    int fields = switch (args[1]) {
      case "alumnos", "calificaciones" -> 4;
      case "materias" -> 3;
      default -> throw usage();
    };
    int expected = switch (args[2]) {
      case "list" -> 3;
      case "read", "delete" -> 4;
      case "create" -> 3 + fields;
      case "update" -> 4 + fields;
      default -> throw usage();
    };
    if (args.length != expected)
      throw usage();
    try (Database database = DatabaseFactory.create(type)) {
      System.out.println("Destino: [" + database.type() + "] admin_bases_datos / " + args[1] + " / " + args[2]);
      switch (args[1]) {
        case "alumnos" -> execute(database.alumnos(), args,
            values -> new Alumno(0, values[0], values[1], values[2], values[3]));
        case "materias" -> execute(database.materias(), args,
            values -> new Materia(0, values[0], Byte.parseByte(values[1]), values[2]));
        case "calificaciones" -> execute(database.calificaciones(), args,
            values -> new Calificaciones(0, Integer.parseInt(values[0]), Integer.parseInt(values[1]),
                Short.parseShort(values[2]), Short.parseShort(values[3])));
        default -> throw usage();
      }
    }
    System.out.println("[" + type + "] Desconectado de admin_bases_datos");
  }

  private static <T> void execute(CrudRepository<T> repository, String[] args, Function<String[], T> parse) {
    switch (args[2]) {
      case "create" -> {
        int id = repository.create(parse.apply(Arrays.copyOfRange(args, 3, args.length)));
        System.out.println("Guardado en [" + args[0].toUpperCase(Locale.ROOT) + "] con ID " + id);
        System.out.println(repository.findById(id).orElseThrow(App::notFound));
      }
      case "list" -> repository.findAll().forEach(System.out::println);
      case "read" -> System.out.println(repository.findById(Integer.parseInt(args[3])).orElseThrow(App::notFound));
      case "update" -> {
        int id = Integer.parseInt(args[3]);
        if (!repository.update(id, parse.apply(Arrays.copyOfRange(args, 4, args.length))))
          throw notFound();
        System.out.println("Actualizado: " + repository.findById(id).orElseThrow(App::notFound));
      }
      case "delete" -> {
        if (!repository.delete(Integer.parseInt(args[3])))
          throw notFound();
        System.out.println("Eliminado: " + args[3]);
      }
      default -> throw usage();
    }
  }

  private static ErrorWithCode usage() {
    return new ErrorWithCode("INVALID_INPUT", "Argumentos inválidos. Ejecuta help para ver el uso.");
  }

  private static ErrorWithCode notFound() {
    return new ErrorWithCode("NOT_FOUND", "El registro no existe en la base seleccionada.");
  }
}
