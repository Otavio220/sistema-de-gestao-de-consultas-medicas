package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Consulta;
import com.example.clinica.clinica_api.entity.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository responsável pelo acesso aos dados da entidade Consulta.
 * Estende JpaRepository, que já fornece os métodos básicos de CRUD.
 */
@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    /**
     * Lista todas as consultas de um paciente específico.
     * Equivale a: SELECT * FROM consultas WHERE paciente_id = ?
     * Usado para exibir o histórico de consultas do paciente.
     */
    List<Consulta> findByPacienteId(Long pacienteId);

    /**
     * Lista todas as consultas de um médico específico.
     * Equivale a: SELECT * FROM consultas WHERE medico_id = ?
     * Usado para exibir a agenda do médico.
     */
    List<Consulta> findByMedicoId(Long medicoId);

    /**
     * Lista todas as consultas filtradas por status.
     * Exemplo: buscar apenas consultas AGENDADAS ou CANCELADAS.
     * Equivale a: SELECT * FROM consultas WHERE status = ?
     */
    List<Consulta> findByStatus(StatusConsulta status);
}