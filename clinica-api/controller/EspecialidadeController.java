package com.example.clinica.clinica_api.controller;

import com.example.clinica.clinica_api.dto.EspecialidadeDTO;
import com.example.clinica.clinica_api.service.EspecialidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeService especialidadeService;

    @GetMapping
    public ResponseEntity<List<EspecialidadeDTO>> listarTodas() {
        return ResponseEntity.ok(especialidadeService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadeDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadeService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EspecialidadeDTO> criar(@RequestBody EspecialidadeDTO dto) {
        EspecialidadeDTO criada = especialidadeService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadeDTO> atualizar(@PathVariable Long id, @RequestBody EspecialidadeDTO dto) {
        return ResponseEntity.ok(especialidadeService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        especialidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
