📚 Catálogo de Libros
Catálogo de Libros es una aplicación de consola desarrollada en Java con Spring Boot que permite gestionar un catálogo personal de libros consumiendo la API de Gutendex (basada en el Proyecto Gutenberg). Los usuarios pueden buscar, registrar y consultar información sobre libros y autores, almacenando los datos en una base de datos PostgreSQL.


🚀 Características
Búsqueda de libros: Integración con la API de Gutendex para encontrar libros por título

Registro de información: Almacena títulos, autores, idiomas y número de descargas

Gestión de autores: Consulta de información de autores con fechas de nacimiento/fallecimiento

Filtros avanzados:

Listado de libros por idioma (ES, EN, FR, PT)

Autores vivos en un año específico

Persistencia de datos: Almacenamiento en PostgreSQL con relaciones entre libros y autores

Interfaz de consola interactiva: Menú intuitivo con manejo robusto de entradas

🛠️ Tecnologías Utilizadas
Java 17: Lenguaje principal

Spring Boot 3.5.4: Framework para la estructura de la aplicación

Spring Data JPA: Para el acceso a datos y mapeo objeto-relacional

PostgreSQL: Base de datos relacional

Jackson: Serialización/deserialización JSON

Maven: Gestión de dependencias y construcción

Java HTTP Client: Cliente HTTP nativo para consumo de APIs

📋 Funcionalidades
Menú Principal
Buscar libro por título: Búsqueda en API Gutendex y registro en base de datos

Listar libros registrados: Muestra todos los libros guardados

Listar autores:

Todos los autores registrados

Autores vivos en un año específico

Listar libros por idioma: Filtra por códigos de idioma (es, en, fr, etc.)

Salir

🏗️ Arquitectura del Proyecto
text
src/main/java/com/bibliotecachallenge/proyectoalura/
├── ProyectoaluraApplication.java       # Clase principal de Spring Boot
├── principal/
│   └── Principal.java                 # Lógica del menú y flujo de la aplicación
├── model/
│   ├── Autor.java                     # Record para autores de la API
│   ├── AutorEntity.java               # Entidad JPA para autores
│   ├── DatosLibro.java                # Record para datos de libros de la API
│   ├── Genero.java                    # Enum para géneros literarios
│   └── Libro.java                     # Entidad JPA para libros
├── repository/
│   ├── AutorRepository.java           # Repositorio JPA para autores
│   └── LibroRepository.java           # Repositorio JPA para libros
└── service/
    ├── BibliotecaApplication.java     # Servicio principal
    ├── ConsumoAPI.java                # Cliente para consumo de APIs
    └── ConvierteDatos.java            # Conversor de JSON a objetos
⚙️ Configuración e Instalación
Requisitos Previos
Java 17 o superior

PostgreSQL 12 o superior

Maven 3.8 o superior

Configuración de Base de Datos
Crea una base de datos en PostgreSQL:

sql
CREATE DATABASE biblioteca;
Configura las credenciales en src/main/resources/application.properties:

properties
spring.datasource.url=jdbc:postgresql://localhost:5432/biblioteca
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
Instalación y Ejecución
Clona el repositorio (o descarga el proyecto)

Navega al directorio del proyecto

Compila y ejecuta:

bash
mvn clean install
mvn spring-boot:run
🎯 Uso de la Aplicación
Al ejecutar la aplicación, se presenta un menú interactivo en consola:

text
--- Catálogo de Libros ---
1. Buscar libro por título
2. Listar libros registrados
3. Listar autores
4. Listar libros por idioma
0. Salir
Seleccione una opción: 
Ejemplos de Uso
Buscar un libro:

Selecciona opción 1

Ingresa el título (ej: "Pride")

La aplicación muestra la información y guarda automáticamente en la base de datos

Consultar autores vivos en 1600:

Selecciona opción 3

Elige la opción 2 (Autores vivos en año específico)

Ingresa el año: 1600

Ve la lista de autores que estaban vivos en ese año

🌟 Características Técnicas Destacadas
Manejo robusto de entradas: Validación de opciones del menú y años de consulta

Relaciones JPA optimizadas: Entre libros y autores

Consumo eficiente de API: Uso de Java HTTP Client

Mapeo inteligente de datos: Conversión de JSON a objetos Java

🔧 Configuración Adicional
Logging
Para reducir el logging de Hibernate, configura en application.properties:

properties
spring.jpa.show-sql=false
logging.level.org.hibernate.SQL=off
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=off
Base de Datos
La aplicación utiliza Hibernate con ddl-auto: update, por lo que las tablas se crean automáticamente. Para producción, considera usar migraciones controladas.
