package com.egg.estancias.services;

import com.egg.estancias.entities.Casa;
import com.egg.estancias.entities.Cliente;
import com.egg.estancias.entities.Estancia;
import com.egg.estancias.errors.ServiceExceptions;
import com.egg.estancias.repositories.CasaRepository;
import com.egg.estancias.repositories.ClienteRepository;
import com.egg.estancias.repositories.EstanciaRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.DAYS;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstanciaService {

    @Autowired
    EstanciaRepository estanciaRepository;

    @Autowired
    CasaRepository casaRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Transactional
    public void crearEstancia(Date fechaDesde, Date fechaHasta, String idCliente, String idCasa) throws ServiceExceptions {

        verificarDatos(fechaDesde, fechaHasta, idCliente, idCasa);

        Estancia estancia = new Estancia();
        estancia.setFechaDesde(fechaDesde);
        estancia.setFechaHasta(fechaHasta);
        estancia.setCliente(clienteRepository.getById(idCliente));
        estancia.setCasa(casaRepository.getById(idCasa));

        estanciaRepository.save(estancia);
    }

    @Transactional
    public void modificarEstancia(String idEstancia, Date fechaDesde, Date fechaHasta, String idCliente, String idCasa) throws ServiceExceptions {

        verificarDatos(fechaDesde, fechaHasta, idCliente, idCasa);

        Optional<Estancia> respuestaEstancia = estanciaRepository.findById(idEstancia);
        if (respuestaEstancia.isPresent()) {
            Estancia estancia = respuestaEstancia.get();

            estancia.setFechaDesde(fechaDesde);
            estancia.setFechaHasta(fechaHasta);
            estancia.setCliente(clienteRepository.getById(idCliente));
            estancia.setCasa(casaRepository.getById(idCasa));

            estanciaRepository.save(estancia);
        } else {
            throw new ServiceExceptions("El Id ingresado no corresponde a una estancia registrada");
        }
    }

    public void verificarDatos(Date fechaDesde, Date fechaHasta, String idCliente, String idCasa) throws ServiceExceptions {

        Optional<Cliente> respuestaCliente = clienteRepository.findById(idCliente);
        if (!respuestaCliente.isPresent() || respuestaCliente.get() == null) {
            throw new ServiceExceptions("El cliente ingresado no se encuentra registrado o es nulo");
        }

        Optional<Casa> respuestaCasa = casaRepository.findById(idCasa);
        if (!respuestaCasa.isPresent() || respuestaCasa.get() == null) {
            throw new ServiceExceptions("La casa ingresada no se encuentra registrada o es nula");
        }

        LocalDate fecha1 = fechaDesde.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate fecha2 = fechaDesde.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long difDias = ChronoUnit.DAYS.between(fecha1, fecha2);

        if (difDias > respuestaCasa.get().getMaxDias() || difDias < respuestaCasa.get().getMinDias()) {
            throw new ServiceExceptions("Las fechas ingresadas no cumplen con el minimo o maximo de dias para el alquiler de la casa");
        }

        if (fechaDesde == null || fechaDesde.before(fechaHasta) || fechaDesde.after(fechaHasta)) {
            throw new ServiceExceptions("La fecha de inicio no puede ser nula, anterior a la fecha actual o posterior a la fecha final de la estancia");
        }

        if (fechaHasta == null) {
            throw new ServiceExceptions("La fecha final no puede ser nula");
        }
    }

    @Transactional(readOnly = true)
    public List<Estancia> buscarEstanciasEntreFechas(Date fechaDesde, Date fechaHasta) throws ServiceExceptions {

        if (fechaDesde == null || fechaDesde.before(new Date()) || fechaDesde.after(fechaHasta)) {
            throw new ServiceExceptions("La fecha de inicio no puede ser nula, anterior a la fecha actual o posterior a la fecha final");
        }

        if (fechaHasta == null || fechaHasta.before(new Date())) {
            throw new ServiceExceptions("La fecha final no puede ser nula o anterior a la fecha actual");
        }

        List<Estancia> estancias = estanciaRepository.listarEstanciasPorFechas(fechaDesde, fechaHasta);

        if (estancias.isEmpty() || estancias == null) {
            throw new ServiceExceptions("No se encontraron estancias entre las fechas indicadas");
        } else {
            return estancias;
        }
    }

    @Transactional(readOnly = true)
    public List<Estancia> buscarEstanciasPorCasa(Casa casa) throws ServiceExceptions {

        Optional<Casa> respuestaCasa = casaRepository.findById(casa.getId());

        if (!respuestaCasa.isPresent() || respuestaCasa.get() == null) {
            throw new ServiceExceptions("La casa ingresada no puede ser nula o no se encuentra registrada en el sistema");
        }

        List<Estancia> estancias = estanciaRepository.buscarEstanciaPorCasa(casa);

        if (estancias.isEmpty() || estancias == null) {
            throw new ServiceExceptions("Aun no hay estancias registradas en el sistema");
        } else {
            return estancias;
        }
    }

    @Transactional(readOnly = true)
    public List<Estancia> buscarEstanciasPorCliente(Cliente cliente) throws ServiceExceptions {

        Optional<Cliente> respuestaCliente = clienteRepository.findById(cliente.getId());

        if (!respuestaCliente.isPresent() || respuestaCliente.get() == null) {
            throw new ServiceExceptions("El cliente ingresado no puede ser nulo o no se encuentra registrado en el sistema");
        }

        List<Estancia> estancias = estanciaRepository.buscarEstanciaPorCliente(cliente);

        if (estancias.isEmpty() || estancias == null) {
            throw new ServiceExceptions("Aun no hay estancias registradas en el sistema");
        } else {
            return estancias;
        }
    }

    @Transactional(readOnly = true)
    public List<Estancia> listarTodasLasEstancias() throws ServiceExceptions {

        List<Estancia> estancias = estanciaRepository.listarEstancias();

        if (estancias.isEmpty()) {
            throw new ServiceExceptions("Aun no hay estancias registradas en el sistema");
        } else {
            return estancias;
        }
    }

}
