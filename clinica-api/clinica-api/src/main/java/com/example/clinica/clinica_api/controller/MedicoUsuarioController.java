package com.example.clinica.clinica_api.controller;

import com.example.clinica.clinica_api.entity.Consulta;
import com.example.clinica.clinica_api.entity.HistoricoClinico;
import com.example.clinica.clinica_api.service.MedicoUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medico")
public class MedicoUsuarioController {

    @Autowired
    private MedicoUsuarioService medicoUsuarioService;

    @GetMapping("/{medicoId}/agenda")
    public ResponseEntity<List<Consulta>> agenda(@PathVariable Long medicoId) {
        return ResponseEntity.ok(medicoUsuarioService.agenda(medicoId));
    }

    @GetMapping("/{medicoId}/consultas")
    public ResponseEntity<List<Consulta>> theConsultas(@PathVariable Long medicoId) {
        return ResponseEntity.ok(medicoUsuarioService.theConsultas(medicoId));
    }

    @PostMapping("/evolucao")
    public ResponseEntity<Void> registrarEvolucao(@RequestBody HistoricoClinico historico) {
        medicoUsuarioService.registrarEvolucao(historico);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
