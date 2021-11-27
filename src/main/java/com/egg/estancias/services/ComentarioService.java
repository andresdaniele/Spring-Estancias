package com.egg.estancias.services;

import com.egg.estancias.entities.Casa;
import com.egg.estancias.entities.Comentario;
import com.egg.estancias.errors.ServiceExceptions;
import com.egg.estancias.repositories.CasaRepository;
import com.egg.estancias.repositories.ComentarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComentarioService {

    @Autowired
    ComentarioRepository comentarioRepository;

    @Autowired
    CasaRepository casaRepository;

    @Transactional
    public void crearComentario(String descripcion, String idCasa) throws ServiceExceptions {

        verificarDatos(descripcion, idCasa);

        Comentario comentario = new Comentario();

        comentario.setDescripcion(descripcion);
        comentario.setCasa(casaRepository.getById(idCasa));

        comentarioRepository.save(comentario);

    }

    @Transactional
    public void modificarComentario(String idComentario, String descripcion, String idCasa) throws ServiceExceptions {

        verificarDatos(descripcion, idCasa);

        Optional<Comentario> respuestaComentario = comentarioRepository.findById(idComentario);

        if (respuestaComentario.isPresent()) {
            Comentario comentario = respuestaComentario.get();

            comentario.setDescripcion(descripcion);
            comentario.setCasa(casaRepository.getById(idCasa));

            comentarioRepository.save(comentario);
        } else {
            throw new ServiceExceptions("El Id ingresado no corresponde al de un comentario registrado en el sistema");
        }
    }

    @Transactional
    public void eliminarComentario(String idComentario) throws ServiceExceptions {

        Optional<Comentario> respuestaComentario = comentarioRepository.findById(idComentario);

        if (respuestaComentario.isPresent()) {
            Comentario comentario = respuestaComentario.get();

            comentarioRepository.delete(comentario);
        } else {
            throw new ServiceExceptions("El Id ingresado no corresponde al de un comentario registrado en el sistema");
        }

    }

    public void verificarDatos(String descripcion, String idCasa) throws ServiceExceptions {

        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new ServiceExceptions("La descripcion no puede ser nula o vacia");
        }

        Optional<Casa> respuestaCasa = casaRepository.findById(idCasa);
        if (!respuestaCasa.isPresent()) {
            throw new ServiceExceptions("El Id ingresado no pertenece a una casa registrada");
        }
    }

    @Transactional(readOnly = true)
    public List<Comentario> listarComentariosCasa(String idCasa) throws ServiceExceptions {

        Optional<Casa> respuestaCasa = casaRepository.findById(idCasa);
        if (respuestaCasa.isPresent()) {
            List<Comentario> comentarios = comentarioRepository.listarComentarioCasa(respuestaCasa.get());

            if (comentarios.isEmpty() || comentarios == null) {

                throw new ServiceExceptions("La casa ingredad no posee aun comentarios");
            } else {
                return comentarios;
            }
        } else {
            throw new ServiceExceptions("El Id ingresado no pertenece a una casa registrada");
        }
    }

    @Transactional(readOnly = true)
    public List<Comentario> listarTodosLosComentarios() throws ServiceExceptions {

        List<Comentario> comentarios = comentarioRepository.listarTodosLosComentarios();

        if (comentarios.isEmpty()) {
            throw new ServiceExceptions("Aun no hay comentarios registrados en el sistema");
        } else {
            return comentarios;
        }
    }
}
