package com.egg.estancias.repositories;

import com.egg.estancias.entities.Casa;
import com.egg.estancias.entities.Comentario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, String> {

    @Query("SELECT c FROM Comentario c WHERE c.casa = :casa")
    public List<Comentario> listarComentarioCasa(@Param("casa") Casa casa);

    @Query("SELECT c FROM Comentario c")
    public List<Comentario> listarTodosLosComentarios();

}
