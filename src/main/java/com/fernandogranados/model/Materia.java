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
    private String clave;
    private byte creditos;
    private String especialidad;

    public Materia() {}

    @Builder
    public Materia(int id, String nombre, String clave, byte creditos, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.creditos = creditos;
        this.especialidad = especialidad;
    }
}
