package com.example.taskmaster.Entidades;

public class Usuarios {
    String nombre, email, usuario;

    public Usuarios(String nombre, String email, String usuario) {
        this.nombre = nombre;
        this.email = email;
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}
