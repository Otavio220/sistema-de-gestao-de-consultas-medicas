package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByCrm(String crm);

    List<Medico> findByAtivoTrue();

    List<Medico> findByEspecialidadeId(Long especialidadeId);

    List<Medico> findByNomeContainingIgnoreCase(String nome);
}
