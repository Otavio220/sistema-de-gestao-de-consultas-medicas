package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.dto.PacienteDTO;
import com.example.clinica.clinica_api.entity.Paciente;
import com.example.clinica.clinica_api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<PacienteDTO> listarTodos() {
        return pacienteRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public PacienteDTO buscarPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com id: " + id));
        return toDTO(paciente);
    }

    public List<PacienteDTO> buscarPorNome(String nome) {
        return pacienteRepository.findByNomeContainingIgnoreCase(nome)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public PacienteDTO criar(PacienteDTO dto) {
        if (dto.getCpf() != null && pacienteRepository.existsByCpf(dto.getCpf())) {
            throw new RuntimeException("Já existe um paciente com o CPF: " + dto.getCpf());
        }
        Paciente paciente = toEntity(dto);
        return toDTO(pacienteRepository.save(paciente));
    }

    public PacienteDTO atualizar(Long id, PacienteDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com id: " + id));
        paciente.setNome(dto.getNome());
        paciente.setCpf(dto.getCpf());
        paciente.setDataNascimento(dto.getDataNascimento());
        paciente.setTelefone(dto.getTelefone());
        paciente.setEmail(dto.getEmail());
        paciente.setEndereco(dto.getEndereco());
        return toDTO(pacienteRepository.save(paciente));
    }

    public void deletar(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new RuntimeException("Paciente não encontrado com id: " + id);
        }
        pacienteRepository.deleteById(id);
    }

    private PacienteDTO toDTO(Paciente p) {
        PacienteDTO dto = new PacienteDTO();
        dto.setId(p.getId());
        dto.setNome(p.getNome());
        dto.setCpf(p.getCpf());
        dto.setDataNascimento(p.getDataNascimento());
        dto.setTelefone(p.getTelefone());
        dto.setEmail(p.getEmail());
        dto.setEndereco(p.getEndereco());
        return dto;
    }

    private Paciente toEntity(PacienteDTO dto) {
        Paciente p = new Paciente();
        p.setNome(dto.getNome());
        p.setCpf(dto.getCpf());
        p.setDataNascimento(dto.getDataNascimento());
        p.setTelefone(dto.getTelefone());
        p.setEmail(dto.getEmail());
        p.setEndereco(dto.getEndereco());
        return p;
    }
}
