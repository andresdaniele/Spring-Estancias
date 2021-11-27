package com.egg.estancias.repositories;

import com.egg.estancias.entities.Familia;
import com.egg.estancias.entities.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface FamiliaRepository extends JpaRepository<Familia, String> {

    @Query("SELECT f FROM Familia f WHERE f.nombre = :nombre")
    public List<Familia> buscarFamiliaPorNombre(@Param("nombre") String nombre);

    @Query("SELECT f FROM Familia f WHERE f.email = :email")
    public List<Familia> buscarFamiliaPorEmail(@Param("email") String email);

    @Query("SELECT f FROM Familia f WHERE f.usuario = :usuario")
    public Familia buscarFamiliaPorUsuario(@Param("usuario") Usuario usuario);

    @Query("SELECT f FROM Familia f ORDER BY f.nombre ASC")
    public List<Familia> listarFamilias();

}
