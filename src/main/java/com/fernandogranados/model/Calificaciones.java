package com.fernandogranados.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Calificaciones {
  private int id;
  private int idAlumno;
  private int idMateria;
  private short calificacion;
  private short periodo;

  public Calificaciones() {
  }

  @Builder
  public Calificaciones(int id, int idAlumno, int idMateria, short calificacion, short periodo) {
    this.id = id;
    this.idAlumno = idAlumno;
    this.idMateria = idMateria;
    this.calificacion = calificacion;
    this.periodo = periodo;
  }
}
