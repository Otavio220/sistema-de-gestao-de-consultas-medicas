package com.example.clinica.clinica_api.controller;

import com.example.clinica.clinica_api.dto.HistoricoClinicoDTO;
import com.example.clinica.clinica_api.service.HistoricoClinicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historicos")
public class HistoricoClinicoController {

    @Autowired
    private HistoricoClinicoService historicoService;

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<HistoricoClinicoDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(historicoService.listarPorPaciente(pacienteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoricoClinicoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(historicoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<HistoricoClinicoDTO> adicionarRegistro(@RequestBody HistoricoClinicoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(historicoService.adicionarRegistro(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        historicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
