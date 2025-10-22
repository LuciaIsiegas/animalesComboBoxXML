package com.luciaia.cochecomboboxxml.base;

public class Animal {
    // Las clases con solo set get y toString
    private String especie;
    private String nombre;

    public Animal(String especie, String nombre) {
        this.especie = especie;
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getNombre() {
        return nombre;
    }

    public void setMatricula(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Animal{" + "especie='" + especie + ", nombre='" + nombre + '}';
    }
}
