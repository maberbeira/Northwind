# Northwind - Acceso a Datos (DAM2)

Actividad 1 del primer trimestre.

Aplicación Java (Maven) que:

1. Crea la base de datos `almacen` con las tablas `productos`, `productos_fav`, `empleados` y `pedidos`.
2. Muestra la estructura de las tablas mediante metadatos (`DatabaseMetaData`).
3. Descarga los productos de https://dummyjson.com/products (HTTPS + JSON con org.json) y los inserta.
4. Inserta empleados y pedidos mediante `Statement` (los pedidos tienen FK a productos y empleados).
5. Muestra todos los productos y los productos con precio inferior a 600 €.
6. Inserta en `productos_fav` los productos con precio superior a 1000 € y los muestra con un JOIN.

## Estructura

- `conexion/ConexionBD` – conexión a MySQL con patrón **Singleton**
- `esquema/Esquema` – nombres de la base de datos, tablas y columnas
- `modelo/Producto` – clase que representa un producto
- `api/ApiProductos` – descarga y lectura del JSON
- `gestion/GestorBD`, `GestorProductos`, `GestorPedidos` – operaciones con la base de datos

## Requisitos

- Java 24 y Maven
- MySQL 8 en ejecución

## Configuración

Copia `src/main/resources/db.properties.example` como `db.properties` en la misma carpeta
y pon tus datos de conexión. Después ejecuta la clase `Main`.