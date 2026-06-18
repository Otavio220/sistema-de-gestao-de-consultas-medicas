package com.example.clinica.clinica_api.controller;

import com.example.clinica.clinica_api.dto.ConsultaDTO;
import com.example.clinica.clinica_api.entity.StatusConsulta;
import com.example.clinica.clinica_api.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping
    public ResponseEntity<List<ConsultaDTO>> listarTodas(@RequestParam(required = false) StatusConsulta status) {
        if (status != null) {
            return ResponseEntity.ok(consultaService.listarPorStatus(status));
        }
        return ResponseEntity.ok(consultaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<ConsultaDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(consultaService.listarPorPaciente(pacienteId));
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<ConsultaDTO>> listarPorMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(consultaService.listarPorMedico(medicoId));
    }

    @PostMapping
    public ResponseEntity<ConsultaDTO> agendar(@RequestBody ConsultaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.agendar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaDTO> atualizar(@PathVariable Long id, @RequestBody ConsultaDTO dto) {
        return ResponseEntity.ok(consultaService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/reagendar")
    public ResponseEntity<ConsultaDTO> reagendar(@PathVariable Long id, @RequestBody Map<String, String> body) {
        LocalDateTime novaDataHora = LocalDateTime.parse(body.get("dataHora"));
        return ResponseEntity.ok(consultaService.reagendar(id, novaDataHora));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ConsultaDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.cancelar(id));
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<ConsultaDTO> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.confirmar(id));
    }
}
