package com.northwind.gestion;

import com.northwind.conexion.ConexionBD;
import com.northwind.esquema.Esquema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

/**
 * Operaciones de estructura: crea la base de datos y sus tablas.
 */
public class GestorBD {

    // La conexión la obtenemos siempre del Singleton
    private final Connection conexion;

    public GestorBD() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }

    // Crea la base de datos "almacen" (borrándola antes si existía) y la selecciona
    public void crearBaseDatos() throws SQLException {
        try (Statement stm = conexion.createStatement()) {
            stm.execute("DROP DATABASE IF EXISTS " + Esquema.BD);
            stm.execute("CREATE DATABASE " + Esquema.BD);
            stm.execute("USE " + Esquema.BD);
            System.out.println("Base de datos '" + Esquema.BD + "' creada y seleccionada");
        }
    }

    // Crea las 4 tablas. Primero las que no tienen FK y después las que dependen de ellas
    public void crearTablas() throws SQLException {

        String sqlProductos = "CREATE TABLE " + Esquema.TAB_PRODUCTOS + " ("
                + Esquema.COL_PROD_ID + " INT PRIMARY KEY, "
                + Esquema.COL_PROD_NOMBRE + " VARCHAR(255) NOT NULL, "
                + Esquema.COL_PROD_DESCRIPCION + " TEXT, "
                + Esquema.COL_PROD_CANTIDAD + " INT, "
                + Esquema.COL_PROD_PRECIO + " DECIMAL(10,2))";

        String sqlEmpleados = "CREATE TABLE " + Esquema.TAB_EMPLEADOS + " ("
                + Esquema.COL_EMP_ID + " INT AUTO_INCREMENT PRIMARY KEY, "
                + Esquema.COL_EMP_NOMBRE + " VARCHAR(100) NOT NULL, "
                + Esquema.COL_EMP_APELLIDOS + " VARCHAR(150), "
                + Esquema.COL_EMP_CORREO + " VARCHAR(150))";

        String sqlFavoritos = "CREATE TABLE " + Esquema.TAB_PRODUCTOS_FAV + " ("
                + Esquema.COL_FAV_ID + " INT AUTO_INCREMENT PRIMARY KEY, "
                + Esquema.COL_FAV_ID_PRODUCTO + " INT NOT NULL UNIQUE, "
                + "FOREIGN KEY (" + Esquema.COL_FAV_ID_PRODUCTO + ") "
                + "REFERENCES " + Esquema.TAB_PRODUCTOS + "(" + Esquema.COL_PROD_ID + "))";

        String sqlPedidos = "CREATE TABLE " + Esquema.TAB_PEDIDOS + " ("
                + Esquema.COL_PED_ID + " INT AUTO_INCREMENT PRIMARY KEY, "
                + Esquema.COL_PED_ID_PRODUCTO + " INT NOT NULL, "
                + Esquema.COL_PED_ID_EMPLEADO + " INT NOT NULL, "
                + Esquema.COL_PED_CANTIDAD + " INT NOT NULL, "
                + "FOREIGN KEY (" + Esquema.COL_PED_ID_PRODUCTO + ") "
                + "REFERENCES " + Esquema.TAB_PRODUCTOS + "(" + Esquema.COL_PROD_ID + "), "
                + "FOREIGN KEY (" + Esquema.COL_PED_ID_EMPLEADO + ") "
                + "REFERENCES " + Esquema.TAB_EMPLEADOS + "(" + Esquema.COL_EMP_ID + "))";

        try (Statement stm = conexion.createStatement()) {
            stm.execute(sqlProductos);
            System.out.println("Tabla '" + Esquema.TAB_PRODUCTOS + "' creada");

            stm.execute(sqlEmpleados);
            System.out.println("Tabla '" + Esquema.TAB_EMPLEADOS + "' creada");

            stm.execute(sqlFavoritos);
            System.out.println("Tabla '" + Esquema.TAB_PRODUCTOS_FAV + "' creada");

            stm.execute(sqlPedidos);
            System.out.println("Tabla '" + Esquema.TAB_PEDIDOS + "' creada");
        }
    }


    // Muestra la estructura de una tabla usando los metadatos de la base de datos
    public void describirTabla(String tabla) throws SQLException {
        DatabaseMetaData dbmd = conexion.getMetaData();

        System.out.println("\n========== ESTRUCTURA DE LA TABLA '" + tabla + "' ==========");
        System.out.println(String.format("%-15s %-10s %8s %-6s %-8s",
                "COLUMNA", "TIPO", "TAMAÑO", "NULO", "AUTOINC"));

        // Columnas de la tabla
        try (ResultSet rs = dbmd.getColumns(Esquema.BD, null, tabla, null)) {
            while (rs.next()) {
                System.out.println(String.format("%-15s %-10s %8d %-6s %-8s",
                        rs.getString("COLUMN_NAME"),       // nombre de la columna
                        rs.getString("TYPE_NAME"),         // tipo (INT, VARCHAR...)
                        rs.getInt("COLUMN_SIZE"),          // tamaño máximo
                        rs.getString("IS_NULLABLE"),       // YES / NO
                        rs.getString("IS_AUTOINCREMENT")));// YES / NO
            }
        }

        // Clave primaria
        try (ResultSet rs = dbmd.getPrimaryKeys(Esquema.BD, null, tabla)) {
            while (rs.next()) {
                System.out.println("Clave primaria: " + rs.getString("COLUMN_NAME"));
            }
        }

        // Claves foráneas (columna de esta tabla -> tabla.columna a la que apunta)
        try (ResultSet rs = dbmd.getImportedKeys(Esquema.BD, null, tabla)) {
            while (rs.next()) {
                System.out.println("Clave foránea: " + rs.getString("FKCOLUMN_NAME")
                        + " -> " + rs.getString("PKTABLE_NAME")
                        + "." + rs.getString("PKCOLUMN_NAME"));
            }
        }
    }
}