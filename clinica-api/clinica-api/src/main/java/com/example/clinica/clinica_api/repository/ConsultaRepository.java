package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Consulta;
import com.example.clinica.clinica_api.entity.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByPacienteId(Long pacienteId);

    List<Consulta> findByMedicoId(Long medicoId);

    List<Consulta> findByConsultorioId(Long consultorioId);

    List<Consulta> findByStatus(StatusConsulta status);

    List<Consulta> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Consulta> findByMedicoIdAndDataHoraBetween(Long medicoId, LocalDateTime inicio, LocalDateTime fim);
}
