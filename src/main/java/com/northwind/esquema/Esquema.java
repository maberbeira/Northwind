package com.northwind.esquema;

/**
 * Esquema de la base de datos "almacen".
 * Centraliza los nombres de la base de datos, tablas y columnas
 * para no escribirlos a mano en cada consulta SQL.
 * Todas las variables de una interfaz son public static final (constantes).
 */
public interface Esquema {

    // ---------- Base de datos ----------
    String BD = "almacen";

    // ---------- Tabla productos ----------
    String TAB_PRODUCTOS = "productos";
    String COL_PROD_ID = "id";
    String COL_PROD_NOMBRE = "nombre";
    String COL_PROD_DESCRIPCION = "descripcion";
    String COL_PROD_CANTIDAD = "cantidad";
    String COL_PROD_PRECIO = "precio";

    // ---------- Tabla productos_fav ----------
    String TAB_PRODUCTOS_FAV = "productos_fav";
    String COL_FAV_ID = "id";
    String COL_FAV_ID_PRODUCTO = "id_producto";

    // ---------- Tabla empleados ----------
    String TAB_EMPLEADOS = "empleados";
    String COL_EMP_ID = "id";
    String COL_EMP_NOMBRE = "nombre";
    String COL_EMP_APELLIDOS = "apellidos";
    String COL_EMP_CORREO = "correo";

    // ---------- Tabla pedidos ----------
    String TAB_PEDIDOS = "pedidos";
    String COL_PED_ID = "id";
    String COL_PED_ID_PRODUCTO = "id_producto";
    String COL_PED_ID_EMPLEADO = "id_empleado";
    String COL_PED_CANTIDAD = "cantidad";
}