package com.northwind.api;

import com.northwind.modelo.Producto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Descarga los productos de dummyjson (conexión HTTPS) y los convierte en objetos Producto.
 */
public class ApiProductos {

    // limit=0 -> devuelve TODOS los productos (sin él, solo devuelve 30)
    private static final String URL_PRODUCTOS = "https://dummyjson.com/products?limit=0";

    // Método público: descarga y convierte
    public List<Producto> obtenerProductos() throws IOException {
        String json = descargarJson(URL_PRODUCTOS);
        List<Producto> productos = convertirAProductos(json);
        System.out.println("Descargados " + productos.size() + " productos de " + URL_PRODUCTOS);
        return productos;
    }

    // Hace la petición HTTPS GET y devuelve la respuesta como texto
    private String descargarJson(String direccion) throws IOException {
        HttpURLConnection conexionHttp = (HttpURLConnection) URI.create(direccion).toURL().openConnection();
        conexionHttp.setRequestMethod("GET");
        conexionHttp.setRequestProperty("Accept", "application/json");

        // 200 = OK. Cualquier otro código significa que algo ha fallado
        int codigo = conexionHttp.getResponseCode();
        if (codigo != HttpURLConnection.HTTP_OK) {
            throw new IOException("El servidor respondió con el código HTTP " + codigo);
        }

        // Leemos la respuesta línea a línea, como un fichero de texto
        StringBuilder respuesta = new StringBuilder();
        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(conexionHttp.getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                respuesta.append(linea);
            }
        } finally {
            conexionHttp.disconnect();
        }
        return respuesta.toString();
    }

    // Recorre el JSON con org.json y crea un Producto por cada elemento del array "products"
    private List<Producto> convertirAProductos(String json) {
        List<Producto> productos = new ArrayList<>();

        JSONObject raiz = new JSONObject(json);                   // el objeto { ... } completo
        JSONArray arrayProductos = raiz.getJSONArray("products"); // el array [ ... ]

        for (int i = 0; i < arrayProductos.length(); i++) {
            JSONObject p = arrayProductos.getJSONObject(i);
            Producto producto = new Producto(
                    p.getInt("id"),
                    p.getString("title"),        // title       -> nombre
                    p.getString("description"),  // description -> descripcion
                    p.getInt("stock"),           // stock       -> cantidad
                    p.getDouble("price")         // price       -> precio
            );
            productos.add(producto);
        }
        return productos;
    }
}
