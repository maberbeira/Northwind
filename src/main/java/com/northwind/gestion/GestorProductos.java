package com.northwind.gestion;

import com.northwind.conexion.ConexionBD;
import com.northwind.esquema.Esquema;
import com.northwind.modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Operaciones sobre las tablas productos y productos_fav.
 */
public class GestorProductos {

    // La conexión la obtenemos siempre del Singleton
    private final Connection conexion;

    public GestorProductos() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }

    // Punto 2: inserta todos los productos de la lista. Devuelve cuántos se han insertado
    public int insertarProductos(List<Producto> productos) throws SQLException {
        // Cada ? es un hueco que rellenamos después con setXXX(posición, valor)
        String sql = "INSERT INTO " + Esquema.TAB_PRODUCTOS + " ("
                + Esquema.COL_PROD_ID + ", "
                + Esquema.COL_PROD_NOMBRE + ", "
                + Esquema.COL_PROD_DESCRIPCION + ", "
                + Esquema.COL_PROD_CANTIDAD + ", "
                + Esquema.COL_PROD_PRECIO + ") VALUES (?, ?, ?, ?, ?)";

        int insertados = 0;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (Producto p : productos) {
                ps.setInt(1, p.getId());
                ps.setString(2, p.getNombre());
                ps.setString(3, p.getDescripcion());
                ps.setInt(4, p.getCantidad());
                ps.setDouble(5, p.getPrecio());
                insertados += ps.executeUpdate(); // devuelve 1 si se insertó la fila
            }
        }
        System.out.println(insertados + " productos insertados en la tabla '" + Esquema.TAB_PRODUCTOS + "'");
        return insertados;
    }

    // Punto 4: muestra todos los productos (Statement + executeQuery)
    public void mostrarTodosLosProductos() throws SQLException {
        String sql = "SELECT * FROM " + Esquema.TAB_PRODUCTOS
                + " ORDER BY " + Esquema.COL_PROD_ID;
        imprimirProductos("TODOS LOS PRODUCTOS", sql, false);
    }

    // Punto 5: muestra los productos con precio inferior al indicado (Statement + WHERE)
    public void mostrarProductosPrecioMenorQue(double precioMaximo) throws SQLException {
        String sql = "SELECT * FROM " + Esquema.TAB_PRODUCTOS
                + " WHERE " + Esquema.COL_PROD_PRECIO + " < " + precioMaximo
                + " ORDER BY " + Esquema.COL_PROD_PRECIO;
        imprimirProductos("PRODUCTOS CON PRECIO INFERIOR A " + precioMaximo + " €", sql, false);
    }

    // Punto 6: inserta en productos_fav los productos con precio superior al indicado
    // Usa INSERT ... SELECT: MySQL inserta directamente el resultado de la consulta
    public int insertarFavoritosPrecioMayorQue(double precioMinimo) throws SQLException {
        String sql = "INSERT INTO " + Esquema.TAB_PRODUCTOS_FAV
                + " (" + Esquema.COL_FAV_ID_PRODUCTO + ") "
                + "SELECT " + Esquema.COL_PROD_ID
                + " FROM " + Esquema.TAB_PRODUCTOS
                + " WHERE " + Esquema.COL_PROD_PRECIO + " > " + precioMinimo;

        int insertados;
        try (Statement stm = conexion.createStatement()) {
            insertados = stm.executeUpdate(sql); // devuelve el número de filas insertadas
        }
        System.out.println("\n" + insertados + " productos con precio superior a " + precioMinimo
                + " € insertados en '" + Esquema.TAB_PRODUCTOS_FAV + "'");
        return insertados;
    }

    // Punto 4 (2ª parte): muestra los productos favoritos con TODOS sus datos (incluida la descripción)
    // productos_fav solo guarda el id, así que hacemos un JOIN con productos
    public void mostrarFavoritos() throws SQLException {
        String sql = "SELECT p.* FROM " + Esquema.TAB_PRODUCTOS_FAV + " f "
                + "INNER JOIN " + Esquema.TAB_PRODUCTOS + " p "
                + "ON f." + Esquema.COL_FAV_ID_PRODUCTO + " = p." + Esquema.COL_PROD_ID
                + " ORDER BY p." + Esquema.COL_PROD_PRECIO + " DESC";
        imprimirProductos("PRODUCTOS FAVORITOS", sql, true);
    }

    // Ejecuta un SELECT sobre productos y los imprime en forma de tabla.
    // Si conDescripcion es true, imprime también la descripción debajo de cada producto
    private void imprimirProductos(String titulo, String sql, boolean conDescripcion) throws SQLException {
        System.out.println("\n========== " + titulo + " ==========");
        System.out.println(String.format("%-4s %-45s %10s %10s", "ID", "NOMBRE", "CANTIDAD", "PRECIO"));

        int total = 0;
        try (Statement stm = conexion.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            // rs.next() avanza a la siguiente fila; devuelve false cuando no quedan más
            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt(Esquema.COL_PROD_ID),
                        rs.getString(Esquema.COL_PROD_NOMBRE),
                        rs.getString(Esquema.COL_PROD_DESCRIPCION),
                        rs.getInt(Esquema.COL_PROD_CANTIDAD),
                        rs.getDouble(Esquema.COL_PROD_PRECIO)
                );
                System.out.println(p); // usa el toString() de Producto
                if (conDescripcion) {
                    System.out.println("     Descripción: " + p.getDescripcion());
                }
                total++;
            }
        }
        System.out.println("Total: " + total + " productos");
    }
}