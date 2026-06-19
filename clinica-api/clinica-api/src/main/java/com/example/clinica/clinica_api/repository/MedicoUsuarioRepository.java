package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.MedicoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoUsuarioRepository extends JpaRepository<MedicoUsuario, Long> {

    Optional<MedicoUsuario> findByEmail(String email);

    List<MedicoUsuario> findByAtivoTrue();
}
