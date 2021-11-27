package com.egg.estancias.controllers;

import com.egg.estancias.entities.Usuario;
import com.egg.estancias.errors.ServiceExceptions;
import com.egg.estancias.services.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/dashboard")
    public String dashboard(ModelMap modelo) throws ServiceExceptions {
        try {
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            modelo.addAttribute("usuarios", usuarios);
            return "dashboard";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "dashboard";
        }
    }

    @GetMapping("/alta/{id}")
    public String habilitarUsuario(@PathVariable String id, ModelMap modelo) throws ServiceExceptions {
        try {
            usuarioService.habilitarUsuario(id);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "dashboard";
        }
    }

    @GetMapping("/baja/{id}")
    public String deshabilitarUsuario(@PathVariable String id, ModelMap modelo) throws ServiceExceptions {
        try {
            usuarioService.deshabilitarUsuario(id);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "dashboard";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable String id, ModelMap modelo) throws ServiceExceptions {
        try {
            usuarioService.eliminarUsuario(id);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "dashboard";
        }
    }

    @GetMapping("/cambiar-rol/{id}")
    public String cambiarRol(@PathVariable String id, ModelMap modelo) throws ServiceExceptions {
        try {
            usuarioService.cambiarRol(id);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "dashboard";
        }
    }

}
