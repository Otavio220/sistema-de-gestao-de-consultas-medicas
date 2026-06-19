package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Recepcionista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Long> {

    Optional<Recepcionista> findByEmail(String email);

    List<Recepcionista> findByAtivoTrue();
}
