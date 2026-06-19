package com.example.clinica.clinica_api.controller;

import com.example.clinica.clinica_api.dto.ConsultorioDTO;
import com.example.clinica.clinica_api.service.ConsultorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultorios")
public class ConsultorioController {

    @Autowired
    private ConsultorioService consultorioService;

    @GetMapping
    public ResponseEntity<List<ConsultorioDTO>> listarTodos(@RequestParam(required = false) Boolean ativos) {
        if (Boolean.TRUE.equals(ativos)) {
            return ResponseEntity.ok(consultorioService.listarAtivos());
        }
        return ResponseEntity.ok(consultorioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultorioDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultorioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ConsultorioDTO> criar(@RequestBody ConsultorioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultorioService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultorioDTO> atualizar(@PathVariable Long id, @RequestBody ConsultorioDTO dto) {
        return ResponseEntity.ok(consultorioService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        consultorioService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
