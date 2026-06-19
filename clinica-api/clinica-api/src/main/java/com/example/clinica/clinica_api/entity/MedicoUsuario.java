package com.example.clinica.clinica_api.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("MEDICO_USUARIO")
public class MedicoUsuario extends Usuario {
    public MedicoUsuario() {}
}
