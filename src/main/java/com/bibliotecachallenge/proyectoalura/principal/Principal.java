package com.bibliotecachallenge.proyectoalura.principal;

import com.bibliotecachallenge.proyectoalura.model.*;
import com.bibliotecachallenge.proyectoalura.repository.AutorRepository;
import com.bibliotecachallenge.proyectoalura.repository.LibroRepository;
import com.bibliotecachallenge.proyectoalura.service.ConsumoAPI;
import com.bibliotecachallenge.proyectoalura.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private List<DatosLibro> datosLibros = new ArrayList<>();

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            try {
                var menu = """
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores
                    4 - Listar libros por idioma
                    0 - Salir
                    """;
                System.out.println(menu);
                System.out.print("Seleccione una opción: ");

                String input = teclado.nextLine();

                // Validar que la entrada sea un número
                if (!input.matches("\\d+")) {
                    System.out.println("\nError: Debe ingresar un número válido (0-4)");
                    continue;
                }

                opcion = Integer.parseInt(input);

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        menuAutores();
                        break;
                    case 4:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("\nOpción inválida. Por favor ingrese un número entre 0 y 4");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nError: Debe ingresar un número válido (0-4)");
            } catch (Exception e) {
                System.out.println("\nOcurrió un error inesperado: " + e.getMessage());
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el título del libro que deseas buscar:");
        var tituloLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + tituloLibro.replace(" ", "+"));

        try {
            var response = conversor.obtenerDatos(json, GutendexResponse.class);

            if (response.results().isEmpty()) {
                System.out.println("No se encontraron libros con ese título.");
                return;
            }

            DatosLibro datosLibro = response.results().get(0);

            System.out.println("\nInformación del libro encontrado:");
            System.out.println("--------------------------------");
            System.out.println("Título: " + (datosLibro.titulo() != null ? datosLibro.titulo() : "Desconocido"));

            if (datosLibro.autores() != null && !datosLibro.autores().isEmpty()) {
                Autor autor = datosLibro.autores().get(0);
                System.out.println("Autor: " + formatNombreAutor(autor.nombre()));

                // Añadir información del año (si está disponible)
                if (autor.anioFallecimiento() != null) {
                    System.out.println("Aprox. año publicación: " + autor.anioFallecimiento());
                } else if (autor.anioNacimiento() != null) {
                    System.out.println("Aprox. año publicación: " + (autor.anioNacimiento() + 30)); // Estimación
                }
            } else {
                System.out.println("Autor: Desconocido");
            }

            if (datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty()) {
                System.out.println("Idioma: " + datosLibro.idiomas().get(0).toUpperCase());
            } else {
                System.out.println("Idioma: Desconocido");
            }

            System.out.println("Descargas: " + (datosLibro.descargas() != null ? datosLibro.descargas() : 0));

            guardarLibro(datosLibro);

        } catch (Exception e) {
            System.out.println("Error al procesar la respuesta: " + e.getMessage());
        }
    }

    private String formatNombreAutor(String nombre) {
        if (nombre == null) return "Anónimo";
        String[] partes = nombre.split(", ");
        if (partes.length > 1) {
            return partes[1] + " " + partes[0];
        }
        return nombre;
    }

    private void guardarLibro(DatosLibro datosLibro) {
        if (datosLibro.titulo() == null) {
            System.out.println("El libro no tiene título, no se puede guardar.");
            return;
        }

        // Verificar si el libro ya existe
        if (libroRepository.existsByTitulo(datosLibro.titulo())) {
            System.out.println("Este libro ya está registrado en la base de datos.");
            return;
        }

        Libro libro = new Libro();
        libro.setTitulo(datosLibro.titulo());
        libro.setDescargas(datosLibro.descargas() != null ? datosLibro.descargas() : 0);

        // Configurar idioma
        if (datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty()) {
            libro.setIdioma(datosLibro.idiomas().get(0));
        } else {
            libro.setIdioma("en"); // Idioma por defecto
        }

        // Configurar autor si existe
        if (datosLibro.autores() != null && !datosLibro.autores().isEmpty()) {
            Autor autorApi = datosLibro.autores().get(0);
            String nombreAutor = formatNombreAutor(autorApi.nombre());

            AutorEntity autor = autorRepository.findByNombre(nombreAutor);

            if (autor == null) {
                autor = new AutorEntity();
                autor.setNombre(nombreAutor);
                autor.setAnioNacimiento(autorApi.anioNacimiento());
                autor.setAnioFallecimiento(autorApi.anioFallecimiento());
                autor = autorRepository.save(autor);
            }

            libro.setAutor(autor);
        }

        // Configurar género
        if (datosLibro.temas() != null && !datosLibro.temas().isEmpty()) {
            try {
                libro.setGenero(Genero.fromString(datosLibro.temas().get(0)));
            } catch (IllegalArgumentException e) {
                libro.setGenero(Genero.FICCION); // Género por defecto
            }
        } else {
            libro.setGenero(Genero.FICCION);
        }

        libroRepository.save(libro);
        System.out.println("Libro registrado exitosamente en la base de datos.");
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
            return;
        }

        libros.forEach(libro -> {
            System.out.printf("Título: %s | Autor: %s | Idioma: %s | Descargas: %d | Género: %s%n",
                    libro.getTitulo(),
                    libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido",
                    libro.getIdioma().toUpperCase(),
                    libro.getDescargas(),
                    libro.getGenero());
        });
    }

    private void menuAutores() {
        System.out.println("""
                Opciones:
                1 - Listar todos los autores
                2 - Listar autores vivos en un año específico
                """);
        int subOpcion = teclado.nextInt();
        teclado.nextLine();

        switch (subOpcion) {
            case 1:
                listarAutores();
                break;
            case 2:
                listarAutoresVivos();
                break;
            default:
                System.out.println("Opción inválida");
        }
    }

    private void listarAutores() {
        List<AutorEntity> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
            return;
        }

        autores.forEach(autor -> {
            System.out.printf("Nombre: %s | Nacimiento: %d | Fallecimiento: %s | Libros: %d%n",
                    autor.getNombre(),
                    autor.getAnioNacimiento(),
                    autor.getAnioFallecimiento() != null ? autor.getAnioFallecimiento().toString() : "Presente",
                    autor.getLibros() != null ? autor.getLibros().size() : 0);
        });
    }

    private void listarAutoresVivos() {
        System.out.println("Ingrese el año para buscar autores vivos:");
        int anio = teclado.nextInt();
        teclado.nextLine();

        List<AutorEntity> autoresVivos = autorRepository.findAll().stream()
                .filter(autor -> autor.estabaVivoEn(anio))
                .toList();

        if (autoresVivos.isEmpty()) {
            System.out.printf("No hay autores registrados que estuvieran vivos en el año %d.%n", anio);
            return;
        }

        System.out.printf("Autores vivos en el año %d:%n", anio);
        autoresVivos.forEach(autor -> {
            System.out.printf("- %s (%d - %s)%n",
                    autor.getNombre(),
                    autor.getAnioNacimiento(),
                    autor.getAnioFallecimiento() != null ? autor.getAnioFallecimiento().toString() : "Presente");
        });
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Ingrese el código de idioma (ES, EN, FR, PT, etc.):");
        String idioma = teclado.nextLine().toLowerCase();

        List<Libro> libros = libroRepository.findByIdioma(idioma);

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en ese idioma.");
            return;
        }

        System.out.printf("Libros en idioma %s:%n", idioma.toUpperCase());
        libros.forEach(libro -> {
            System.out.printf("- %s de %s (%d descargas)%n",
                    libro.getTitulo(),
                    libro.getAutor() != null ? libro.getAutor().getNombre() : "Autor desconocido",
                    libro.getDescargas());
        });
    }
}
