package com.example.clinica.clinica_api.controller;

import com.example.clinica.clinica_api.dto.AdministradorDTO;
import com.example.clinica.clinica_api.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @GetMapping
    public ResponseEntity<List<AdministradorDTO>> listarTodos(@RequestParam(required = false) Boolean ativos) {
        if (Boolean.TRUE.equals(ativos)) {
            return ResponseEntity.ok(administradorService.listarAtivos());
        }
        return ResponseEntity.ok(administradorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministradorDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(administradorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AdministradorDTO> criar(@RequestBody Map<String, Object> body) {
        AdministradorDTO dto = new AdministradorDTO();
        dto.setNome((String) body.get("nome"));
        dto.setEmail((String) body.get("email"));
        String senha = (String) body.get("senha");
        return ResponseEntity.status(HttpStatus.CREATED).body(administradorService.criar(dto, senha));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministradorDTO> atualizar(@PathVariable Long id, @RequestBody AdministradorDTO dto) {
        return ResponseEntity.ok(administradorService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        administradorService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
