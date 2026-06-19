package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.dto.ConsultaDTO;
import com.example.clinica.clinica_api.entity.*;
import com.example.clinica.clinica_api.exception.RecursoNaoEncontradoException;
import com.example.clinica.clinica_api.exception.RegraNegocioException;
import com.example.clinica.clinica_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    /**
     * Duração padrão (em minutos) usada quando nem a consulta nem a
     * especialidade informam uma duração explícita.
     */
    private static final int DURACAO_PADRAO_MINUTOS = 30;

    /**
     * Status que NÃO bloqueiam o horário de um médico/consultório,
     * pois a consulta não vai mais ocorrer (ou já ocorreu).
     */
    private static final List<StatusConsulta> STATUS_QUE_NAO_OCUPAM_HORARIO =
            List.of(StatusConsulta.CANCELADA, StatusConsulta.NAO_COMPARECEU);

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ConsultorioRepository consultorioRepository;

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    // ---------------------------------------------------------------
    // Consultas (leitura)
    // ---------------------------------------------------------------

    public List<ConsultaDTO> listarTodas() {
        return consultaRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ConsultaDTO buscarPorId(Long id) {
        return toDTO(buscarConsultaOuFalhar(id));
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
    // Agendamento
    // ---------------------------------------------------------------

    /**
     * Agenda uma nova consulta.
     *
     * Regras aplicadas:
     * - O paciente é obrigatório.
     * - É necessário informar OU um médico (medicoId) OU uma especialidade
     *   (especialidadeId). Quando só a especialidade é informada, o sistema
     *   busca automaticamente um médico ativo daquela especialidade que
     *   esteja livre no horário solicitado.
     * - Se um médico for informado e também uma especialidade, valida que o
     *   médico realmente pertence àquela especialidade.
     * - O consultório é opcional: se não informado, o sistema atribui
     *   automaticamente um consultório ativo livre no horário. Se informado,
     *   valida que está ativo e disponível naquele horário.
     * - A duração da consulta vem do DTO; na ausência dela, usa a duração
     *   padrão da especialidade; na ausência de ambas, usa um padrão fixo.
     * - Caso a consulta tenha requisitos prévios cadastrados (jejum, exames
     *   anteriores, idade mínima, etc.), um aviso é devolvido no DTO de
     *   resposta para que o atendente notifique o paciente.
     */
    public ConsultaDTO agendar(ConsultaDTO dto) {
        Paciente paciente = buscarPacienteOuFalhar(dto.getPacienteId());

        int duracaoMinutos = resolverDuracao(dto);
        LocalDateTime inicio = dto.getDataHora();
        if (inicio == null) {
            throw new RegraNegocioException("A data/hora da consulta é obrigatória.");
        }
        LocalDateTime fim = inicio.plusMinutes(duracaoMinutos);

        Medico medico = resolverMedico(dto, inicio, fim, null);
        Consultorio consultorio = resolverConsultorio(dto, inicio, fim, null);

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setConsultorio(consultorio);
        consulta.setDataHora(inicio);
        consulta.setDuracaoMinutos(duracaoMinutos);
        consulta.setMotivo(dto.getMotivo());
        consulta.setObservacoes(dto.getObservacoes());
        consulta.setRequisitosPrevios(dto.getRequisitosPrevios());
        consulta.setStatus(StatusConsulta.AGENDADA);

        consulta = consultaRepository.save(consulta);

        ConsultaDTO resposta = toDTO(consulta);
        resposta.setAvisoRequisitosPrevios(montarAvisoRequisitosPrevios(consulta));
        return resposta;
    }

    /**
     * Reagenda uma consulta existente para uma nova data/hora, reaproveitando
     * o mesmo médico e consultório (a disponibilidade é revalidada para o
     * novo horário, excluindo a própria consulta da verificação de conflito).
     */
    public ConsultaDTO reagendar(Long id, LocalDateTime novaDataHora) {
        Consulta consulta = buscarConsultaOuFalhar(id);

        if (novaDataHora == null) {
            throw new RegraNegocioException("A nova data/hora é obrigatória para reagendar.");
        }

        int duracaoMinutos = consulta.getDuracaoMinutos() != null
                ? consulta.getDuracaoMinutos() : DURACAO_PADRAO_MINUTOS;
        LocalDateTime novoFim = novaDataHora.plusMinutes(duracaoMinutos);

        if (consulta.getMedico() != null
                && haConflitoDeMedico(consulta.getMedico().getId(), novaDataHora, novoFim, consulta.getId())) {
            throw new RegraNegocioException(
                    "O médico já possui outra consulta nesse novo horário. Escolha outro horário.");
        }

        if (consulta.getConsultorio() != null
                && haConflitoDeConsultorio(consulta.getConsultorio().getId(), novaDataHora, novoFim, consulta.getId())) {
            throw new RegraNegocioException(
                    "O consultório já está ocupado nesse novo horário. Escolha outro horário.");
        }

        consulta.setDataHora(novaDataHora);
        consulta.setStatus(StatusConsulta.REAGENDADA);
        return toDTO(consultaRepository.save(consulta));
    }

    /**
     * Cancela uma consulta. O horário do médico/consultório é liberado
     * automaticamente, pois consultas CANCELADAS não entram nas verificações
     * de conflito de agendamento.
     */
    public ConsultaDTO cancelar(Long id) {
        Consulta consulta = buscarConsultaOuFalhar(id);
        consulta.setStatus(StatusConsulta.CANCELADA);
        return toDTO(consultaRepository.save(consulta));
    }

    public ConsultaDTO confirmar(Long id) {
        Consulta consulta = buscarConsultaOuFalhar(id);
        consulta.setStatus(StatusConsulta.CONFIRMADA);
        return toDTO(consultaRepository.save(consulta));
    }

    public ConsultaDTO marcarRealizada(Long id) {
        Consulta consulta = buscarConsultaOuFalhar(id);
        consulta.setStatus(StatusConsulta.REALIZADA);
        return toDTO(consultaRepository.save(consulta));
    }

    public ConsultaDTO marcarNaoCompareceu(Long id) {
        Consulta consulta = buscarConsultaOuFalhar(id);
        consulta.setStatus(StatusConsulta.NAO_COMPARECEU);
        return toDTO(consultaRepository.save(consulta));
    }

    /**
     * Atualização "completa" de uma consulta já existente (dados gerais),
     * revalidando disponibilidade de médico/consultório para o horário informado.
     */
    public ConsultaDTO atualizar(Long id, ConsultaDTO dto) {
        Consulta consulta = buscarConsultaOuFalhar(id);

        int duracaoMinutos = resolverDuracao(dto);
        LocalDateTime inicio = dto.getDataHora() != null ? dto.getDataHora() : consulta.getDataHora();
        LocalDateTime fim = inicio.plusMinutes(duracaoMinutos);

        Medico medico = resolverMedico(dto, inicio, fim, consulta.getId());
        Consultorio consultorio = resolverConsultorio(dto, inicio, fim, consulta.getId());

        consulta.setDataHora(inicio);
        consulta.setDuracaoMinutos(duracaoMinutos);
        consulta.setMotivo(dto.getMotivo());
        consulta.setObservacoes(dto.getObservacoes());
        consulta.setRequisitosPrevios(dto.getRequisitosPrevios());
        consulta.setMedico(medico);
        consulta.setConsultorio(consultorio);

        consulta = consultaRepository.save(consulta);

        ConsultaDTO resposta = toDTO(consulta);
        resposta.setAvisoRequisitosPrevios(montarAvisoRequisitosPrevios(consulta));
        return resposta;
    }

    // ---------------------------------------------------------------
    // Resolução de médico / especialidade / consultório
    // ---------------------------------------------------------------

    /**
     * Decide qual médico atenderá a consulta:
     * - Se medicoId foi informado, valida existência, status ativo,
     *   compatibilidade com a especialidade (quando informada) e disponibilidade.
     * - Se apenas especialidadeId foi informado, busca automaticamente o
     *   primeiro médico ativo daquela especialidade que esteja livre no horário.
     */
    private Medico resolverMedico(ConsultaDTO dto, LocalDateTime inicio, LocalDateTime fim, Long consultaIdAtual) {
        if (dto.getMedicoId() != null) {
            Medico medico = medicoRepository.findById(dto.getMedicoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Médico não encontrado com id: " + dto.getMedicoId()));

            if (!Boolean.TRUE.equals(medico.getAtivo())) {
                throw new RegraNegocioException("O médico selecionado está inativo.");
            }

            if (dto.getEspecialidadeId() != null
                    && (medico.getEspecialidade() == null
                    || !medico.getEspecialidade().getId().equals(dto.getEspecialidadeId()))) {
                throw new RegraNegocioException(
                        "O médico selecionado não atende à especialidade informada.");
            }

            if (haConflitoDeMedico(medico.getId(), inicio, fim, consultaIdAtual)) {
                throw new RegraNegocioException(
                        "O médico já possui outra consulta nesse horário. Escolha outro horário ou médico.");
            }

            return medico;
        }

        if (dto.getEspecialidadeId() == null) {
            throw new RegraNegocioException(
                    "Informe o médico (medicoId) ou a especialidade (especialidadeId) desejada.");
        }

        Especialidade especialidade = especialidadeRepository.findById(dto.getEspecialidadeId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Especialidade não encontrada com id: " + dto.getEspecialidadeId()));

        List<Medico> candidatos = medicoRepository.findByEspecialidadeId(especialidade.getId());

        Optional<Medico> medicoDisponivel = candidatos.stream()
                .filter(m -> Boolean.TRUE.equals(m.getAtivo()))
                .filter(m -> !haConflitoDeMedico(m.getId(), inicio, fim, consultaIdAtual))
                .findFirst();

        return medicoDisponivel.orElseThrow(() -> new RegraNegocioException(
                "Não há médico disponível para a especialidade '" + especialidade.getNome()
                        + "' no horário solicitado."));
    }

    /**
     * Decide qual consultório será usado:
     * - Se consultorioId foi informado, valida que está ativo e livre.
     * - Se não informado, atribui automaticamente o primeiro consultório
     *   ativo livre no horário solicitado.
     */
    private Consultorio resolverConsultorio(ConsultaDTO dto, LocalDateTime inicio, LocalDateTime fim, Long consultaIdAtual) {
        if (dto.getConsultorioId() != null) {
            Consultorio consultorio = consultorioRepository.findById(dto.getConsultorioId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Consultório não encontrado com id: " + dto.getConsultorioId()));

            if (!Boolean.TRUE.equals(consultorio.getAtivo())) {
                throw new RegraNegocioException("O consultório selecionado está inativo.");
            }

            if (haConflitoDeConsultorio(consultorio.getId(), inicio, fim, consultaIdAtual)) {
                throw new RegraNegocioException(
                        "O consultório já está ocupado nesse horário. Escolha outro horário ou consultório.");
            }

            return consultorio;
        }

        List<Consultorio> ativos = consultorioRepository.findByAtivoTrue();

        return ativos.stream()
                .filter(c -> !haConflitoDeConsultorio(c.getId(), inicio, fim, consultaIdAtual))
                .findFirst()
                .orElseThrow(() -> new RegraNegocioException(
                        "Não há consultório disponível no horário solicitado."));
    }

    private int resolverDuracao(ConsultaDTO dto) {
        if (dto.getDuracaoMinutos() != null && dto.getDuracaoMinutos() > 0) {
            return dto.getDuracaoMinutos();
        }
        if (dto.getEspecialidadeId() != null) {
            Optional<Especialidade> especialidade = especialidadeRepository.findById(dto.getEspecialidadeId());
            if (especialidade.isPresent() && especialidade.get().getDuracaoPadraoMinutos() != null) {
                return especialidade.get().getDuracaoPadraoMinutos();
            }
        }
        return DURACAO_PADRAO_MINUTOS;
    }

    // ---------------------------------------------------------------
    // Verificação de conflito de horário (disponibilidade)
    // ---------------------------------------------------------------

    private boolean haConflitoDeMedico(Long medicoId, LocalDateTime inicio, LocalDateTime fim, Long excluirConsultaId) {
        return consultaRepository.findByMedicoId(medicoId).stream()
                .filter(c -> excluirConsultaId == null || !c.getId().equals(excluirConsultaId))
                .filter(c -> !STATUS_QUE_NAO_OCUPAM_HORARIO.contains(c.getStatus()))
                .anyMatch(c -> intervalosSeSobrepoe(c, inicio, fim));
    }

    private boolean haConflitoDeConsultorio(Long consultorioId, LocalDateTime inicio, LocalDateTime fim, Long excluirConsultaId) {
        return consultaRepository.findByConsultorioId(consultorioId).stream()
                .filter(c -> excluirConsultaId == null || !c.getId().equals(excluirConsultaId))
                .filter(c -> !STATUS_QUE_NAO_OCUPAM_HORARIO.contains(c.getStatus()))
                .anyMatch(c -> intervalosSeSobrepoe(c, inicio, fim));
    }

    /**
     * Dois intervalos [inicio, fim) se sobrepõem quando um começa antes do
     * outro terminar e termina depois do outro começar.
     */
    private boolean intervalosSeSobrepoe(Consulta existente, LocalDateTime novoInicio, LocalDateTime novoFim) {
        LocalDateTime inicioExistente = existente.getDataHora();
        int duracaoExistente = existente.getDuracaoMinutos() != null
                ? existente.getDuracaoMinutos() : DURACAO_PADRAO_MINUTOS;
        LocalDateTime fimExistente = inicioExistente.plusMinutes(duracaoExistente);

        return novoInicio.isBefore(fimExistente) && novoFim.isAfter(inicioExistente);
    }

    // ---------------------------------------------------------------
    // Requisitos prévios
    // ---------------------------------------------------------------

    /**
     * Monta a mensagem de aviso devolvida ao atendente/paciente sempre que a
     * consulta possuir requisitos prévios cadastrados (jejum, exames
     * anteriores, restrição de idade, etc.).
     */
    private String montarAvisoRequisitosPrevios(Consulta consulta) {
        String requisitos = consulta.getRequisitosPrevios();
        if (requisitos == null || requisitos.isBlank()) {
            return null;
        }
        return "Atenção: para esta consulta é necessário observar os seguintes requisitos prévios: "
                + requisitos.trim() + ".";
    }

    // ---------------------------------------------------------------
    // Helpers de busca
    // ---------------------------------------------------------------

    private Consulta buscarConsultaOuFalhar(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada com id: " + id));
    }

    private Paciente buscarPacienteOuFalhar(Long id) {
        if (id == null) {
            throw new RegraNegocioException("O paciente é obrigatório para agendar uma consulta.");
        }
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado com id: " + id));
    }

    // ---------------------------------------------------------------
    // Conversão para DTO
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
