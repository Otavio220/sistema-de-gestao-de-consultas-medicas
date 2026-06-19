package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.ConsultorioEquipamento;
import com.example.clinica.clinica_api.entity.ConsultorioEquipamentoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository responsável pelo acesso aos dados da tabela de relacionamento
 * entre Consultorio e Equipamento (relação N:N).
 * A chave primária é composta (ConsultorioEquipamentoId), por isso ela é
 * passada como segundo parâmetro do JpaRepository no lugar do Long.
 */
@Repository
public interface ConsultorioEquipamentoRepository extends JpaRepository<ConsultorioEquipamento, ConsultorioEquipamentoId> {

    /**
     * Lista todos os equipamentos associados a um consultório específico.
     * Equivale a: SELECT * FROM consultorio_equipamento WHERE consultorio_id = ?
     */
    List<ConsultorioEquipamento> findByConsultorioId(Long consultorioId);

    /**
     * Lista todos os consultórios que possuem um equipamento específico.
     * Equivale a: SELECT * FROM consultorio_equipamento WHERE equipamento_id = ?
     */
    List<ConsultorioEquipamento> findByEquipamentoId(Long equipamentoId);
}