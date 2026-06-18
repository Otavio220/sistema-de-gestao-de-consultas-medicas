package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.entity.HistoricoClinico;
import com.example.clinica.clinica_api.entity.Paciente;
import com.example.clinica.clinica_api.repository.HistoricoClinicoRepository;
import com.example.clinica.clinica_api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private HistoricoClinicoRepository historicoClinicoRepository;

    // Atualizar dados do paciente
    public void atualizar(Long id, Paciente dadosNovos) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        paciente.setNome(dadosNovos.getNome());
        paciente.setTelefone(dadosNovos.getTelefone());
        paciente.setEmail(dadosNovos.getEmail());
        paciente.setEndereco(dadosNovos.getEndereco());
        paciente.setDataNascimento(dadosNovos.getDataNascimento());
        pacienteRepository.save(paciente);
    }

    // Retorna histórico clínico do paciente ordenado do mais recente
    public List<HistoricoClinico> historico(Long pacienteId) {
        return historicoClinicoRepository.findByPacienteIdOrderByDataRegistroDesc(pacienteId);
    }
}
