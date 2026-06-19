package com.example.clinica.clinica_api.controller;

import com.example.clinica.clinica_api.dto.RecepcionistaDTO;
import com.example.clinica.clinica_api.service.RecepcionistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recepcionistas")
public class RecepcionistaController {

    @Autowired
    private RecepcionistaService recepcionistaService;

    @GetMapping
    public ResponseEntity<List<RecepcionistaDTO>> listarTodos(@RequestParam(required = false) Boolean ativos) {
        if (Boolean.TRUE.equals(ativos)) {
            return ResponseEntity.ok(recepcionistaService.listarAtivos());
        }
        return ResponseEntity.ok(recepcionistaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecepcionistaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(recepcionistaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<RecepcionistaDTO> criar(@RequestBody Map<String, Object> body) {
        RecepcionistaDTO dto = new RecepcionistaDTO();
        dto.setNome((String) body.get("nome"));
        dto.setEmail((String) body.get("email"));
        String senha = (String) body.get("senha");
        return ResponseEntity.status(HttpStatus.CREATED).body(recepcionistaService.criar(dto, senha));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecepcionistaDTO> atualizar(@PathVariable Long id, @RequestBody RecepcionistaDTO dto) {
        return ResponseEntity.ok(recepcionistaService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        recepcionistaService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
