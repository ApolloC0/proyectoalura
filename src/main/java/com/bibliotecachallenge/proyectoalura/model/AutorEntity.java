package com.bibliotecachallenge.proyectoalura.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "autores")
public class AutorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer anioNacimiento;
    private Integer anioFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    // Constructores
    public AutorEntity() {}

    public AutorEntity(Autor autor) {
        this.nombre = formatNombre(autor.nombre());
        this.anioNacimiento = autor.anioNacimiento();
        this.anioFallecimiento = autor.anioFallecimiento();
    }

    private String formatNombre(String nombre) {
        if (nombre == null) return "Anónimo";

        String[] partes = nombre.split(", ");
        if (partes.length > 1) {
            return partes[1] + " " + partes[0]; // Convertir "Austen, Jane" a "Jane Austen"
        }
        return nombre;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getAnioNacimiento() { return anioNacimiento; }
    public void setAnioNacimiento(Integer anioNacimiento) { this.anioNacimiento = anioNacimiento; }
    public Integer getAnioFallecimiento() { return anioFallecimiento; }
    public void setAnioFallecimiento(Integer anioFallecimiento) { this.anioFallecimiento = anioFallecimiento; }
    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }

    public boolean estabaVivoEn(Integer anio) {
        if (anioNacimiento == null) return false;
        if (anioFallecimiento == null) return anio >= anioNacimiento;
        return anio >= anioNacimiento && anio <= anioFallecimiento;
    }
}