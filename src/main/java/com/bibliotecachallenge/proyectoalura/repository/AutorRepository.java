package com.bibliotecachallenge.proyectoalura.repository;

import com.bibliotecachallenge.proyectoalura.model.AutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<AutorEntity, Long> {
    AutorEntity findByNombre(String nombre);
}