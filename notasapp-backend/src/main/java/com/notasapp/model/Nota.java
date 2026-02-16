package com.notasapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

/**
 * Modelo que representa un documento en la coleccion "notas" de MongoDB.
 */
@Document(collection = "notas")
public class Nota {

    @Id
    private String id;

    @Indexed
    private String usuarioId;

    private String titulo;

    private String contenido;

    private LocalDateTime fecha;

    public Nota() {
        this.fecha = LocalDateTime.now();
    }

    public Nota(String usuarioId, String titulo, String contenido) {
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fecha = LocalDateTime.now();
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
