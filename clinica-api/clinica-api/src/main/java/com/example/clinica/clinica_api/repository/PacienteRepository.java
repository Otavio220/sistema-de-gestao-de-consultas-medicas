package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository responsável pelo acesso aos dados da entidade Paciente.
 * Estende JpaRepository, que já fornece os métodos básicos de CRUD:
 * save(), findById(), findAll(), deleteById(), etc.
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    /**
     * Busca um paciente pelo CPF.
     * Retorna Optional para evitar NullPointerException caso não encontre.
     * Exemplo de uso: pacienteRepository.findByCpf("123.456.789-00")
     */
    Optional<Paciente> findByCpf(String cpf);

    /**
     * Verifica se já existe um paciente cadastrado com o CPF informado.
     * Útil para validar duplicatas antes de salvar um novo paciente.
     * Retorna true se existir, false caso contrário.
     */
    boolean existsByCpf(String cpf);
}