package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository responsável pelo acesso aos dados da entidade Equipamento.
 * Estende JpaRepository, que já fornece os métodos básicos de CRUD.
 */
@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    /**
     * Lista apenas os equipamentos que estão ativos.
     * Equipamentos inativos (em manutenção ou desativados) não devem
     * aparecer como disponíveis.
     * Equivale a: SELECT * FROM equipamentos WHERE ativo = true
     */
    List<Equipamento> findByAtivoTrue();
}