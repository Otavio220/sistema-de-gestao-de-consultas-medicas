package com.example.clinica.clinica_api.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMINISTRADOR")
public class Administrador extends Usuario {
    public Administrador() {}
}
