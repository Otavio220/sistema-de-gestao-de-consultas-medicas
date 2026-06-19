package com.example.clinica.clinica_api.controller;

import com.example.clinica.clinica_api.dto.ConsultorioEquipamentoDTO;
import com.example.clinica.clinica_api.service.ConsultorioEquipamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultorios/{consultorioId}/equipamentos")
public class ConsultorioEquipamentoController {

    @Autowired
    private ConsultorioEquipamentoService service;

    @GetMapping
    public ResponseEntity<List<ConsultorioEquipamentoDTO>> listarPorConsultorio(@PathVariable Long consultorioId) {
        return ResponseEntity.ok(service.listarPorConsultorio(consultorioId));
    }

    @PostMapping("/{equipamentoId}")
    public ResponseEntity<ConsultorioEquipamentoDTO> vincular(
            @PathVariable Long consultorioId,
            @PathVariable Long equipamentoId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.vincular(consultorioId, equipamentoId));
    }

    @DeleteMapping("/{equipamentoId}")
    public ResponseEntity<Void> desvincular(
            @PathVariable Long consultorioId,
            @PathVariable Long equipamentoId) {
        service.desvincular(consultorioId, equipamentoId);
        return ResponseEntity.noContent().build();
    }
}
