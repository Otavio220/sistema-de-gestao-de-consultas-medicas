package com.example.clinica.clinica_api.controller;

import com.example.clinica.clinica_api.dto.UsuarioDTO;
import com.example.clinica.clinica_api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodos(@RequestParam(required = false) Boolean ativos) {
        if (Boolean.TRUE.equals(ativos)) {
            return ResponseEntity.ok(usuarioService.listarAtivos());
        }
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> criar(@RequestBody Map<String, Object> body) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome((String) body.get("nome"));
        dto.setEmail((String) body.get("email"));
        String senha = (String) body.get("senha");
        String cargo = (String) body.getOrDefault("cargo", "USUARIO");
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criar(dto, senha, cargo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        usuarioService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/autenticar")
    public ResponseEntity<Map<String, Boolean>> autenticar(@RequestBody Map<String, String> credenciais) {
        boolean autenticado = usuarioService.autenticar(
                credenciais.get("email"),
                credenciais.get("senha")
        );
        return ResponseEntity.ok(Map.of("autenticado", autenticado));
    }
}
