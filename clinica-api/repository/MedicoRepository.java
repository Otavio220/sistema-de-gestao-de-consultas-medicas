package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByCrm(String crm);

    List<Medico> findByAtivoTrue();

    List<Medico> findByEspecialidadeId(Long especialidadeId);

    List<Medico> findByNomeContainingIgnoreCase(String nome);

    // ---------------------------------------------------------------
    // Busca médicos ativos de uma especialidade que NÃO estão
    // com consulta no intervalo de horário informado
    // ---------------------------------------------------------------
    @Query("SELECT m FROM Medico m WHERE m.especialidade.id = :especialidadeId " +
            "AND m.ativo = true " +
            "AND m.id NOT IN :medicosOcupados")
    List<Medico> findMedicosDisponiveisPorEspecialidade(
            @Param("especialidadeId") Long especialidadeId,
            @Param("medicosOcupados") List<Long> medicosOcupados);

    // Fallback: quando nenhum médico está ocupado, retorna todos ativos da especialidade
    @Query("SELECT m FROM Medico m WHERE m.especialidade.id = :especialidadeId AND m.ativo = true")
    List<Medico> findAtivosByEspecialidade(@Param("especialidadeId") Long especialidadeId);
}