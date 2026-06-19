package com.example.clinica.clinica_api.controller;

import com.example.clinica.clinica_api.dto.EquipamentoDTO;
import com.example.clinica.clinica_api.service.EquipamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipamentos")
public class EquipamentoController {

    @Autowired
    private EquipamentoService equipamentoService;

    @GetMapping
    public ResponseEntity<List<EquipamentoDTO>> listarTodos(@RequestParam(required = false) Boolean ativos) {
        if (Boolean.TRUE.equals(ativos)) {
            return ResponseEntity.ok(equipamentoService.listarAtivos());
        }
        return ResponseEntity.ok(equipamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(equipamentoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EquipamentoDTO> criar(@RequestBody EquipamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamentoService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipamentoDTO> atualizar(@PathVariable Long id, @RequestBody EquipamentoDTO dto) {
        return ResponseEntity.ok(equipamentoService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        equipamentoService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/manutencao")
    public ResponseEntity<Void> realizarManutencao(@PathVariable Long id) {
        equipamentoService.realizarManutencao(id);
        return ResponseEntity.noContent().build();
    }
}
