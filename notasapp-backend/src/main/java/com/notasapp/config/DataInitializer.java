package com.notasapp.config;

import com.notasapp.model.Usuario;
import com.notasapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crea un usuario de prueba al arrancar la aplicacion si no existe.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        if (!usuarioRepository.existsByUsername("demo")) {
            Usuario usuarioDemo = new Usuario();
            usuarioDemo.setUsername("demo");
            usuarioDemo.setEmail("demo@notasapp.com");
            usuarioDemo.setPassword(passwordEncoder.encode("demo123"));

            usuarioRepository.save(usuarioDemo);
            System.out.println("Usuario de prueba creado: username=demo, password=demo123");
        } else {
            System.out.println("Usuario demo ya existe en la base de datos");
        }
    }
}
