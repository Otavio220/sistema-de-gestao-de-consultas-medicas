package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.dto.ConsultaDTO;
import com.example.clinica.clinica_api.entity.*;
import com.example.clinica.clinica_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ConsultorioRepository consultorioRepository;

    public List<ConsultaDTO> listarTodas() {
        return consultaRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ConsultaDTO buscarPorId(Long id) {
        return toDTO(consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada com id: " + id)));
    }

    public List<ConsultaDTO> listarPorPaciente(Long pacienteId) {
        return consultaRepository.findByPacienteId(pacienteId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ConsultaDTO> listarPorMedico(Long medicoId) {
        return consultaRepository.findByMedicoId(medicoId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ConsultaDTO> listarPorStatus(StatusConsulta status) {
        return consultaRepository.findByStatus(status)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ConsultaDTO agendar(ConsultaDTO dto) {
        Consulta consulta = new Consulta();
        preencherConsulta(consulta, dto);
        consulta.setStatus(StatusConsulta.AGENDADA);
        return toDTO(consultaRepository.save(consulta));
    }

    public ConsultaDTO reagendar(Long id, LocalDateTime novaDataHora) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada com id: " + id));
        consulta.setDataHora(novaDataHora);
        consulta.setStatus(StatusConsulta.REAGENDADA);
        return toDTO(consultaRepository.save(consulta));
    }

    public ConsultaDTO cancelar(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada com id: " + id));
        consulta.setStatus(StatusConsulta.CANCELADA);
        return toDTO(consultaRepository.save(consulta));
    }

    public ConsultaDTO confirmar(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada com id: " + id));
        consulta.setStatus(StatusConsulta.CONFIRMADA);
        return toDTO(consultaRepository.save(consulta));
    }

    public ConsultaDTO atualizar(Long id, ConsultaDTO dto) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada com id: " + id));
        preencherConsulta(consulta, dto);
        return toDTO(consultaRepository.save(consulta));
    }

    private void preencherConsulta(Consulta consulta, ConsultaDTO dto) {
        consulta.setDataHora(dto.getDataHora());
        consulta.setDuracaoMinutos(dto.getDuracaoMinutos());
        consulta.setMotivo(dto.getMotivo());
        consulta.setObservacoes(dto.getObservacoes());
        consulta.setRequisitosPrevios(dto.getRequisitosPrevios());

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com id: " + dto.getPacienteId()));
        consulta.setPaciente(paciente);

        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Médico não encontrado com id: " + dto.getMedicoId()));
        consulta.setMedico(medico);

        if (dto.getConsultorioId() != null) {
            Consultorio consultorio = consultorioRepository.findById(dto.getConsultorioId())
                    .orElseThrow(() -> new RuntimeException("Consultório não encontrado com id: " + dto.getConsultorioId()));
            consulta.setConsultorio(consultorio);
        }
    }

    private ConsultaDTO toDTO(Consulta c) {
        ConsultaDTO dto = new ConsultaDTO();
        dto.setId(c.getId());
        dto.setDataHora(c.getDataHora());
        dto.setDuracaoMinutos(c.getDuracaoMinutos());
        dto.setStatus(c.getStatus());
        dto.setMotivo(c.getMotivo());
        dto.setObservacoes(c.getObservacoes());
        dto.setRequisitosPrevios(c.getRequisitosPrevios());
        if (c.getPaciente() != null) {
            dto.setPacienteId(c.getPaciente().getId());
            dto.setPacienteNome(c.getPaciente().getNome());
        }
        if (c.getMedico() != null) {
            dto.setMedicoId(c.getMedico().getId());
            dto.setMedicoNome(c.getMedico().getNome());
        }
        if (c.getConsultorio() != null) {
            dto.setConsultorioId(c.getConsultorio().getId());
            dto.setConsultorioNome(c.getConsultorio().getNome());
        }
        return dto;
    }
}
