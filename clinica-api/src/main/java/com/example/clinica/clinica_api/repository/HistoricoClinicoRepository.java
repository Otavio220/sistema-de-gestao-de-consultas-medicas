package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.HistoricoClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository responsável pelo acesso aos dados da entidade HistoricoClinico.
 * Estende JpaRepository, que já fornece os métodos básicos de CRUD.
 */
@Repository
public interface HistoricoClinicoRepository extends JpaRepository<HistoricoClinico, Long> {

    /**
     * Lista todo o histórico clínico de um paciente, ordenado do registro
     * mais recente para o mais antigo (OrderByDataRegistroDesc).
     * Equivale a: SELECT * FROM historicos_clinicos WHERE paciente_id = ?
     *             ORDER BY data_registro DESC
     */
    List<HistoricoClinico> findByPacienteIdOrderByDataRegistroDesc(Long pacienteId);
}