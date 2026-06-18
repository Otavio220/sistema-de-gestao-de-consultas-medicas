package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository responsável pelo acesso aos dados da entidade Agenda.
 * Estende JpaRepository, que já fornece os métodos básicos de CRUD.
 */
@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    /**
     * Lista todos os horários da agenda de um consultório específico.
     * Equivale a: SELECT * FROM agenda WHERE consultorio_id = ?
     */
    List<Agenda> findByConsultorioId(Long consultorioId);

    /**
     * Lista todos os horários disponíveis em uma data específica.
     * Útil para mostrar ao recepcionista quais horários ainda estão livres.
     * Equivale a: SELECT * FROM agenda WHERE data = ? AND disponivel = true
     */
    List<Agenda> findByDataAndDisponivelTrue(LocalDate data);

    /**
     * Lista os horários disponíveis de um consultório em uma data específica.
     * Combina os dois filtros anteriores para uma busca mais precisa.
     * Equivale a: SELECT * FROM agenda WHERE consultorio_id = ? AND data = ? AND disponivel = true
     */
    List<Agenda> findByConsultorioIdAndDataAndDisponivelTrue(Long consultorioId, LocalDate data);
}