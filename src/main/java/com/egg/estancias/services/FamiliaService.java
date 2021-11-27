package com.egg.estancias.services;

import com.egg.estancias.entities.Familia;
import com.egg.estancias.entities.Usuario;
import com.egg.estancias.errors.ServiceExceptions;
import com.egg.estancias.repositories.FamiliaRepository;
import com.egg.estancias.repositories.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FamiliaService {

    @Autowired
    FamiliaRepository familiaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Transactional
    public void crearFamilia(String nombre, Integer edadMinHijo, Integer edadMaxHijo,
            Integer numHijos, String email, String idUsuario) throws ServiceExceptions {

        verificarDatos(nombre, edadMinHijo, edadMaxHijo, numHijos, email, idUsuario);

        Familia familia = new Familia();
        familia.setNombre(nombre);
        familia.setEdadMinHijo(edadMinHijo);
        familia.setEdadMaxHijo(edadMaxHijo);
        familia.setNumHijos(numHijos);
        familia.setEmail(email);
        familia.setUsuario(usuarioRepository.getById(idUsuario));

        familiaRepository.save(familia);

    }

    @Transactional
    public void modificarFamilia(String idFamilia, String nombre, Integer edadMinHijo, Integer edadMaxHijo,
            Integer numHijos, String email, String idUsuario) throws ServiceExceptions {

        verificarDatos(nombre, edadMinHijo, edadMaxHijo, numHijos, email, idUsuario);

        Optional<Familia> respuestaFamilia = familiaRepository.findById(idFamilia);

        if (respuestaFamilia.isPresent()) {

            Familia familia = respuestaFamilia.get();
            familia.setNombre(nombre);
            familia.setEdadMinHijo(edadMinHijo);
            familia.setEdadMaxHijo(edadMaxHijo);
            familia.setNumHijos(numHijos);
            familia.setEmail(email);
            familia.setUsuario(usuarioRepository.getById(idUsuario));

            familiaRepository.save(familia);
        } else {
            throw new ServiceExceptions("El Id ingresado no pertenece a una familia registrada en el sistema");
        }
    }

    @Transactional
    public void elminarFamilia(String idFamilia) throws ServiceExceptions {

        Optional<Familia> respuestaFamilia = familiaRepository.findById(idFamilia);

        if (respuestaFamilia.isPresent()) {
            Familia familia = respuestaFamilia.get();
            familiaRepository.delete(familia);

        } else {
            throw new ServiceExceptions("El Id ingresado no pertenece a una familia registrada en el sistema");
        }
    }

    public void verificarDatos(String nombre, Integer edadMinHijo, Integer edadMaxHijo,
            Integer numHijos, String email, String idUsuario) throws ServiceExceptions {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ServiceExceptions("El nombre ingresado no puede ser nulo o vacio");
        }

        if (edadMaxHijo == null || edadMaxHijo < 0 || edadMaxHijo < edadMinHijo) {
            throw new ServiceExceptions("La edad maxima de los hijos no puede ser menor a la edad minima, nula o negativa");
        }

        if (edadMinHijo == null || edadMinHijo < 0) {
            throw new ServiceExceptions("La edad minima de los hijos no puede ser nula o negativa");
        }

        if (numHijos == null || numHijos < 0) {
            throw new ServiceExceptions("El numero de hijos no puede ser nulo o negativo");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new ServiceExceptions("El email ingresado no puede ser nulo o vacio");
        }

        Optional<Usuario> respustaUsuario = usuarioRepository.findById(idUsuario);

        if (!respustaUsuario.isPresent()) {
            throw new ServiceExceptions("El Id ingresado no corresponde a un usuario registrado en el sistema");
        }
    }

    @Transactional(readOnly = true)
    public List<Familia> listarFamiliaPorNombre(String nombre) throws ServiceExceptions {

        List<Familia> familias = familiaRepository.buscarFamiliaPorNombre(nombre);

        if (familias.isEmpty() || familias == null) {
            throw new ServiceExceptions("No se encontraron familias registradadas con ese nombre");
        } else {
            return familias;
        }
    }

    @Transactional(readOnly = true)
    public List<Familia> listarFamiliaPorEmail(String email) throws ServiceExceptions {

        List<Familia> familias = familiaRepository.buscarFamiliaPorEmail(email);

        if (familias.isEmpty() || familias == null) {
            throw new ServiceExceptions("No se encontraron familias registradadas con ese email");
        } else {
            return familias;
        }
    }

    @Transactional(readOnly = true)
    public Familia listarFamiliaPorUsuario(String idUsuario) throws ServiceExceptions {

        Optional<Usuario> respuestaUsuario = usuarioRepository.findById(idUsuario);

        if (respuestaUsuario.isPresent()) {
            Familia familia = familiaRepository.buscarFamiliaPorUsuario(respuestaUsuario.get());

            if (familia == null) {
                throw new ServiceExceptions("El usuario no posee ninguna familia registrada");
            } else {
                return familia;
            }
        } else {
            throw new ServiceExceptions("El id ingresado no pertenece a ningun usuario regitrado");
        }
    }

    @Transactional(readOnly = true)
    public List<Familia> listarTodasLasFamilia() throws ServiceExceptions {

        List<Familia> familias = familiaRepository.listarFamilias();

        if (familias.isEmpty() || familias == null) {
            throw new ServiceExceptions("No se encontraron familias registradadas en el sistema");
        } else {
            return familias;
        }
    }

}
