package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository responsável pelo acesso aos dados da entidade Consultorio.
 * Estende JpaRepository, que já fornece os métodos básicos de CRUD.
 */
@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {

    /**
     * Lista apenas os consultórios que estão ativos.
     * Consultórios inativos não devem ser disponibilizados para agendamento.
     * Equivale a: SELECT * FROM consultorios WHERE ativo = true
     */
    List<Consultorio> findByAtivoTrue();
}