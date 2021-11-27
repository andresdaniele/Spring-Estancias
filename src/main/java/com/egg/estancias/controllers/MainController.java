package com.egg.estancias.controllers;

import com.egg.estancias.errors.ServiceExceptions;
import com.egg.estancias.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/inicio")
    public String inicio() { 
        return "inicio";
    }

    @GetMapping("/login")
    public String login(ModelMap modelo, @RequestParam(required = false) String error, @RequestParam(required = false) String logout) {

        if (error != null) {
            modelo.put("error", "El email o la contraseña son incorrectos");
        }

        if (logout != null) {
            modelo.put("logout", "La sesion se cerro correctamente");
        }
        return "login";

    }

    @GetMapping("/registro")
    public String registrar() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarPost(ModelMap modelo, @RequestParam String alias, @RequestParam String email, @RequestParam String clave, @RequestParam String clave2) {
        try {
            usuarioService.registrar(alias, email, clave, clave2);
            modelo.put("titulo", "Bienvenido!");
            modelo.put("exito", "Te registraste correctamente en Estancias");
            return "registroExitoso";
        } catch (ServiceExceptions e) {
            System.out.println(e.getMessage());
            modelo.put("alias", alias);
            modelo.put("email", email);
            modelo.put("error", e.getMessage());

            return "registro";
        }

    }

}
