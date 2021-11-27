package com.egg.estancias.repositories;

import com.egg.estancias.entities.Cliente;
import com.egg.estancias.entities.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    @Query("SELECT c FROM Cliente c WHERE c.nombre = :nombre")
    public List<Cliente> buscarClientePorNombre(@Param("nombre") String nombre);

    @Query("SELECT c FROM Cliente c WHERE c.ciudad = :ciudad")
    public List<Cliente> buscarClientePorCiudad(@Param("ciudad") String ciudad);

    @Query("SELECT c FROM Cliente c WHERE c.pais = :pais")
    public List<Cliente> buscarClientePorPais(@Param("pais") String pais);

    @Query("SELECT c FROM Cliente c WHERE c.email = :email")
    public List<Cliente> buscarClientePorEmail(@Param("email") String email);

    @Query("SELECT c FROM Cliente c WHERE c.usuario = :usuario")
    public Cliente buscarClientePorUsuario(@Param("usuario") Usuario usuario);

    @Query("SELECT c FROM Cliente c ORDER BY c.nombre ASC")
    public List<Cliente> listarClientes();
}
