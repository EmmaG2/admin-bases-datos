package com.fernandogranados.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Alumno {
  private int id;
  private String nombre;
  private String matricula;
  private String especialidad;
  private String fechaIngreso;

  public Alumno() {
  }

  @Builder
  public Alumno(int id, String nombre, String matricula, String especialidad, String fechaIngreso) {
    this.id = id;
    this.nombre = nombre;
    this.matricula = matricula;
    this.especialidad = especialidad;
    this.fechaIngreso = fechaIngreso;
  }
}
