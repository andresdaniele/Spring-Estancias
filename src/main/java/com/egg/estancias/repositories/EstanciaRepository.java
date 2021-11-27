package com.egg.estancias.repositories;

import com.egg.estancias.entities.Casa;
import com.egg.estancias.entities.Cliente;
import com.egg.estancias.entities.Estancia;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstanciaRepository extends JpaRepository<Estancia, String> {

    @Query("SELECT e FROM Estancia e WHERE e.cliente = :cliente")
    public List<Estancia> buscarEstanciaPorCliente(@Param("cliente") Cliente cliente);

    @Query("SELECT e FROM Estancia e WHERE e.casa = :casa")
    public List<Estancia> buscarEstanciaPorCasa(@Param("casa") Casa casa);

    @Query("SELECT e FROM Estancia e ORDER BY e.fechaDesde ASC")
    public List<Estancia> listarEstancias();

    @Query("SELECT e FROM Estancia e WHERE e.fechaDesde <= :fechaDesde AND e.fechaHasta <= :fechaHasta")
    public List<Estancia> listarEstanciasPorFechas(@Param("fechaDesde") Date fechaDesde, @Param("fechaHasta") Date fechaHasta);


}
