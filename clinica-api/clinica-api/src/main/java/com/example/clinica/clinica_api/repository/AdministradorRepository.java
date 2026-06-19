package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    Optional<Administrador> findByEmail(String email);

    List<Administrador> findByAtivoTrue();
}
