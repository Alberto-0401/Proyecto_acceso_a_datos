package com.notasapp.repository;

import com.notasapp.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de acceso a datos para la coleccion "usuarios".
 */
@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    /**
     * Buscar usuario por username
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Buscar usuario por email
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Comprobar si existe un usuario con ese username
     */
    boolean existsByUsername(String username);

    /**
     * Comprobar si existe un usuario con ese email
     */
    boolean existsByEmail(String email);
}
