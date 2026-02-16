package com.notasapp.service;

import com.notasapp.model.Usuario;
import com.notasapp.repository.UsuarioRepository;
import com.notasapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Servicio de autenticacion (registro, login, perfil).
 */
@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * REGISTRAR UN NUEVO USUARIO
     */
    public Map<String, Object> registrar(Map<String, Object> datos) {
        String username = (String) datos.get("username");
        String email = (String) datos.get("email");
        String password = (String) datos.get("password");

        if (username == null || username.trim().isEmpty()) {
            return respuestaError("El nombre de usuario es requerido");
        }

        if (email == null || email.trim().isEmpty()) {
            return respuestaError("El email es requerido");
        }

        if (password == null || password.length() < 6) {
            return respuestaError("La contraseña debe tener al menos 6 caracteres");
        }

        if (usuarioRepository.existsByUsername(username)) {
            return respuestaError("El nombre de usuario ya está registrado");
        }

        if (usuarioRepository.existsByEmail(email)) {
            return respuestaError("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password));

        usuario = usuarioRepository.save(usuario);

        String token = jwtUtil.generarToken(usuario.getId(), usuario.getUsername(), false);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("success", true);
        respuesta.put("message", "Usuario registrado exitosamente");
        respuesta.put("token", token);
        respuesta.put("usuario", usuario);

        return respuesta;
    }

    /**
     * INICIAR SESION (LOGIN)
     */
    public Map<String, Object> login(Map<String, Object> datos) {
        String username = (String) datos.get("username");
        String password = (String) datos.get("password");
        boolean mantenerSesion = Boolean.TRUE.equals(datos.get("mantenerSesion"));

        if (username == null || username.trim().isEmpty()) {
            return respuestaError("El nombre de usuario es requerido");
        }

        if (password == null || password.isEmpty()) {
            return respuestaError("La contraseña es requerida");
        }

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElse(null);

        if (usuario == null) {
            return respuestaError("Credenciales inválidas");
        }

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            return respuestaError("Credenciales inválidas");
        }

        String token = jwtUtil.generarToken(usuario.getId(), usuario.getUsername(), mantenerSesion);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("success", true);
        respuesta.put("message", "Login exitoso");
        respuesta.put("token", token);
        respuesta.put("usuario", usuario);

        return respuesta;
    }

    /**
     * OBTENER PERFIL DEL USUARIO
     */
    public Usuario obtenerPerfil(String userId) {
        return usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private Map<String, Object> respuestaError(String mensaje) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("success", false);
        respuesta.put("message", mensaje);
        return respuesta;
    }
}
