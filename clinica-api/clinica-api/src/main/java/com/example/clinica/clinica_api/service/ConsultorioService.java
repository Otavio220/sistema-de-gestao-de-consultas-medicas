package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.dto.ConsultorioDTO;
import com.example.clinica.clinica_api.entity.Consultorio;
import com.example.clinica.clinica_api.repository.ConsultorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultorioService {

    @Autowired
    private ConsultorioRepository consultorioRepository;

    public List<ConsultorioDTO> listarTodos() {
        return consultorioRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ConsultorioDTO> listarAtivos() {
        return consultorioRepository.findByAtivoTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ConsultorioDTO buscarPorId(Long id) {
        return toDTO(consultorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultório não encontrado com id: " + id)));
    }

    public ConsultorioDTO criar(ConsultorioDTO dto) {
        return toDTO(consultorioRepository.save(toEntity(dto)));
    }

    public ConsultorioDTO atualizar(Long id, ConsultorioDTO dto) {
        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultório não encontrado com id: " + id));
        consultorio.setNome(dto.getNome());
        consultorio.setAndar(dto.getAndar());
        consultorio.setDescricao(dto.getDescricao());
        consultorio.setAtivo(dto.getAtivo());
        return toDTO(consultorioRepository.save(consultorio));
    }

    public void inativar(Long id) {
        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultório não encontrado com id: " + id));
        consultorio.setAtivo(false);
        consultorioRepository.save(consultorio);
    }

    private ConsultorioDTO toDTO(Consultorio c) {
        ConsultorioDTO dto = new ConsultorioDTO();
        dto.setId(c.getId());
        dto.setNome(c.getNome());
        dto.setAndar(c.getAndar());
        dto.setDescricao(c.getDescricao());
        dto.setAtivo(c.getAtivo());
        return dto;
    }

    private Consultorio toEntity(ConsultorioDTO dto) {
        Consultorio c = new Consultorio();
        c.setNome(dto.getNome());
        c.setAndar(dto.getAndar());
        c.setDescricao(dto.getDescricao());
        c.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        return c;
    }
}
