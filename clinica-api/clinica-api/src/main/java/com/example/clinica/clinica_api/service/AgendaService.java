package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.entity.Agenda;
import com.example.clinica.clinica_api.repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    // Bloquear horário da agenda (marcar como indisponível)
    public void bloquear(Long id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horário não encontrado"));
        agenda.setDisponivel(false);
        agendaRepository.save(agenda);
    }

    // Liberar horário da agenda (marcar como disponível)
    public void liberar(Long id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horário não encontrado"));
        agenda.setDisponivel(true);
        agendaRepository.save(agenda);
    }

    // Listar horários disponíveis por data
    public List<Agenda> listarDisponiveisPorData(LocalDate data) {
        return agendaRepository.findByDataAndDisponivelTrue(data);
    }

    // Listar horários por consultório
    public List<Agenda> listarPorConsultorio(Long consultorioId) {
        return agendaRepository.findByConsultorioId(consultorioId);
    }
}
