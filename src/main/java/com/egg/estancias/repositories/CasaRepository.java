package com.egg.estancias.repositories;

import com.egg.estancias.entities.Casa;
import com.egg.estancias.entities.Familia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CasaRepository extends JpaRepository<Casa, String> {

    @Query("SELECT c FROM Casa c WHERE c.ciudad = :ciudad")
    public List<Casa> buscarCasaPorCiudad(@Param("ciudad") String ciudad);

    @Query("SELECT c FROM Casa c WHERE c.pais = :pais")
    public List<Casa> buscarCasaPorPais(@Param("pais") String pais);

    @Query("SELECT c FROM Casa c WHERE c.tipoVivienda = :tipoVivienda")
    public List<Casa> buscarCasaPorTipoDeVivienda(@Param("tipoVivienda") String tipoVivienda);

    @Query("SELECT c FROM Casa c WHERE c.precio BETWEEN :precioBajo AND :precioAlto")
    public List<Casa> buscarCasaPorPrecio(@Param("precioBajo") Double precioBajo, @Param("precioBajo") Double precioAlto);

    @Query("SELECT c FROM Casa c WHERE c.familia = :familia")
    public List<Casa> buscarCasaPorPropietario(@Param("familia") Familia familia);

    @Query("SELECT c FROM Casa c")
    public List<Casa> listarTodasLasCasas();
}
