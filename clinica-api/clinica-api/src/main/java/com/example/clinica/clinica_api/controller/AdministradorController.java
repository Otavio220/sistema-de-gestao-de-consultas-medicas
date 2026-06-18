package com.example.clinica.clinica_api.controller;

import com.example.clinica.clinica_api.entity.Especialidade;
import com.example.clinica.clinica_api.entity.Medico;
import com.example.clinica.clinica_api.entity.Usuario;
import com.example.clinica.clinica_api.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrador")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @PostMapping("/usuarios")
    public ResponseEntity<Void> gerenciarUsuarios(@RequestBody Usuario usuario) {
        administradorService.gerenciarUsuarios(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(administradorService.listarUsuarios());
    }

    @PostMapping("/medicos")
    public ResponseEntity<Void> gerenciarMedicos(@RequestBody Medico medico) {
        administradorService.gerenciarMedicos(medico);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/medicos")
    public ResponseEntity<List<Medico>> listarMedicosAtivos() {
        return ResponseEntity.ok(administradorService.listarMedicosAtivos());
    }

    @PostMapping("/consultores")
    public ResponseEntity<Void> gerenciarConsultores(@RequestBody Usuario consultor) {
        administradorService.gerenciarConsultores(consultor);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/especialidades")
    public ResponseEntity<Void> gerenciamentoEspecialidades(@RequestBody Especialidade especialidade) {
        administradorService.gerenciamentoEspecialidades(especialidade);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/especialidades")
    public ResponseEntity<List<Especialidade>> listarEspecialidades() {
        return ResponseEntity.ok(administradorService.listarEspecialidades());
    }
}
