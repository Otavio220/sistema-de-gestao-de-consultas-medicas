package com.example.clinica.clinica_api.dto;

import com.example.clinica.clinica_api.entity.StatusConsulta;
import java.time.LocalDateTime;

public class ConsultaDTO {
    private Long id;
    private LocalDateTime dataHora;
    private Integer duracaoMinutos;
    private StatusConsulta status;
    private String motivo;
    private String observacoes;
    private String requisitosPrevios;
    private Long pacienteId;
    private String pacienteNome;
    private Long medicoId;
    private String medicoNome;
    private Long consultorioId;
    private String consultorioNome;

    public ConsultaDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public Integer getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(Integer duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }
    public StatusConsulta getStatus() { return status; }
    public void setStatus(StatusConsulta status) { this.status = status; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public String getRequisitosPrevios() { return requisitosPrevios; }
    public void setRequisitosPrevios(String requisitosPrevios) { this.requisitosPrevios = requisitosPrevios; }
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public String getPacienteNome() { return pacienteNome; }
    public void setPacienteNome(String pacienteNome) { this.pacienteNome = pacienteNome; }
    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }
    public String getMedicoNome() { return medicoNome; }
    public void setMedicoNome(String medicoNome) { this.medicoNome = medicoNome; }
    public Long getConsultorioId() { return consultorioId; }
    public void setConsultorioId(Long consultorioId) { this.consultorioId = consultorioId; }
    public String getConsultorioNome() { return consultorioNome; }
    public void setConsultorioNome(String consultorioNome) { this.consultorioNome = consultorioNome; }
}
