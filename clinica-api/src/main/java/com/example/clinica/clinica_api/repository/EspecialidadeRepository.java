package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository responsável pelo acesso aos dados da entidade Especialidade.
 * Utiliza apenas os métodos padrão do JpaRepository (CRUD básico),
 * pois não há necessidade de buscas personalizadas por enquanto.
 */
@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {
}