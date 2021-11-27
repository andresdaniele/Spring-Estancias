package com.egg.estancias.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Familia {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String nombre;
    private Integer edadMinHijo;
    private Integer edadMaxHijo;
    private Integer numHijos;
    private String email;

    @OneToOne
    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEdadMinHijo() {
        return edadMinHijo;
    }

    public void setEdadMinHijo(Integer edadMinHijo) {
        this.edadMinHijo = edadMinHijo;
    }

    public Integer getEdadMaxHijo() {
        return edadMaxHijo;
    }

    public void setEdadMaxHijo(Integer edadMaxHijo) {
        this.edadMaxHijo = edadMaxHijo;
    }

    public Integer getNumHijos() {
        return numHijos;
    }

    public void setNumHijos(Integer numHijos) {
        this.numHijos = numHijos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
