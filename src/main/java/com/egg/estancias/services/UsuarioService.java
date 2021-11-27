package com.egg.estancias.services;

import com.egg.estancias.entities.Usuario;
import com.egg.estancias.enums.Rol;
import com.egg.estancias.errors.ServiceExceptions;
import com.egg.estancias.repositories.UsuarioRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Transactional
    public void registrar(String alias, String email, String clave, String clave2) throws ServiceExceptions {

        validarDatos(alias, email, clave, clave2);

        Usuario usuario = new Usuario();
        usuario.setAlias(alias);
        usuario.setEmail(email);
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);
        usuario.setFechaAlta(new Date());
        usuario.setFechaBaja(null);
        usuario.setRol(Rol.USER);

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void modificarUsuario(String idUsuario, String alias, String email, String clave, String clave2) throws ServiceExceptions {

        validarDatos(alias, email, clave, clave2);

        Optional<Usuario> respuestaUsuario = usuarioRepository.findById(idUsuario);

        if (respuestaUsuario.isPresent()) {
            Usuario usuario = respuestaUsuario.get();
            usuario.setAlias(alias);
            usuario.setEmail(email);
            usuario.setClave(clave);
            usuario.setFechaAlta(new Date());

            usuarioRepository.save(usuario);
        } else {
            throw new ServiceExceptions("El id ingresado no pertenece a un usuario registrado");
        }
    }

    public void validarDatos(String alias, String email, String clave, String clave2) throws ServiceExceptions {

        if (alias == null || alias.trim().isEmpty()) {
            throw new ServiceExceptions("El alias ingresado no debe ser nulo o estar vacio");
        }

        if (email == null || alias.trim().isEmpty()) {
            throw new ServiceExceptions("El email ingresado no debe ser nulo o estar vacio");
        }

        if (clave == null || clave.trim().isEmpty() || clave.length() < 6) {
            throw new ServiceExceptions("La clave ingresada no puede ser nula, estar vacia o contener menos de 6 caracteres");
        }
        if (!clave.equals(clave2)) {
            throw new ServiceExceptions("Las claves deben ser iguales");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.buscarUsuarioPorEmail(mail);

        if (usuario != null && usuario.getFechaAlta() != null) {

            List<GrantedAuthority> permisos = new ArrayList<>(); //creamos una lista de permisos los cuales otorgan autorizacion para poder acceder a las distinas paginas desde los controladores         
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + usuario.getRol()); //creamos un permiso para el usuario segun su rol
            permisos.add(p1); //Agrego el permiso a la lista

            //Esto me permite guardar el OBJETO USUARIO LOG, para luego ser utilizado
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario); // llave + valor. La llave es el nombre con el que va a viajar el usuario una vez que se registre

            User user = new User(usuario.getEmail(), usuario.getClave(), permisos);

            return user;
        } else {
            return null;
        }
    }

    @Transactional
    public void cambiarRol(String idUsuario) throws ServiceExceptions {

        Optional<Usuario> respuesta = usuarioRepository.findById(idUsuario);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            if (usuario.getRol().equals(Rol.ADMIN)) {
                usuario.setRol(Rol.USER);
            } else if (usuario.getRol().equals(Rol.USER)) {
                usuario.setRol(Rol.ADMIN);
            }

            usuarioRepository.save(usuario);
        } else {
            throw new ServiceExceptions("El id ingresado no pertenece a un usuario registrado");
        }
    }

    @Transactional
    public void deshabilitarUsuario(String idUsuario) throws ServiceExceptions {

        Optional<Usuario> respuesta = usuarioRepository.findById(idUsuario);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            usuario.setFechaAlta(null);
            usuario.setFechaBaja(new Date());

            usuarioRepository.save(usuario);
        } else {
            throw new ServiceExceptions("El id ingresado no pertence a un usuario registrado");
        }
    }

    @Transactional
    public void habilitarUsuario(String idUsuario) throws ServiceExceptions {

        Optional<Usuario> respuesta = usuarioRepository.findById(idUsuario);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            usuario.setFechaBaja(null);
            usuario.setFechaAlta(new Date());

            usuarioRepository.save(usuario);
        } else {
            throw new ServiceExceptions("El id ingresado no pertence a un usuario registrado");
        }
    }

    @Transactional
    public void eliminarUsuario(String idUsuario) throws ServiceExceptions {

        Optional<Usuario> respuesta = usuarioRepository.findById(idUsuario);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            usuarioRepository.delete(usuario);
        } else {
            throw new ServiceExceptions("El id ingresado no pertence a un usuario registrado");
        }
    }

    @Transactional(readOnly = true)

    public List<Usuario> listarUsuarios() throws ServiceExceptions {

        List<Usuario> usuarios = usuarioRepository.listarUsuarios();

        if (usuarios.isEmpty() || usuarios == null) {
            throw new ServiceExceptions("No se encontraron usuarios registrados");
        } else {
            return usuarios;
        }
    }
}
