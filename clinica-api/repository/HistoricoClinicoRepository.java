package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.HistoricoClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoClinicoRepository extends JpaRepository<HistoricoClinico, Long> {

    List<HistoricoClinico> findByPacienteIdOrderByDataRegistroDesc(Long pacienteId);
}
