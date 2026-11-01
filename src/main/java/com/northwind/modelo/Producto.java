package com.northwind.modelo;

/**
 * Representa un producto. Sus atributos coinciden con las columnas de la tabla productos.
 */
public class Producto {

    private int id;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private double precio;

    public Producto(int id, String nombre, String descripcion, int cantidad, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    // Cómo se muestra un producto al imprimirlo por consola
    @Override
    public String toString() {
        return String.format("%-4d %-45s %5d uds. %10.2f €", id, nombre, cantidad, precio);
    }
}
