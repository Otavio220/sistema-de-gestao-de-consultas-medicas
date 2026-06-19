package com.example.clinica.clinica_api.dto;

public class ConsultorioEquipamentoDTO {
    private Long consultorioId;
    private String consultorioNome;
    private Long equipamentoId;
    private String equipamentoNome;

    public ConsultorioEquipamentoDTO() {}

    public Long getConsultorioId() { return consultorioId; }
    public void setConsultorioId(Long consultorioId) { this.consultorioId = consultorioId; }
    public String getConsultorioNome() { return consultorioNome; }
    public void setConsultorioNome(String consultorioNome) { this.consultorioNome = consultorioNome; }
    public Long getEquipamentoId() { return equipamentoId; }
    public void setEquipamentoId(Long equipamentoId) { this.equipamentoId = equipamentoId; }
    public String getEquipamentoNome() { return equipamentoNome; }
    public void setEquipamentoNome(String equipamentoNome) { this.equipamentoNome = equipamentoNome; }
}
