package com.northwind.conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestiona la conexión con MySQL aplicando el patrón Singleton:
 * solo existe una instancia de esta clase y, por tanto, una única conexión.
 */
public class ConexionBD {

    // 1) La única instancia de la clase (static: pertenece a la clase, no a un objeto)
    private static ConexionBD instancia;

    // La conexión con MySQL. Está protegida: solo se accede mediante métodos públicos
    private Connection conexion;

    // 2) Constructor privado: nadie puede hacer "new ConexionBD()" desde fuera
    private ConexionBD() {
    }

    // 3) Punto de acceso único: crea la instancia la primera vez y luego devuelve siempre la misma
    public static ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    // Si la conexión no está creada, la crea. En cualquier caso, la devuelve
    public Connection getConexion() {
        if (conexion == null) {
            conectar();
        }
        return conexion;
    }

    // Lee db.properties y abre la conexión. Privado: solo se llama desde dentro
    private void conectar() {
        // --- Leer el fichero de configuración ---
        Properties props = new Properties();
        try (InputStream entrada = ConexionBD.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (entrada == null) {
                throw new RuntimeException("No se encuentra el fichero db.properties en resources");
            }
            props.load(entrada);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer db.properties", e);
        }

        String host = props.getProperty("db.host");
        String puerto = props.getProperty("db.puerto");
        String usuario = props.getProperty("db.usuario");
        String password = props.getProperty("db.password");

        // --- Cadena de conexión ---
        // Sin nombre de base de datos al final: nos conectamos al servidor,
        // porque la BD "almacen" todavía no existe (la crea el programa)
        String url = "jdbc:mysql://" + host + ":" + puerto + "/"
                + "?useUnicode=true&characterEncoding=UTF-8";

        // --- Abrir la conexión ---
        try {
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("Conectado a MySQL en " + host + ":" + puerto);
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo conectar a MySQL: " + e.getMessage(), e);
        }
    }

    // Cierra la conexión si está abierta
    public void desconectar() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            } finally {
                conexion = null;
            }
        }
    }
}