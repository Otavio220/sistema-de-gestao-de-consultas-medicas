package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.entity.Consulta;
import com.example.clinica.clinica_api.entity.Paciente;
import com.example.clinica.clinica_api.entity.StatusConsulta;
import com.example.clinica.clinica_api.repository.ConsultaRepository;
import com.example.clinica.clinica_api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecepcionistaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    // Agendar consulta
    public void agendarConsulta(Consulta consulta) {
        if (consulta.getPaciente() == null) {
            throw new RuntimeException("Paciente é obrigatório para agendar consulta");
        }
        if (consulta.getMedico() == null) {
            throw new RuntimeException("Médico é obrigatório para agendar consulta");
        }
        consulta.setStatus(StatusConsulta.AGENDADA);
        consultaRepository.save(consulta);
    }

    // Reagendar consulta
    public void reagendarConsulta(Long consultaId, Consulta dadosNovos) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        consulta.setDataHora(dadosNovos.getDataHora());
        consulta.setConsultorio(dadosNovos.getConsultorio());
        consulta.setStatus(StatusConsulta.REAGENDADA);
        consultaRepository.save(consulta);
    }

    // Cancelar consulta
    public void cancelarConsulta(Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        consulta.setStatus(StatusConsulta.CANCELADA);
        consultaRepository.save(consulta);
    }

    // Cadastrar paciente
    public void cadastrarPaciente(Paciente paciente) {
        if (paciente.getNome() == null || paciente.getNome().isBlank()) {
            throw new RuntimeException("Nome do paciente é obrigatório");
        }
        if (pacienteRepository.existsByCpf(paciente.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
        pacienteRepository.save(paciente);
    }
}
