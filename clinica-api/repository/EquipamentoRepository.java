package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    List<Equipamento> findByAtivoTrue();

    List<Equipamento> findByNomeContainingIgnoreCase(String nome);
}
