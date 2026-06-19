package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.dto.AgendaDTO;
import com.example.clinica.clinica_api.entity.Agenda;
import com.example.clinica.clinica_api.entity.Consultorio;
import com.example.clinica.clinica_api.repository.AgendaRepository;
import com.example.clinica.clinica_api.repository.ConsultorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private ConsultorioRepository consultorioRepository;

    public List<AgendaDTO> listarTodas() {
        return agendaRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AgendaDTO> listarDisponiveis() {
        return agendaRepository.findByDisponivelTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AgendaDTO> listarPorConsultorio(Long consultorioId) {
        return agendaRepository.findByConsultorioId(consultorioId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AgendaDTO> listarPorData(LocalDate data) {
        return agendaRepository.findByData(data)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AgendaDTO> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return agendaRepository.findByDataBetween(inicio, fim)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public AgendaDTO buscarPorId(Long id) {
        return toDTO(agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda não encontrada com id: " + id)));
    }

    public AgendaDTO criar(AgendaDTO dto) {
        return toDTO(agendaRepository.save(toEntity(dto)));
    }

    public AgendaDTO bloquear(Long id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda não encontrada com id: " + id));
        agenda.setDisponivel(false);
        return toDTO(agendaRepository.save(agenda));
    }

    public AgendaDTO liberar(Long id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda não encontrada com id: " + id));
        agenda.setDisponivel(true);
        return toDTO(agendaRepository.save(agenda));
    }

    public void deletar(Long id) {
        if (!agendaRepository.existsById(id)) {
            throw new RuntimeException("Agenda não encontrada com id: " + id);
        }
        agendaRepository.deleteById(id);
    }

    private AgendaDTO toDTO(Agenda a) {
        AgendaDTO dto = new AgendaDTO();
        dto.setId(a.getId());
        dto.setData(a.getData());
        dto.setHoraInicio(a.getHoraInicio());
        dto.setHoraFim(a.getHoraFim());
        dto.setDisponivel(a.getDisponivel());
        if (a.getConsultorio() != null) {
            dto.setConsultorioId(a.getConsultorio().getId());
            dto.setConsultorioNome(a.getConsultorio().getNome());
        }
        return dto;
    }

    private Agenda toEntity(AgendaDTO dto) {
        Agenda a = new Agenda();
        a.setData(dto.getData());
        a.setHoraInicio(dto.getHoraInicio());
        a.setHoraFim(dto.getHoraFim());
        a.setDisponivel(dto.getDisponivel() != null ? dto.getDisponivel() : true);
        if (dto.getConsultorioId() != null) {
            Consultorio consultorio = consultorioRepository.findById(dto.getConsultorioId())
                    .orElseThrow(() -> new RuntimeException("Consultório não encontrado com id: " + dto.getConsultorioId()));
            a.setConsultorio(consultorio);
        }
        return a;
    }
}
