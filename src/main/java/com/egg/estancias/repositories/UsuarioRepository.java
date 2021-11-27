package com.egg.estancias.repositories;

import com.egg.estancias.entities.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarUsuarioPorEmail(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.alias = :alias")
    public Usuario buscarUsuarioPorAlias(@Param("alias") String alias);

    @Query("SELECT u FROM Usuario u")
    public List<Usuario> listarUsuarios();

}
