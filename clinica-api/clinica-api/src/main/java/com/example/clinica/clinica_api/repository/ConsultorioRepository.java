package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {

    List<Consultorio> findByAtivoTrue();

    List<Consultorio> findByAndar(String andar);
}
