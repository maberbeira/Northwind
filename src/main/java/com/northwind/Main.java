package com.northwind;

import com.northwind.api.ApiProductos;
import com.northwind.conexion.ConexionBD;
import com.northwind.gestion.GestorBD;
import com.northwind.gestion.GestorProductos;
import com.northwind.modelo.Producto;
import com.northwind.gestion.GestorPedidos;
import com.northwind.esquema.Esquema;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        ConexionBD bd = ConexionBD.getInstancia();

        try {
            // 1. Crear la base de datos y las tablas
            GestorBD gestorBD = new GestorBD();
            gestorBD.crearBaseDatos();
            gestorBD.crearTablas();


            // Extra: describir la estructura de las tablas mediante metadatos
            String[] tablas = {Esquema.TAB_PRODUCTOS, Esquema.TAB_EMPLEADOS,
                    Esquema.TAB_PRODUCTOS_FAV, Esquema.TAB_PEDIDOS};
            for (String tabla : tablas) {
                gestorBD.describirTabla(tabla);
            }

            // 2. Descargar los productos del JSON (HTTPS) e insertarlos
            ApiProductos api = new ApiProductos();
            List<Producto> productos = api.obtenerProductos();

            GestorProductos gestorProductos = new GestorProductos();
            gestorProductos.insertarProductos(productos);


            // 3. Insertar empleados y pedidos con Statement
            //    (después de los productos, porque pedidos tiene FK hacia productos)
            GestorPedidos gestorPedidos = new GestorPedidos();
            gestorPedidos.insertarEmpleados();
            gestorPedidos.insertarPedidos();


            // 4. Mostrar todos los productos
            gestorProductos.mostrarTodosLosProductos();


            // 5. Mostrar los productos con precio inferior a 600 €
            gestorProductos.mostrarProductosPrecioMenorQue(600);


            // 6. Insertar en productos_fav los productos con precio superior a 1000 €
            gestorProductos.insertarFavoritosPrecioMayorQue(1000);


            // 4 (2ª parte). Mostrar los productos favoritos con sus datos
            //    (se hace después de insertarlos; antes la tabla estaría vacía)
            gestorProductos.mostrarFavoritos();

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al descargar los productos: " + e.getMessage());
        } finally {
            // Se ejecuta siempre, haya error o no: cerramos la conexión
            bd.desconectar();
        }
    }
}