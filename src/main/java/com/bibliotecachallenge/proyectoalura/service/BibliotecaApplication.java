package com.bibliotecachallenge.proyectoalura.service;

import com.bibliotecachallenge.proyectoalura.principal.Principal;
import com.bibliotecachallenge.proyectoalura.repository.AutorRepository;
import com.bibliotecachallenge.proyectoalura.repository.LibroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaApplication implements CommandLineRunner {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public BibliotecaApplication(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(libroRepository, autorRepository);
        principal.muestraElMenu();
    }
}