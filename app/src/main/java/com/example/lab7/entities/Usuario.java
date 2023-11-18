package com.example.lab7.entities;

public class Usuario {

    private String nombre;
    private Integer edad;

    public Usuario(String nombre, Integer edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public Usuario() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }
}
