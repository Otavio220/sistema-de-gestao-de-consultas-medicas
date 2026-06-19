package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.dto.EquipamentoDTO;
import com.example.clinica.clinica_api.entity.Equipamento;
import com.example.clinica.clinica_api.repository.EquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipamentoService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    public List<EquipamentoDTO> listarTodos() {
        return equipamentoRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<EquipamentoDTO> listarAtivos() {
        return equipamentoRepository.findByAtivoTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public EquipamentoDTO buscarPorId(Long id) {
        return toDTO(equipamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado com id: " + id)));
    }

    public EquipamentoDTO criar(EquipamentoDTO dto) {
        return toDTO(equipamentoRepository.save(toEntity(dto)));
    }

    public EquipamentoDTO atualizar(Long id, EquipamentoDTO dto) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado com id: " + id));
        equipamento.setNome(dto.getNome());
        equipamento.setDescricao(dto.getDescricao());
        equipamento.setAtivo(dto.getAtivo());
        return toDTO(equipamentoRepository.save(equipamento));
    }

    public void inativar(Long id) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado com id: " + id));
        equipamento.setAtivo(false);
        equipamentoRepository.save(equipamento);
    }

    public void realizarManutencao(Long id) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado com id: " + id));
        equipamento.setAtivo(false);
        equipamentoRepository.save(equipamento);
    }

    private EquipamentoDTO toDTO(Equipamento e) {
        EquipamentoDTO dto = new EquipamentoDTO();
        dto.setId(e.getId());
        dto.setNome(e.getNome());
        dto.setDescricao(e.getDescricao());
        dto.setAtivo(e.getAtivo());
        return dto;
    }

    private Equipamento toEntity(EquipamentoDTO dto) {
        Equipamento e = new Equipamento();
        e.setNome(dto.getNome());
        e.setDescricao(dto.getDescricao());
        e.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        return e;
    }
}
