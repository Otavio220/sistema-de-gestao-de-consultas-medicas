package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.ConsultorioEquipamento;
import com.example.clinica.clinica_api.entity.ConsultorioEquipamentoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultorioEquipamentoRepository extends JpaRepository<ConsultorioEquipamento, ConsultorioEquipamentoId> {

    List<ConsultorioEquipamento> findByConsultorioId(Long consultorioId);

    List<ConsultorioEquipamento> findByEquipamentoId(Long equipamentoId);
}
