package com.example.clinica.clinica_api.controller;

import com.example.clinica.clinica_api.dto.MedicoUsuarioDTO;
import com.example.clinica.clinica_api.service.MedicoUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medico-usuarios")
public class MedicoUsuarioController {

    @Autowired
    private MedicoUsuarioService medicoUsuarioService;

    @GetMapping
    public ResponseEntity<List<MedicoUsuarioDTO>> listarTodos(@RequestParam(required = false) Boolean ativos) {
        if (Boolean.TRUE.equals(ativos)) {
            return ResponseEntity.ok(medicoUsuarioService.listarAtivos());
        }
        return ResponseEntity.ok(medicoUsuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoUsuarioDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoUsuarioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<MedicoUsuarioDTO> criar(@RequestBody Map<String, Object> body) {
        MedicoUsuarioDTO dto = new MedicoUsuarioDTO();
        dto.setNome((String) body.get("nome"));
        dto.setEmail((String) body.get("email"));
        String senha = (String) body.get("senha");
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoUsuarioService.criar(dto, senha));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoUsuarioDTO> atualizar(@PathVariable Long id, @RequestBody MedicoUsuarioDTO dto) {
        return ResponseEntity.ok(medicoUsuarioService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        medicoUsuarioService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
