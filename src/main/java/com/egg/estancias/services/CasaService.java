package com.egg.estancias.services;

import com.egg.estancias.entities.Casa;
import com.egg.estancias.entities.Familia;
import com.egg.estancias.errors.ServiceExceptions;
import com.egg.estancias.repositories.CasaRepository;
import com.egg.estancias.repositories.FamiliaRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.stereotype.Service;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CasaService {

    @Autowired
    FamiliaRepository familiaRepository;

    @Autowired
    CasaRepository casaRepository;

    @Transactional
    public void crearCasa(String calle, String codPostal, Integer numero, String ciudad, String pais,
            Date fechaDesde, Date fechaHasta, Integer minDias, Integer maxDias,
            Double precio, String tipoVivienda, String idFamilia) throws ServiceExceptions {

        verificarDatos(calle, codPostal, numero, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda, idFamilia);

        Casa casa = new Casa();

        casa.setCalle(calle);
        casa.setCodPostal(codPostal);
        casa.setNumero(numero);
        casa.setCiudad(ciudad);
        casa.setPais(pais);
        casa.setFechaDesde(fechaDesde);
        casa.setFechaHasta(fechaHasta);
        casa.setMinDias(minDias);
        casa.setMaxDias(maxDias);
        casa.setPrecio(precio);
        casa.setTipoVivienda(tipoVivienda);
        casa.setPropietarios(familiaRepository.getById(idFamilia));

        casaRepository.save(casa);
    }

    @Transactional
    public void modificarCasa(String idCasa, String calle, String codPostal, Integer numero, String ciudad, String pais,
            Date fechaDesde, Date fechaHasta, Integer minDias, Integer maxDias,
            Double precio, String tipoVivienda, String idFamilia) throws ServiceExceptions {

        verificarDatos(calle, codPostal, numero, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda, idFamilia);

        Optional<Casa> respuestaCasa = casaRepository.findById(idCasa);

        if (respuestaCasa.isPresent()) {
            Casa casa = respuestaCasa.get();

            casa.setCalle(calle);
            casa.setCodPostal(codPostal);
            casa.setNumero(numero);
            casa.setCiudad(ciudad);
            casa.setPais(pais);
            casa.setFechaDesde(fechaDesde);
            casa.setFechaHasta(fechaHasta);
            casa.setMinDias(minDias);
            casa.setMaxDias(maxDias);
            casa.setPrecio(precio);
            casa.setTipoVivienda(tipoVivienda);
            casa.setPropietarios(familiaRepository.getById(idFamilia));

            casaRepository.save(casa);
        } else {
            throw new ServiceExceptions("El Id ingresado no corresponde a una casa registrada");
        }

    }

    @Transactional
    public void eliminarCasa(String idCasa) throws ServiceExceptions {

        Optional<Casa> respuestaCasa = casaRepository.findById(idCasa);

        if (respuestaCasa.isPresent()) {
            casaRepository.deleteById(idCasa);
        } else {
            throw new ServiceExceptions("El id ingresado no corresponde a una casa registrad");
        }
    }

    public void verificarDatos(String calle, String codPostal, Integer numero, String ciudad, String pais,
            Date fechaDesde, Date fechaHasta, Integer minDias, Integer maxDias,
            Double precio, String tipoVivienda, String idFamilia) throws ServiceExceptions {

        if (calle == null || calle.trim().isEmpty()) {
            throw new ServiceExceptions("La calle no puede ser un dato nulo o vacio");
        }

        if (codPostal == null || codPostal.trim().isEmpty()) {
            throw new ServiceExceptions("El codigo postal no puede ser un dato nulo o vacio");
        }

        if (numero == null || numero <= 0) {
            throw new ServiceExceptions("El numero no puede ser nulo o negativo");
        }

        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new ServiceExceptions("La ciudad no puede ser un dato nulo o vacio");
        }

        if (pais == null || pais.trim().isEmpty()) {
            throw new ServiceExceptions("El pais no puede ser un dato nulo o vacio");
        }

        if (fechaDesde == null || fechaDesde.before(new Date())) {
            throw new ServiceExceptions("La fecha de inicio de alquiler no puede ser nula o anterior a la fecha actual");
        }

        if (fechaHasta == null || fechaHasta.before(new Date()) || fechaHasta.before(new Date())) {
            throw new ServiceExceptions("La fecha de inicio de alquiler no puede ser nula o anterior a la fecha actual");
        }

        LocalDate fecha1 = fechaDesde.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fecha2 = fechaDesde.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long difDias = DAYS.between(fecha1, fecha2);

        if (minDias == null || minDias <= 0 || minDias < difDias) {
            throw new ServiceExceptions("El numero minimo de dias no puede ser nulo o negativo. Ademas el minimo de dias no puede ser menor a la diferencias de dias entre las dos fechas");
        }
        if (maxDias == null || maxDias <= 0 || maxDias > difDias) {
            throw new ServiceExceptions("El numero maximo de dias no puede ser nulo o negativo. Ademas el maximo de dias no puede ser mayor a la diferencias de dias entre las dos fechas");
        }

        if (precio == null || numero <= 0) {
            throw new ServiceExceptions("El precio no puede ser nulo o negativo");
        }
        if (tipoVivienda == null || tipoVivienda.trim().isEmpty()) {
            throw new ServiceExceptions("La calle no puede ser un dato nulo o vacio");
        }

        Optional<Familia> respuesta = familiaRepository.findById(idFamilia);

        if (!respuesta.isPresent()) {
            throw new ServiceExceptions("El propietario no puede ser nulo");
        }
    }

    @Transactional(readOnly = true)
    public List<Casa> buscarCasasPorCiudad(String ciudad) throws ServiceExceptions {

        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new ServiceExceptions("La ciudad ingresada no puede ser nulo o estar vacio");
        }

        List<Casa> casas = casaRepository.buscarCasaPorCiudad(ciudad);

        if (casas.isEmpty() || casas == null) {
            throw new ServiceExceptions("No se encontraron casas en la ciudad ingresada");
        } else {
            return casas;
        }
    }

    @Transactional(readOnly = true)
    public List<Casa> buscarCasasPorPais(String pais) throws ServiceExceptions {

        if (pais == null || pais.trim().isEmpty()) {
            throw new ServiceExceptions("El pais ingresado no puede ser nulo o estar vacio");
        }

        List<Casa> casas = casaRepository.buscarCasaPorPais(pais);

        if (casas.isEmpty() || casas == null) {
            throw new ServiceExceptions("No se encontraron casas en el pais ingresado");
        } else {
            return casas;
        }
    }

    @Transactional(readOnly = true)
    public List<Casa> buscarCasasPorTipoVivienda(String tipoVivienda) throws ServiceExceptions {

        if (tipoVivienda == null || tipoVivienda.trim().isEmpty()) {
            throw new ServiceExceptions("El tipo de vivienda ingresado no puede ser nulo o estar vacio");
        }

        List<Casa> casas = casaRepository.buscarCasaPorTipoDeVivienda(tipoVivienda);

        if (casas.isEmpty() || casas == null) {
            throw new ServiceExceptions("No se encontraron casas del tipo de vivienda especificado");
        } else {
            return casas;
        }
    }

    @Transactional(readOnly = true)
    public List<Casa> buscarCasasEntrePrecios(Double precioBajo, Double precioAlto) throws ServiceExceptions {

        if (precioBajo == null || precioBajo <= 0) {
            throw new ServiceExceptions("El precio ingresado no puede ser nulo o negativo");
        }

        if (precioAlto == null || precioAlto <= 0 || precioAlto < precioBajo) {
            throw new ServiceExceptions("El precio ingresado no puede ser nulo, negativo o mayor al de menor precio");
        }

        List<Casa> casas = casaRepository.buscarCasaPorPrecio(precioBajo, precioAlto);

        if (casas.isEmpty() || casas == null) {
            throw new ServiceExceptions("No se encontraron casas en el margen de precio ingresado");
        } else {
            return casas;
        }
    }

    @Transactional(readOnly = true)
    public List<Casa> buscarCasasPorPropietario(String idFamilia) throws ServiceExceptions {

        Optional<Familia> respuestaFamilia = familiaRepository.findById(idFamilia);
        if (!respuestaFamilia.isPresent() || respuestaFamilia.get() == null) {
            throw new ServiceExceptions("El propietario ingresado no puede ser nulo o no se encuentra registrada en el sistema");
        }

        List<Casa> casas = casaRepository.buscarCasaPorPropietario(respuestaFamilia.get());

        if (casas.isEmpty() || casas == null) {
            throw new ServiceExceptions("No se encontraron casas del propietario especificado");
        } else {
            return casas;
        }
    }

    @Transactional(readOnly = true)
    public List<Casa> listarTodasLasCasas() throws ServiceExceptions {

        List<Casa> casas = casaRepository.listarTodasLasCasas();

        if (casas.isEmpty()) {
            throw new ServiceExceptions("Aun no hay casas registradas en el sistema");
        } else {
            return casas;
        }
    }

}
