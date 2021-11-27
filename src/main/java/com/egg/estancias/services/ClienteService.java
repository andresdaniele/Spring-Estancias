package com.egg.estancias.services;

import com.egg.estancias.entities.Cliente;
import com.egg.estancias.entities.Usuario;
import com.egg.estancias.errors.ServiceExceptions;
import com.egg.estancias.repositories.ClienteRepository;
import com.egg.estancias.repositories.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Transactional
    public void crearCliente(String nombre, String calle, Integer numero,
            String codPostal, String ciudad, String pais, String email, String idUsuario) throws ServiceExceptions {

        verificarDatos(nombre, calle, numero, codPostal, ciudad, pais, email, idUsuario);

        Cliente cliente = new Cliente();

        cliente.setNombre(nombre);
        cliente.setCalle(calle);
        cliente.setNumero(numero);
        cliente.setCodPostal(codPostal);
        cliente.setCiudad(ciudad);
        cliente.setPais(pais);
        cliente.setEmail(email);
        cliente.setUsuario(usuarioRepository.getById(idUsuario));

        clienteRepository.save(cliente);
    }

    @Transactional
    public void modificarCliente(String idCliente, String nombre, String calle, Integer numero,
            String codPostal, String ciudad, String pais, String email, String idUsuario) throws ServiceExceptions {

        verificarDatos(nombre, calle, numero, codPostal, ciudad, pais, email, idUsuario);

        Optional<Cliente> respuestaCliente = clienteRepository.findById(idCliente);

        if (respuestaCliente.isPresent()) {

            Cliente cliente = respuestaCliente.get();

            cliente.setNombre(nombre);
            cliente.setCalle(calle);
            cliente.setNumero(numero);
            cliente.setCodPostal(codPostal);
            cliente.setCiudad(ciudad);
            cliente.setPais(pais);
            cliente.setEmail(email);
            cliente.setUsuario(usuarioRepository.getById(idUsuario));

            clienteRepository.save(cliente);
        } else {
            throw new ServiceExceptions("El Id ingresado no pertenece a un cliente registrado en el sistema");
        }

    }

    public void verificarDatos(String nombre, String calle, Integer numero,
            String codPostal, String ciudad, String pais, String email, String idUsuario) throws ServiceExceptions {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ServiceExceptions("El nombre no puede ser un dato nulo o vacio");
        }
        if (calle == null || calle.trim().isEmpty()) {
            throw new ServiceExceptions("La calle no puede ser un dato nulo o vacio");
        }

        if (numero == null || numero <= 0) {
            throw new ServiceExceptions("El numero no puede ser nulo o negativo");
        }

        if (codPostal == null || codPostal.trim().isEmpty()) {
            throw new ServiceExceptions("El codigo postal no puede ser un dato nulo o vacio");
        }

        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new ServiceExceptions("La ciudad no puede ser un dato nulo o vacio");
        }

        if (pais == null || pais.trim().isEmpty()) {
            throw new ServiceExceptions("El pais no puede ser un dato nulo o vacio");
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
    public List<Cliente> listarClientePorNombre(String nombre) throws ServiceExceptions {

        List<Cliente> clientes = clienteRepository.buscarClientePorNombre(nombre);

        if (clientes.isEmpty() || clientes == null) {
            throw new ServiceExceptions("No se encontraron clientes registradados con ese nombre");
        } else {
            return clientes;
        }
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarClientePorEmail(String email) throws ServiceExceptions {

        List<Cliente> clientes = clienteRepository.buscarClientePorEmail(email);

        if (clientes.isEmpty() || clientes == null) {
            throw new ServiceExceptions("No se encontraron clientes registradados con ese email");
        } else {
            return clientes;
        }
    }

    @Transactional(readOnly = true)
    public Cliente listarClientePorUsuario(String idUsuario) throws ServiceExceptions {

        Optional<Usuario> respuestaUsuario = usuarioRepository.findById(idUsuario);

        if (respuestaUsuario.isPresent()) {
            Cliente cliente = clienteRepository.buscarClientePorUsuario(respuestaUsuario.get());

            if (cliente == null) {
                throw new ServiceExceptions("El usuario no posee ningun cliente registrado");
            } else {
                return cliente;
            }
        } else {
            throw new ServiceExceptions("El id ingresado no pertenece a ningun usuario regitrado");
        }
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodasLosClientes() throws ServiceExceptions {

        List<Cliente> clientes = clienteRepository.listarClientes();

        if (clientes.isEmpty() || clientes == null) {
            throw new ServiceExceptions("No se encontraron clientes registrados en el sistema");
        } else {
            return clientes;
        }
    }

}
