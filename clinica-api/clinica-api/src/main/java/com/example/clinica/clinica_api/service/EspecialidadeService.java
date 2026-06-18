package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.dto.EspecialidadeDTO;
import com.example.clinica.clinica_api.entity.Especialidade;
import com.example.clinica.clinica_api.repository.EspecialidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EspecialidadeService {

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    public List<EspecialidadeDTO> listarTodas() {
        return especialidadeRepository.findAllByOrderByNomeAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public EspecialidadeDTO buscarPorId(Long id) {
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada com id: " + id));
        return toDTO(especialidade);
    }

    public EspecialidadeDTO criar(EspecialidadeDTO dto) {
        especialidadeRepository.findByNomeIgnoreCase(dto.getNome())
                .ifPresent(e -> { throw new RuntimeException("Já existe uma especialidade com o nome: " + dto.getNome()); });

        Especialidade especialidade = toEntity(dto);
        especialidade = especialidadeRepository.save(especialidade);
        return toDTO(especialidade);
    }

    public EspecialidadeDTO atualizar(Long id, EspecialidadeDTO dto) {
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada com id: " + id));

        especialidade.setNome(dto.getNome());
        especialidade.setDescricao(dto.getDescricao());
        especialidade.setDuracaoPadraoMinutos(dto.getDuracaoPadraoMinutos());

        especialidade = especialidadeRepository.save(especialidade);
        return toDTO(especialidade);
    }

    public void deletar(Long id) {
        if (!especialidadeRepository.existsById(id)) {
            throw new RuntimeException("Especialidade não encontrada com id: " + id);
        }
        especialidadeRepository.deleteById(id);
    }

    private EspecialidadeDTO toDTO(Especialidade e) {
        return new EspecialidadeDTO(e.getId(), e.getNome(), e.getDescricao(), e.getDuracaoPadraoMinutos());
    }

    private Especialidade toEntity(EspecialidadeDTO dto) {
        Especialidade e = new Especialidade();
        e.setNome(dto.getNome());
        e.setDescricao(dto.getDescricao());
        e.setDuracaoPadraoMinutos(dto.getDuracaoPadraoMinutos());
        return e;
    }
}
