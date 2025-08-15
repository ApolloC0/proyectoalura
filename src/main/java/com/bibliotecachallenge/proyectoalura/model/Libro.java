package com.bibliotecachallenge.proyectoalura.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String idioma;
    private Integer descargas;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private AutorEntity autor;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    // Constructores
    public Libro() {}

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();

        // Para idiomas (usando .size() en lugar de .length)
        this.idioma = datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty() ?
                datosLibro.idiomas().get(0) : "en";

        this.descargas = datosLibro.descargas();

        // Para temas (usando .size() en lugar de .length)
        if (datosLibro.temas() != null && !datosLibro.temas().isEmpty()) {
            try {
                this.genero = Genero.fromString(datosLibro.temas().get(0));
            } catch (IllegalArgumentException e) {
                this.genero = Genero.FICCION;
            }
        } else {
            this.genero = Genero.FICCION;
        }
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public Integer getDescargas() { return descargas; }
    public void setDescargas(Integer descargas) { this.descargas = descargas; }
    public AutorEntity getAutor() { return autor; }
    public void setAutor(AutorEntity autor) { this.autor = autor; }
    public Genero getGenero() { return genero; }
    public void setGenero(Genero genero) { this.genero = genero; }

    @Override
    public String toString() {
        return "Libro{" +
                "titulo='" + titulo + '\'' +
                ", idioma='" + idioma + '\'' +
                ", descargas=" + descargas +
                ", autor=" + (autor != null ? autor.getNombre() : "Desconocido") +
                ", genero=" + genero +
                '}';
    }
}
