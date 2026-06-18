package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.dto.HistoricoClinicoDTO;
import com.example.clinica.clinica_api.dto.PacienteDTO;
import com.example.clinica.clinica_api.entity.HistoricoClinico;
import com.example.clinica.clinica_api.entity.Paciente;
import com.example.clinica.clinica_api.repository.HistoricoClinicoRepository;
import com.example.clinica.clinica_api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private HistoricoClinicoRepository historicoClinicoRepository;

    public List<PacienteDTO> listarTodos() {
        return pacienteRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public PacienteDTO buscarPorId(Long id) {
        return toDTO(pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com id: " + id)));
    }

    public PacienteDTO criar(PacienteDTO dto) {
        if (pacienteRepository.existsByCpf(dto.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
        Paciente paciente = toEntity(dto);
        return toDTO(pacienteRepository.save(paciente));
    }

    public PacienteDTO atualizar(Long id, PacienteDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com id: " + id));
        paciente.setNome(dto.getNome());
        paciente.setTelefone(dto.getTelefone());
        paciente.setEmail(dto.getEmail());
        paciente.setEndereco(dto.getEndereco());
        paciente.setDataNascimento(dto.getDataNascimento());
        return toDTO(pacienteRepository.save(paciente));
    }

    public void deletar(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new RuntimeException("Paciente não encontrado com id: " + id);
        }
        pacienteRepository.deleteById(id);
    }

    public List<HistoricoClinicoDTO> historico(Long pacienteId) {
        return historicoClinicoRepository.findByPacienteIdOrderByDataRegistroDesc(pacienteId)
                .stream().map(this::toHistoricoDTO).collect(Collectors.toList());
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
        Paciente paciente = new Paciente();
        paciente.setNome(dto.getNome());
        paciente.setCpf(dto.getCpf());
        paciente.setDataNascimento(dto.getDataNascimento());
        paciente.setTelefone(dto.getTelefone());
        paciente.setEmail(dto.getEmail());
        paciente.setEndereco(dto.getEndereco());
        return paciente;
    }

    private HistoricoClinicoDTO toHistoricoDTO(HistoricoClinico h) {
        HistoricoClinicoDTO dto = new HistoricoClinicoDTO();
        dto.setId(h.getId());
        dto.setDescricao(h.getDescricao());
        dto.setDataRegistro(h.getDataRegistro());
        if (h.getPaciente() != null) {
            dto.setPacienteId(h.getPaciente().getId());
            dto.setPacienteNome(h.getPaciente().getNome());
        }
        return dto;
    }
}
