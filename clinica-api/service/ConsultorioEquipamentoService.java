package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.dto.ConsultorioEquipamentoDTO;
import com.example.clinica.clinica_api.entity.*;
import com.example.clinica.clinica_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultorioEquipamentoService {

    @Autowired
    private ConsultorioEquipamentoRepository consultorioEquipamentoRepository;

    @Autowired
    private ConsultorioRepository consultorioRepository;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    public List<ConsultorioEquipamentoDTO> listarPorConsultorio(Long consultorioId) {
        return consultorioEquipamentoRepository.findByConsultorioId(consultorioId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ConsultorioEquipamentoDTO> listarPorEquipamento(Long equipamentoId) {
        return consultorioEquipamentoRepository.findByEquipamentoId(equipamentoId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ConsultorioEquipamentoDTO vincular(Long consultorioId, Long equipamentoId) {
        Consultorio consultorio = consultorioRepository.findById(consultorioId)
                .orElseThrow(() -> new RuntimeException("Consultório não encontrado com id: " + consultorioId));
        Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado com id: " + equipamentoId));

        ConsultorioEquipamento ce = new ConsultorioEquipamento(consultorio, equipamento);
        return toDTO(consultorioEquipamentoRepository.save(ce));
    }

    public void desvincular(Long consultorioId, Long equipamentoId) {
        ConsultorioEquipamentoId id = new ConsultorioEquipamentoId(consultorioId, equipamentoId);
        if (!consultorioEquipamentoRepository.existsById(id)) {
            throw new RuntimeException("Vínculo não encontrado entre consultório " + consultorioId + " e equipamento " + equipamentoId);
        }
        consultorioEquipamentoRepository.deleteById(id);
    }

    private ConsultorioEquipamentoDTO toDTO(ConsultorioEquipamento ce) {
        ConsultorioEquipamentoDTO dto = new ConsultorioEquipamentoDTO();
        dto.setConsultorioId(ce.getConsultorio().getId());
        dto.setConsultorioNome(ce.getConsultorio().getNome());
        dto.setEquipamentoId(ce.getEquipamento().getId());
        dto.setEquipamentoNome(ce.getEquipamento().getNome());
        return dto;
    }
}
