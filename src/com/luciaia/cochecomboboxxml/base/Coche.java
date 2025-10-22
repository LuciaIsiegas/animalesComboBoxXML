package com.luciaia.cochecomboboxxml.base;

public class Coche {
    // Las clases con solo set get y toString
    private String modelo;
    private String marca;

    public Coche(String modelo, String marca){
        this.modelo = modelo;
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMatricula(String marca) {
        this.marca = marca;
    }

    @Override
    public String toString() {
        return "Coche{" + "modelo='" + modelo + ", marca='" + marca + '}';
    }
}
