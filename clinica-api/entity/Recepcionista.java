package com.example.clinica.clinica_api.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("RECEPCIONISTA")
public class Recepcionista extends Usuario {
    public Recepcionista() {}
}
