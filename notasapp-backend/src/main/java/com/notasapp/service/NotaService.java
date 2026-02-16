package com.notasapp.service;

import com.notasapp.model.Nota;
import com.notasapp.repository.NotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Servicio con la logica de negocio para las operaciones CRUD de notas.
 */
@Service
public class NotaService {

    @Autowired
    private NotaRepository notaRepository;

    /**
     * OBTENER TODAS LAS NOTAS DE UN USUARIO
     */
    public List<Nota> obtenerNotasPorUsuario(String usuarioId) {
        return notaRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    /**
     * CREAR UNA NUEVA NOTA
     */
    public Nota crearNota(String usuarioId, Map<String, Object> datos) {
        String titulo = (String) datos.get("titulo");
        String contenido = (String) datos.get("contenido");

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new RuntimeException("El título es requerido");
        }

        if (contenido == null || contenido.trim().isEmpty()) {
            throw new RuntimeException("El contenido es requerido");
        }

        Nota nota = new Nota();
        nota.setUsuarioId(usuarioId);
        nota.setTitulo(titulo);
        nota.setContenido(contenido);

        return notaRepository.save(nota);
    }

    /**
     * ACTUALIZAR UNA NOTA EXISTENTE
     */
    public Nota actualizarNota(String notaId, String usuarioId, Map<String, Object> datos) {
        Nota nota = notaRepository.findByIdAndUsuarioId(notaId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada o no tienes permiso para editarla"));

        String titulo = (String) datos.get("titulo");
        String contenido = (String) datos.get("contenido");

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new RuntimeException("El título es requerido");
        }

        if (contenido == null || contenido.trim().isEmpty()) {
            throw new RuntimeException("El contenido es requerido");
        }

        nota.setTitulo(titulo);
        nota.setContenido(contenido);

        return notaRepository.save(nota);
    }

    /**
     * ELIMINAR UNA NOTA
     */
    public void eliminarNota(String notaId, String usuarioId) {
        Nota nota = notaRepository.findByIdAndUsuarioId(notaId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada o no tienes permiso para eliminarla"));

        notaRepository.delete(nota);
    }
}
