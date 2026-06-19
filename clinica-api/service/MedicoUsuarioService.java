package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.dto.MedicoUsuarioDTO;
import com.example.clinica.clinica_api.entity.MedicoUsuario;
import com.example.clinica.clinica_api.repository.MedicoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicoUsuarioService {

    @Autowired
    private MedicoUsuarioRepository medicoUsuarioRepository;

    public List<MedicoUsuarioDTO> listarTodos() {
        return medicoUsuarioRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<MedicoUsuarioDTO> listarAtivos() {
        return medicoUsuarioRepository.findByAtivoTrue().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public MedicoUsuarioDTO buscarPorId(Long id) {
        return toDTO(medicoUsuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MédicoUsuário não encontrado com id: " + id)));
    }

    public MedicoUsuarioDTO criar(MedicoUsuarioDTO dto, String senha) {
        medicoUsuarioRepository.findByEmail(dto.getEmail()).ifPresent(m -> {
            throw new RuntimeException("Já existe um usuário com o e-mail: " + dto.getEmail());
        });
        MedicoUsuario m = new MedicoUsuario();
        m.setNome(dto.getNome());
        m.setEmail(dto.getEmail());
        m.setSenha(senha);
        m.setAtivo(true);
        return toDTO(medicoUsuarioRepository.save(m));
    }

    public MedicoUsuarioDTO atualizar(Long id, MedicoUsuarioDTO dto) {
        MedicoUsuario m = medicoUsuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MédicoUsuário não encontrado com id: " + id));
        m.setNome(dto.getNome());
        m.setEmail(dto.getEmail());
        m.setAtivo(dto.getAtivo());
        return toDTO(medicoUsuarioRepository.save(m));
    }

    public void inativar(Long id) {
        MedicoUsuario m = medicoUsuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MédicoUsuário não encontrado com id: " + id));
        m.setAtivo(false);
        medicoUsuarioRepository.save(m);
    }

    private MedicoUsuarioDTO toDTO(MedicoUsuario m) {
        MedicoUsuarioDTO dto = new MedicoUsuarioDTO();
        dto.setId(m.getId());
        dto.setNome(m.getNome());
        dto.setEmail(m.getEmail());
        dto.setAtivo(m.getAtivo());
        return dto;
    }
}
