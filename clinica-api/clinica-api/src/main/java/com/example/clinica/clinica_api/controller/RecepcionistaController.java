package com.example.clinica.clinica_api.controller;

import com.example.clinica.clinica_api.dto.ConsultaDTO;
import com.example.clinica.clinica_api.dto.PacienteDTO;
import com.example.clinica.clinica_api.entity.Consulta;
import com.example.clinica.clinica_api.entity.Paciente;
import com.example.clinica.clinica_api.service.RecepcionistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recepcionista")
public class RecepcionistaController {

    @Autowired
    private RecepcionistaService recepcionistaService;

    @PostMapping("/consultas/agendar")
    public ResponseEntity<Void> agendarConsulta(@RequestBody Consulta consulta) {
        recepcionistaService.agendarConsulta(consulta);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/consultas/{id}/reagendar")
    public ResponseEntity<Void> reagendarConsulta(@PathVariable Long id, @RequestBody Consulta dadosNovos) {
        recepcionistaService.reagendarConsulta(id, dadosNovos);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/consultas/{id}/cancelar")
    public ResponseEntity<Void> cancelarConsulta(@PathVariable Long id) {
        recepcionistaService.cancelarConsulta(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/pacientes")
    public ResponseEntity<Void> cadastrarPaciente(@RequestBody Paciente paciente) {
        recepcionistaService.cadastrarPaciente(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
