package com.notasapp.controller;

import com.notasapp.model.Usuario;
import com.notasapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de autenticacion (registro, login, perfil).
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500", "http://localhost:5501", "http://127.0.0.1:5501", "http://localhost:63342", "http://127.0.0.1:63342"})
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * POST /api/auth/registro - Registrar nuevo usuario
     */
    @PostMapping("/registro")
    public ResponseEntity<Map<String, Object>> registro(@RequestBody Map<String, Object> datos) {
        Map<String, Object> response = authService.registrar(datos);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/login - Iniciar sesion
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> datos) {
        Map<String, Object> response = authService.login(datos);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/auth/perfil - Obtener perfil del usuario logueado
     */
    @GetMapping("/perfil")
    public ResponseEntity<Map<String, Object>> perfil(@RequestAttribute("userId") String userId) {
        Map<String, Object> respuesta = new HashMap<>();
        try {
            Usuario usuario = authService.obtenerPerfil(userId);
            respuesta.put("success", true);
            respuesta.put("message", "Perfil obtenido");
            respuesta.put("data", usuario);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            respuesta.put("success", false);
            respuesta.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(respuesta);
        }
    }
}
