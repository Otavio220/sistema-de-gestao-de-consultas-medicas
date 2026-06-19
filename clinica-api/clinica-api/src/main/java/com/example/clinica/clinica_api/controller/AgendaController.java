package com.example.clinica.clinica_api.controller;

import com.example.clinica.clinica_api.dto.AgendaDTO;
import com.example.clinica.clinica_api.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/agenda")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @GetMapping
    public ResponseEntity<List<AgendaDTO>> listarTodas(@RequestParam(required = false) Boolean disponiveis) {
        if (Boolean.TRUE.equals(disponiveis)) {
            return ResponseEntity.ok(agendaService.listarDisponiveis());
        }
        return ResponseEntity.ok(agendaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendaService.buscarPorId(id));
    }

    @GetMapping("/consultorio/{consultorioId}")
    public ResponseEntity<List<AgendaDTO>> listarPorConsultorio(@PathVariable Long consultorioId) {
        return ResponseEntity.ok(agendaService.listarPorConsultorio(consultorioId));
    }

    @GetMapping("/data")
    public ResponseEntity<List<AgendaDTO>> listarPorData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(agendaService.listarPorData(data));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<AgendaDTO>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(agendaService.listarPorPeriodo(inicio, fim));
    }

    @PostMapping
    public ResponseEntity<AgendaDTO> criar(@RequestBody AgendaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendaService.criar(dto));
    }

    @PatchMapping("/{id}/bloquear")
    public ResponseEntity<AgendaDTO> bloquear(@PathVariable Long id) {
        return ResponseEntity.ok(agendaService.bloquear(id));
    }

    @PatchMapping("/{id}/liberar")
    public ResponseEntity<AgendaDTO> liberar(@PathVariable Long id) {
        return ResponseEntity.ok(agendaService.liberar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        agendaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
