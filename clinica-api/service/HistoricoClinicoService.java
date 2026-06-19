package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.dto.HistoricoClinicoDTO;
import com.example.clinica.clinica_api.entity.HistoricoClinico;
import com.example.clinica.clinica_api.entity.Paciente;
import com.example.clinica.clinica_api.repository.HistoricoClinicoRepository;
import com.example.clinica.clinica_api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricoClinicoService {

    @Autowired
    private HistoricoClinicoRepository historicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<HistoricoClinicoDTO> listarPorPaciente(Long pacienteId) {
        return historicoRepository.findByPacienteIdOrderByDataRegistroDesc(pacienteId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public HistoricoClinicoDTO buscarPorId(Long id) {
        return toDTO(historicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Histórico não encontrado com id: " + id)));
    }

    public HistoricoClinicoDTO adicionarRegistro(HistoricoClinicoDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com id: " + dto.getPacienteId()));
        HistoricoClinico historico = new HistoricoClinico();
        historico.setPaciente(paciente);
        historico.setDescricao(dto.getDescricao());
        return toDTO(historicoRepository.save(historico));
    }

    public void deletar(Long id) {
        if (!historicoRepository.existsById(id)) {
            throw new RuntimeException("Histórico não encontrado com id: " + id);
        }
        historicoRepository.deleteById(id);
    }

    private HistoricoClinicoDTO toDTO(HistoricoClinico h) {
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
