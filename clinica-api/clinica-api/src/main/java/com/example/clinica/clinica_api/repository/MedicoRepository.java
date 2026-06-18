package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository responsável pelo acesso aos dados da entidade Medico.
 * Estende JpaRepository, que já fornece os métodos básicos de CRUD.
 */
@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    /**
     * Busca um médico pelo número do CRM.
     * Retorna Optional para evitar NullPointerException caso não encontre.
     */
    Optional<Medico> findByCrm(String crm);

    /**
     * Lista todos os médicos de uma determinada especialidade.
     * O Spring Data JPA interpreta o nome do método e gera a query automaticamente:
     * SELECT * FROM medicos WHERE especialidade_id = ?
     */
    List<Medico> findByEspecialidadeId(Long especialidadeId);

    /**
     * Lista apenas os médicos que estão ativos no sistema.
     * Equivale a: SELECT * FROM medicos WHERE ativo = true
     */
    List<Medico> findByAtivoTrue();
}