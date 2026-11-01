package com.northwind.gestion;

import com.northwind.conexion.ConexionBD;
import com.northwind.esquema.Esquema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Operaciones sobre las tablas empleados y pedidos (mediante Statement).
 */
public class GestorPedidos {

    // La conexión la obtenemos siempre del Singleton
    private final Connection conexion;

    public GestorPedidos() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }

    // Inserta una serie de empleados con Statement
    public void insertarEmpleados() throws SQLException {
        // Cada fila: {nombre, apellidos, correo}
        String[][] empleados = {
                {"Mari", "Berbeira Torres", "maberbeira@northwind.com"},
                {"Javier", "Esteban Camacho", "jaesteban@northwind.com"},
                {"Kim", "Castro Chacón", "kim.castro@northwind.com"},
                {"Ramón", "Álvarez Rodríguez", "ramon.alvarez@northwind.com"}
        };

        int insertados = 0;
        try (Statement stm = conexion.createStatement()) {
            for (String[] e : empleados) {
                // Los textos van entre comillas simples '...'
                String sql = "INSERT INTO " + Esquema.TAB_EMPLEADOS + " ("
                        + Esquema.COL_EMP_NOMBRE + ", "
                        + Esquema.COL_EMP_APELLIDOS + ", "
                        + Esquema.COL_EMP_CORREO + ") "
                        + "VALUES ('" + e[0] + "', '" + e[1] + "', '" + e[2] + "')";
                insertados += stm.executeUpdate(sql);
            }
        }
        System.out.println(insertados + " empleados insertados en la tabla '" + Esquema.TAB_EMPLEADOS + "'");
    }

    // Inserta una serie de pedidos con Statement. Cada pedido apunta (FK) a un producto y a un empleado
    public void insertarPedidos() throws SQLException {
        // Cada fila: {id_producto, id_empleado, cantidad}
        int[][] pedidos = {
                {1, 1, 3},
                {5, 2, 1},
                {12, 3, 2},
                {30, 1, 5},
                {78, 4, 1},
                {100, 2, 4}
        };

        int insertados = 0;
        try (Statement stm = conexion.createStatement()) {
            for (int[] p : pedidos) {
                // Los números van SIN comillas
                String sql = "INSERT INTO " + Esquema.TAB_PEDIDOS + " ("
                        + Esquema.COL_PED_ID_PRODUCTO + ", "
                        + Esquema.COL_PED_ID_EMPLEADO + ", "
                        + Esquema.COL_PED_CANTIDAD + ") "
                        + "VALUES (" + p[0] + ", " + p[1] + ", " + p[2] + ")";
                insertados += stm.executeUpdate(sql);
            }
        }
        System.out.println(insertados + " pedidos insertados en la tabla '" + Esquema.TAB_PEDIDOS + "'");
    }
}