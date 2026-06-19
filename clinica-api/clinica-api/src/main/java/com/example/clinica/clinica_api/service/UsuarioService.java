package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.dto.UsuarioDTO;
import com.example.clinica.clinica_api.entity.*;
import com.example.clinica.clinica_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<UsuarioDTO> listarAtivos() {
        return usuarioRepository.findByAtivoTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
        return toDTO(usuario);
    }

    public UsuarioDTO criar(UsuarioDTO dto, String senha, String cargo) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Já existe um usuário com o e-mail: " + dto.getEmail());
        }
        Usuario usuario = criarPorCargo(cargo);
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(senha);
        usuario.setAtivo(true);
        return toDTO(usuarioRepository.save(usuario));
    }

    private Usuario criarPorCargo(String cargo) {
        if (cargo == null) return new Usuario();
        if (cargo.equalsIgnoreCase("RECEPCIONISTA")) return new Recepcionista();
        if (cargo.equalsIgnoreCase("MEDICO_USUARIO")) return new MedicoUsuario();
        if (cargo.equalsIgnoreCase("ADMINISTRADOR")) return new Administrador();
        return new Usuario();
    }

    public UsuarioDTO atualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setAtivo(dto.getAtivo());
        return toDTO(usuarioRepository.save(usuario));
    }

    public boolean autenticar(String email, String senha) {
        return usuarioRepository.findByEmail(email)
                .map(u -> u.autenticar(senha))
                .orElse(false);
    }

    public void inativar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    public UsuarioDTO toDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getId());
        dto.setNome(u.getNome());
        dto.setEmail(u.getEmail());
        dto.setAtivo(u.getAtivo());

        String cargo;
        if (u instanceof Administrador) {
            cargo = "ADMINISTRADOR";
        } else if (u instanceof MedicoUsuario) {
            cargo = "MEDICO_USUARIO";
        } else if (u instanceof Recepcionista) {
            cargo = "RECEPCIONISTA";
        } else {
            cargo = "USUARIO";
        }

        dto.setCargo(cargo);
        return dto;
    }
}