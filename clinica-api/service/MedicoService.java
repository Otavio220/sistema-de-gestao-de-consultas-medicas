package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.dto.MedicoDTO;
import com.example.clinica.clinica_api.entity.Especialidade;
import com.example.clinica.clinica_api.entity.Medico;
import com.example.clinica.clinica_api.repository.EspecialidadeRepository;
import com.example.clinica.clinica_api.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    public List<MedicoDTO> listarTodos() {
        return medicoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MedicoDTO> listarAtivos() {
        return medicoRepository.findByAtivoTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MedicoDTO> listarPorEspecialidade(Long especialidadeId) {
        return medicoRepository.findByEspecialidadeId(especialidadeId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MedicoDTO buscarPorId(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico não encontrado com id: " + id));
        return toDTO(medico);
    }

    public MedicoDTO criar(MedicoDTO dto) {
        medicoRepository.findByCrm(dto.getCrm())
                .ifPresent(m -> { throw new RuntimeException("Já existe um médico com o CRM: " + dto.getCrm()); });

        Medico medico = toEntity(dto);
        medico = medicoRepository.save(medico);
        return toDTO(medico);
    }

    public MedicoDTO atualizar(Long id, MedicoDTO dto) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico não encontrado com id: " + id));

        medico.setNome(dto.getNome());
        medico.setTelefone(dto.getTelefone());
        medico.setEmail(dto.getEmail());
        medico.setAtivo(dto.getAtivo());

        if (dto.getEspecialidadeId() != null) {
            Especialidade especialidade = especialidadeRepository.findById(dto.getEspecialidadeId())
                    .orElseThrow(() -> new RuntimeException("Especialidade não encontrada com id: " + dto.getEspecialidadeId()));
            medico.setEspecialidade(especialidade);
        }

        medico = medicoRepository.save(medico);
        return toDTO(medico);
    }

    public void inativar(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico não encontrado com id: " + id));
        medico.setAtivo(false);
        medicoRepository.save(medico);
    }

    public List<MedicoDTO> consultarAgenda(Long id) {
        medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico não encontrado com id: " + id));
        // Retorna consultas via ConsultaService - aqui retorna lista vazia como placeholder
        return List.of();
    }

    private MedicoDTO toDTO(Medico m) {
        MedicoDTO dto = new MedicoDTO();
        dto.setId(m.getId());
        dto.setNome(m.getNome());
        dto.setCrm(m.getCrm());
        dto.setTelefone(m.getTelefone());
        dto.setEmail(m.getEmail());
        dto.setAtivo(m.getAtivo());
        if (m.getEspecialidade() != null) {
            dto.setEspecialidadeId(m.getEspecialidade().getId());
            dto.setEspecialidadeNome(m.getEspecialidade().getNome());
        }
        return dto;
    }

    private Medico toEntity(MedicoDTO dto) {
        Medico m = new Medico();
        m.setNome(dto.getNome());
        m.setCrm(dto.getCrm());
        m.setTelefone(dto.getTelefone());
        m.setEmail(dto.getEmail());
        m.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        if (dto.getEspecialidadeId() != null) {
            Especialidade especialidade = especialidadeRepository.findById(dto.getEspecialidadeId())
                    .orElseThrow(() -> new RuntimeException("Especialidade não encontrada com id: " + dto.getEspecialidadeId()));
            m.setEspecialidade(especialidade);
        }
        return m;
    }
}
