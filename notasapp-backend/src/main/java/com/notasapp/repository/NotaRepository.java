package com.notasapp.repository;

import com.notasapp.model.Nota;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para la coleccion "notas".
 */
@Repository
public interface NotaRepository extends MongoRepository<Nota, String> {

    /**
     * Obtener todas las notas de un usuario ordenadas por fecha descendente
     */
    List<Nota> findByUsuarioIdOrderByFechaDesc(String usuarioId);

    /**
     * Buscar una nota por su ID y el ID del usuario propietario
     */
    Optional<Nota> findByIdAndUsuarioId(String id, String usuarioId);
}
