package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByCpf(String cpf);

    List<Paciente> findByNomeContainingIgnoreCase(String nome);

    boolean existsByCpf(String cpf);
}
