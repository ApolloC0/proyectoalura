package com.bibliotecachallenge.proyectoalura.model;

public enum Genero {
    FICCION("Fiction"),
    NO_FICCION("Nonfiction"),
    POESIA("Poetry"),
    DRAMA("Drama"),
    ROMANCE("Romance"),
    CIENCIA_FICCION("Science Fiction"),
    FANTASIA("Fantasy");

    private String generoGutendex;

    Genero(String generoGutendex) {
        this.generoGutendex = generoGutendex;
    }

    public static Genero fromString(String text) {
        for (Genero genero : Genero.values()) {
            if (genero.generoGutendex.equalsIgnoreCase(text)) {
                return genero;
            }
        }
        throw new IllegalArgumentException("Ningún género encontrado: " + text);
    }
}
