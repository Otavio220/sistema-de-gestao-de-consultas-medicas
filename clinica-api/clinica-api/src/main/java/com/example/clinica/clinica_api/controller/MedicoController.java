package com.example.clinica.clinica_api.controller;

import com.example.clinica.clinica_api.dto.MedicoDTO;
import com.example.clinica.clinica_api.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public ResponseEntity<List<MedicoDTO>> listarTodos(@RequestParam(required = false) Boolean ativos) {
        if (Boolean.TRUE.equals(ativos)) {
            return ResponseEntity.ok(medicoService.listarAtivos());
        }
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    @GetMapping("/especialidade/{especialidadeId}")
    public ResponseEntity<List<MedicoDTO>> listarPorEspecialidade(@PathVariable Long especialidadeId) {
        return ResponseEntity.ok(medicoService.listarPorEspecialidade(especialidadeId));
    }

    @PostMapping
    public ResponseEntity<MedicoDTO> criar(@RequestBody MedicoDTO dto) {
        MedicoDTO criado = medicoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoDTO> atualizar(@PathVariable Long id, @RequestBody MedicoDTO dto) {
        return ResponseEntity.ok(medicoService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        medicoService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/agenda")
    public ResponseEntity<List<MedicoDTO>> consultarAgenda(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.consultarAgenda(id));
    }
}
