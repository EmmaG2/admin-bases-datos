package com.fernandogranados.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Materia {
    private int id;
    private String nombre;
    private byte creditos;
    private String especialidad;

    public Materia() {}

    @Builder
    public Materia(int id, String nombre, byte creditos, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.creditos = creditos;
        this.especialidad = especialidad;
    }
}
