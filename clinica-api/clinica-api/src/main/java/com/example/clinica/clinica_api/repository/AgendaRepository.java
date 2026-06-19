package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    List<Agenda> findByConsultorioId(Long consultorioId);

    List<Agenda> findByData(LocalDate data);

    List<Agenda> findByConsultorioIdAndData(Long consultorioId, LocalDate data);

    List<Agenda> findByDisponivelTrue();

    List<Agenda> findByDataBetween(LocalDate inicio, LocalDate fim);
}
