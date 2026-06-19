package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Consulta;
import com.example.clinica.clinica_api.entity.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // ---------------------------------------------------------------
    // Verifica se o médico já tem consulta no intervalo de horário
    // (ignora consultas canceladas e não comparecidas)
    // ---------------------------------------------------------------
    @Query("SELECT COUNT(c) > 0 FROM Consulta c WHERE c.medico.id = :medicoId " +
            "AND c.status NOT IN ('CANCELADA', 'NAO_COMPARECEU') " +
            "AND c.dataHora < :fim " +
            "AND FUNCTION('TIMESTAMPADD', MINUTE, c.duracaoMinutos, c.dataHora) > :inicio " +
            "AND (:consultaId IS NULL OR c.id <> :consultaId)")
    boolean existeConflitoPorMedico(
            @Param("medicoId") Long medicoId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("consultaId") Long consultaId);

    // ---------------------------------------------------------------
    // Verifica se o consultório já está ocupado no intervalo de horário
    // ---------------------------------------------------------------
    @Query("SELECT COUNT(c) > 0 FROM Consulta c WHERE c.consultorio.id = :consultorioId " +
            "AND c.status NOT IN ('CANCELADA', 'NAO_COMPARECEU') " +
            "AND c.dataHora < :fim " +
            "AND FUNCTION('TIMESTAMPADD', MINUTE, c.duracaoMinutos, c.dataHora) > :inicio " +
            "AND (:consultaId IS NULL OR c.id <> :consultaId)")
    boolean existeConflitoPorConsultorio(
            @Param("consultorioId") Long consultorioId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("consultaId") Long consultaId);

    // ---------------------------------------------------------------
    // Busca médicos com consulta no horário (para atribuição automática)
    // ---------------------------------------------------------------
    @Query("SELECT DISTINCT c.medico.id FROM Consulta c WHERE c.medico.especialidade.id = :especialidadeId " +
            "AND c.status NOT IN ('CANCELADA', 'NAO_COMPARECEU') " +
            "AND c.dataHora < :fim " +
            "AND FUNCTION('TIMESTAMPADD', MINUTE, c.duracaoMinutos, c.dataHora) > :inicio")
    List<Long> findMedicoOcupadosPorEspecialidadeEHorario(
            @Param("especialidadeId") Long especialidadeId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    // ---------------------------------------------------------------
    // Busca consultórios ocupados no horário
    // ---------------------------------------------------------------
    @Query("SELECT DISTINCT c.consultorio.id FROM Consulta c WHERE c.consultorio IS NOT NULL " +
            "AND c.status NOT IN ('CANCELADA', 'NAO_COMPARECEU') " +
            "AND c.dataHora < :fim " +
            "AND FUNCTION('TIMESTAMPADD', MINUTE, c.duracaoMinutos, c.dataHora) > :inicio")
    List<Long> findConsultoriosOcupadosNoHorario(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);
}