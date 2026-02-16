package com.notasapp.controller;

import com.notasapp.model.Nota;
import com.notasapp.service.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para las operaciones CRUD de notas.
 */
@RestController
@RequestMapping("/api/notas")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500", "http://localhost:5501", "http://127.0.0.1:5501", "http://localhost:63342", "http://127.0.0.1:63342"})
public class NotaController {

    @Autowired
    private NotaService notaService;

    /**
     * GET /api/notas - Obtener todas las notas del usuario logueado
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerNotas(@RequestAttribute("userId") String userId) {
        List<Nota> notas = notaService.obtenerNotasPorUsuario(userId);
        return ResponseEntity.ok(respuestaExito("Notas obtenidas", notas));
    }

    /**
     * POST /api/notas - Crear una nueva nota
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearNota(
            @RequestAttribute("userId") String userId,
            @RequestBody Map<String, Object> datos) {
        try {
            Nota nota = notaService.crearNota(userId, datos);
            return ResponseEntity.ok(respuestaExito("Nota creada exitosamente", nota));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(respuestaError(e.getMessage()));
        }
    }

    /**
     * PUT /api/notas/{id} - Actualizar una nota existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarNota(
            @PathVariable String id,
            @RequestAttribute("userId") String userId,
            @RequestBody Map<String, Object> datos) {
        try {
            Nota nota = notaService.actualizarNota(id, userId, datos);
            return ResponseEntity.ok(respuestaExito("Nota actualizada exitosamente", nota));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(respuestaError(e.getMessage()));
        }
    }

    /**
     * DELETE /api/notas/{id} - Eliminar una nota
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarNota(
            @PathVariable String id,
            @RequestAttribute("userId") String userId) {
        try {
            notaService.eliminarNota(id, userId);
            return ResponseEntity.ok(respuestaExito("Nota eliminada exitosamente", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(respuestaError(e.getMessage()));
        }
    }

    private Map<String, Object> respuestaExito(String mensaje, Object data) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("success", true);
        respuesta.put("message", mensaje);
        if (data != null) {
            respuesta.put("data", data);
        }
        return respuesta;
    }

    private Map<String, Object> respuestaError(String mensaje) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("success", false);
        respuesta.put("message", mensaje);
        return respuesta;
    }
}
