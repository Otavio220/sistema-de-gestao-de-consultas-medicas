package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.entity.Consulta;
import com.example.clinica.clinica_api.entity.HistoricoClinico;
import com.example.clinica.clinica_api.repository.ConsultaRepository;
import com.example.clinica.clinica_api.repository.HistoricoClinicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoUsuarioService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private HistoricoClinicoRepository historicoClinicoRepository;

    // Retorna a agenda do médico (consultas agendadas)
    public List<Consulta> agenda(Long medicoId) {
        return consultaRepository.findAll()
                .stream()
                .filter(c -> c.getMedico() != null && c.getMedico().getId().equals(medicoId))
                .toList();
    }

    // Retorna todas as consultas do médico
    public List<Consulta> theConsultas(Long medicoId) {
        return consultaRepository.findAll()
                .stream()
                .filter(c -> c.getMedico() != null && c.getMedico().getId().equals(medicoId))
                .toList();
    }

    // Registra evolução no histórico clínico do paciente
    public void registrarEvolucao(HistoricoClinico historico) {
        if (historico.getPaciente() == null) {
            throw new RuntimeException("Paciente é obrigatório para registrar evolução");
        }
        historicoClinicoRepository.save(historico);
    }
}
