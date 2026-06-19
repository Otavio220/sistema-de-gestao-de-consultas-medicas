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

    // ---------------------------------------------------------------
    // LISTAR
    // ---------------------------------------------------------------

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

    // ---------------------------------------------------------------
    // AGENDAR — com atribuição automática e validação de conflito
    // ---------------------------------------------------------------

    public ConsultaDTO agendar(ConsultaDTO dto) {
        Consulta consulta = new Consulta();

        // 1. Busca o paciente
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com id: " + dto.getPacienteId()));
        consulta.setPaciente(paciente);
        consulta.setDataHora(dto.getDataHora());
        consulta.setMotivo(dto.getMotivo());
        consulta.setObservacoes(dto.getObservacoes());
        consulta.setRequisitosPrevios(dto.getRequisitosPrevios());

        // 2. Resolve o médico: manual (medicoId) ou automático (especialidadeId)
        Medico medico = resolverMedico(dto);
        consulta.setMedico(medico);

        // 3. Define duração: usa a do DTO ou a padrão da especialidade do médico
        int duracao = resolverDuracao(dto, medico);
        consulta.setDuracaoMinutos(duracao);

        LocalDateTime inicio = dto.getDataHora();
        LocalDateTime fim = inicio.plusMinutes(duracao);

        // 4. Valida conflito de horário do médico
        validarConflitoPorMedico(medico.getId(), inicio, fim, null);

        // 5. Resolve o consultório: manual ou automático
        Consultorio consultorio = resolverConsultorio(dto, inicio, fim);
        consulta.setConsultorio(consultorio);

        // 6. Valida conflito de horário do consultório
        if (consultorio != null) {
            validarConflitoPorConsultorio(consultorio.getId(), inicio, fim, null);
        }

        consulta.setStatus(StatusConsulta.AGENDADA);
        Consulta salva = consultaRepository.save(consulta);

        // 7. Monta o DTO de retorno com aviso de requisitos prévios
        ConsultaDTO resposta = toDTO(salva);
        montarAvisoRequisitosPrevios(resposta, salva);
        return resposta;
    }

    // ---------------------------------------------------------------
    // REAGENDAR — com validação de conflito no novo horário
    // ---------------------------------------------------------------

    public ConsultaDTO reagendar(Long id, LocalDateTime novaDataHora) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada com id: " + id));

        int duracao = consulta.getDuracaoMinutos() != null ? consulta.getDuracaoMinutos() : 30;
        LocalDateTime novoInicio = novaDataHora;
        LocalDateTime novoFim = novaDataHora.plusMinutes(duracao);

        // Valida conflito do médico no novo horário (ignora a própria consulta)
        validarConflitoPorMedico(consulta.getMedico().getId(), novoInicio, novoFim, id);

        // Valida conflito do consultório no novo horário (ignora a própria consulta)
        if (consulta.getConsultorio() != null) {
            validarConflitoPorConsultorio(consulta.getConsultorio().getId(), novoInicio, novoFim, id);
        }

        consulta.setDataHora(novaDataHora);
        consulta.setStatus(StatusConsulta.REAGENDADA);

        ConsultaDTO resposta = toDTO(consultaRepository.save(consulta));
        montarAvisoRequisitosPrevios(resposta, consulta);
        return resposta;
    }

    // ---------------------------------------------------------------
    // CANCELAR / CONFIRMAR / ATUALIZAR
    // ---------------------------------------------------------------

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
        consulta.setDataHora(dto.getDataHora());
        consulta.setDuracaoMinutos(dto.getDuracaoMinutos());
        consulta.setMotivo(dto.getMotivo());
        consulta.setObservacoes(dto.getObservacoes());
        consulta.setRequisitosPrevios(dto.getRequisitosPrevios());
        return toDTO(consultaRepository.save(consulta));
    }

    // ---------------------------------------------------------------
    // MÉTODOS AUXILIARES PRIVADOS
    // ---------------------------------------------------------------

    /**
     * Se o DTO trouxer medicoId, usa esse médico.
     * Caso contrário, busca automaticamente pelo especialidadeId.
     */
    private Medico resolverMedico(ConsultaDTO dto) {
        if (dto.getMedicoId() != null) {
            return medicoRepository.findById(dto.getMedicoId())
                    .orElseThrow(() -> new RuntimeException("Médico não encontrado com id: " + dto.getMedicoId()));
        }

        if (dto.getEspecialidadeId() == null) {
            throw new RuntimeException("Informe o medicoId ou o especialidadeId para agendar a consulta.");
        }

        // Descobre quais médicos já estão ocupados naquele horário
        int duracao = dto.getDuracaoMinutos() != null ? dto.getDuracaoMinutos() : 30;
        LocalDateTime inicio = dto.getDataHora();
        LocalDateTime fim = inicio.plusMinutes(duracao);

        List<Long> medicosOcupados = consultaRepository
                .findMedicoOcupadosPorEspecialidadeEHorario(dto.getEspecialidadeId(), inicio, fim);

        List<Medico> disponiveis;
        if (medicosOcupados.isEmpty()) {
            disponiveis = medicoRepository.findAtivosByEspecialidade(dto.getEspecialidadeId());
        } else {
            disponiveis = medicoRepository.findMedicosDisponiveisPorEspecialidade(
                    dto.getEspecialidadeId(), medicosOcupados);
        }

        if (disponiveis.isEmpty()) {
            throw new RuntimeException(
                    "Nenhum médico disponível para a especialidade informada no horário solicitado.");
        }

        // Retorna o primeiro médico disponível
        return disponiveis.get(0);
    }

    /**
     * Usa a duração do DTO se informada; caso contrário usa a duração padrão da especialidade.
     */
    private int resolverDuracao(ConsultaDTO dto, Medico medico) {
        if (dto.getDuracaoMinutos() != null && dto.getDuracaoMinutos() > 0) {
            return dto.getDuracaoMinutos();
        }
        if (medico.getEspecialidade() != null
                && medico.getEspecialidade().getDuracaoPadraoMinutos() != null) {
            return medico.getEspecialidade().getDuracaoPadraoMinutos();
        }
        return 30; // padrão geral: 30 minutos
    }

    /**
     * Se consultorioId for informado, usa esse consultório.
     * Caso contrário, busca automaticamente o primeiro disponível no horário.
     */
    private Consultorio resolverConsultorio(ConsultaDTO dto, LocalDateTime inicio, LocalDateTime fim) {
        if (dto.getConsultorioId() != null) {
            return consultorioRepository.findById(dto.getConsultorioId())
                    .orElseThrow(() -> new RuntimeException(
                            "Consultório não encontrado com id: " + dto.getConsultorioId()));
        }

        // Busca consultórios ocupados no horário
        List<Long> ocupados = consultaRepository.findConsultoriosOcupadosNoHorario(inicio, fim);

        List<Consultorio> todosAtivos = consultorioRepository.findByAtivoTrue();

        return todosAtivos.stream()
                .filter(c -> !ocupados.contains(c.getId()))
                .findFirst()
                .orElse(null); // Não bloqueia o agendamento se não houver consultório disponível
    }

    /**
     * Lança exceção se o médico já tiver consulta no intervalo.
     * consultaId é passado para ignorar a própria consulta no reagendamento.
     */
    private void validarConflitoPorMedico(Long medicoId, LocalDateTime inicio,
                                          LocalDateTime fim, Long consultaId) {
        boolean conflito = consultaRepository.existeConflitoPorMedico(medicoId, inicio, fim, consultaId);
        if (conflito) {
            throw new RuntimeException(
                    "O médico já possui uma consulta agendada nesse horário. Escolha outro horário.");
        }
    }

    /**
     * Lança exceção se o consultório já estiver ocupado no intervalo.
     */
    private void validarConflitoPorConsultorio(Long consultorioId, LocalDateTime inicio,
                                               LocalDateTime fim, Long consultaId) {
        boolean conflito = consultaRepository.existeConflitoPorConsultorio(
                consultorioId, inicio, fim, consultaId);
        if (conflito) {
            throw new RuntimeException(
                    "O consultório já está ocupado nesse horário. Escolha outro horário ou consultório.");
        }
    }

    /**
     * Se a consulta tiver requisitos prévios, adiciona um aviso no retorno da API.
     */
    private void montarAvisoRequisitosPrevios(ConsultaDTO dto, Consulta consulta) {
        if (consulta.getRequisitosPrevios() != null && !consulta.getRequisitosPrevios().isBlank()) {
            dto.setAvisoRequisitosPrevios(
                    "ATENÇÃO — Requisitos prévios para esta consulta: " + consulta.getRequisitosPrevios());
        }
    }

    // ---------------------------------------------------------------
    // CONVERSÃO Entidade → DTO
    // ---------------------------------------------------------------

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
            if (c.getMedico().getEspecialidade() != null) {
                dto.setEspecialidadeId(c.getMedico().getEspecialidade().getId());
                dto.setEspecialidadeNome(c.getMedico().getEspecialidade().getNome());
            }
        }
        if (c.getConsultorio() != null) {
            dto.setConsultorioId(c.getConsultorio().getId());
            dto.setConsultorioNome(c.getConsultorio().getNome());
        }
        return dto;
    }
}