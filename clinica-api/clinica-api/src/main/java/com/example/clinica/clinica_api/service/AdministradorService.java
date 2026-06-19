package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.dto.AdministradorDTO;
import com.example.clinica.clinica_api.entity.Administrador;
import com.example.clinica.clinica_api.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    public List<AdministradorDTO> listarTodos() {
        return administradorRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AdministradorDTO> listarAtivos() {
        return administradorRepository.findByAtivoTrue().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public AdministradorDTO buscarPorId(Long id) {
        return toDTO(administradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador não encontrado com id: " + id)));
    }

    public AdministradorDTO criar(AdministradorDTO dto, String senha) {
        administradorRepository.findByEmail(dto.getEmail()).ifPresent(a -> {
            throw new RuntimeException("Já existe um usuário com o e-mail: " + dto.getEmail());
        });
        Administrador a = new Administrador();
        a.setNome(dto.getNome());
        a.setEmail(dto.getEmail());
        a.setSenha(senha);
        a.setAtivo(true);
        return toDTO(administradorRepository.save(a));
    }

    public AdministradorDTO atualizar(Long id, AdministradorDTO dto) {
        Administrador a = administradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador não encontrado com id: " + id));
        a.setNome(dto.getNome());
        a.setEmail(dto.getEmail());
        a.setAtivo(dto.getAtivo());
        return toDTO(administradorRepository.save(a));
    }

    public void inativar(Long id) {
        Administrador a = administradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador não encontrado com id: " + id));
        a.setAtivo(false);
        administradorRepository.save(a);
    }

    private AdministradorDTO toDTO(Administrador a) {
        AdministradorDTO dto = new AdministradorDTO();
        dto.setId(a.getId());
        dto.setNome(a.getNome());
        dto.setEmail(a.getEmail());
        dto.setAtivo(a.getAtivo());
        return dto;
    }
}
