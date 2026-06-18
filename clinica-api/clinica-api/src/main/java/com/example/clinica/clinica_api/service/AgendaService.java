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

    public AgendaDTO buscarPorId(Long id) {
        return toDTO(agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horário não encontrado com id: " + id)));
    }

    public List<AgendaDTO> listarDisponiveisPorData(LocalDate data) {
        return agendaRepository.findByDataAndDisponivelTrue(data)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AgendaDTO> listarPorConsultorio(Long consultorioId) {
        return agendaRepository.findByConsultorioId(consultorioId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public AgendaDTO criar(AgendaDTO dto) {
        Agenda agenda = new Agenda();
        preencherAgenda(agenda, dto);
        return toDTO(agendaRepository.save(agenda));
    }

    public void bloquear(Long id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horário não encontrado com id: " + id));
        agenda.setDisponivel(false);
        agendaRepository.save(agenda);
    }

    public void liberar(Long id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horário não encontrado com id: " + id));
        agenda.setDisponivel(true);
        agendaRepository.save(agenda);
    }

    private void preencherAgenda(Agenda agenda, AgendaDTO dto) {
        agenda.setData(dto.getData());
        agenda.setHoraInicio(dto.getHoraInicio());
        agenda.setHoraFim(dto.getHoraFim());
        agenda.setDisponivel(dto.getDisponivel() != null ? dto.getDisponivel() : true);
        if (dto.getConsultorioId() != null) {
            Consultorio consultorio = consultorioRepository.findById(dto.getConsultorioId())
                    .orElseThrow(() -> new RuntimeException("Consultório não encontrado com id: " + dto.getConsultorioId()));
            agenda.setConsultorio(consultorio);
        }
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
}
