package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.dto.RecepcionistaDTO;
import com.example.clinica.clinica_api.entity.Recepcionista;
import com.example.clinica.clinica_api.repository.RecepcionistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecepcionistaService {

    @Autowired
    private RecepcionistaRepository recepcionistaRepository;

    public List<RecepcionistaDTO> listarTodos() {
        return recepcionistaRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<RecepcionistaDTO> listarAtivos() {
        return recepcionistaRepository.findByAtivoTrue().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public RecepcionistaDTO buscarPorId(Long id) {
        return toDTO(recepcionistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recepcionista não encontrado com id: " + id)));
    }

    public RecepcionistaDTO criar(RecepcionistaDTO dto, String senha) {
        recepcionistaRepository.findByEmail(dto.getEmail()).ifPresent(r -> {
            throw new RuntimeException("Já existe um usuário com o e-mail: " + dto.getEmail());
        });
        Recepcionista r = new Recepcionista();
        r.setNome(dto.getNome());
        r.setEmail(dto.getEmail());
        r.setSenha(senha);
        r.setAtivo(true);
        return toDTO(recepcionistaRepository.save(r));
    }

    public RecepcionistaDTO atualizar(Long id, RecepcionistaDTO dto) {
        Recepcionista r = recepcionistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recepcionista não encontrado com id: " + id));
        r.setNome(dto.getNome());
        r.setEmail(dto.getEmail());
        r.setAtivo(dto.getAtivo());
        return toDTO(recepcionistaRepository.save(r));
    }

    public void inativar(Long id) {
        Recepcionista r = recepcionistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recepcionista não encontrado com id: " + id));
        r.setAtivo(false);
        recepcionistaRepository.save(r);
    }

    private RecepcionistaDTO toDTO(Recepcionista r) {
        RecepcionistaDTO dto = new RecepcionistaDTO();
        dto.setId(r.getId());
        dto.setNome(r.getNome());
        dto.setEmail(r.getEmail());
        dto.setAtivo(r.getAtivo());
        return dto;
    }
}
