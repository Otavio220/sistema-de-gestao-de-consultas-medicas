package com.example.clinica.clinica_api.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ConsultorioEquipamentoId implements Serializable {

    private Long consultorioId;
    private Long equipamentoId;

    public ConsultorioEquipamentoId() {
    }

    public ConsultorioEquipamentoId(Long consultorioId, Long equipamentoId) {
        this.consultorioId = consultorioId;
        this.equipamentoId = equipamentoId;
    }

    public Long getConsultorioId() { return consultorioId; }
    public void setConsultorioId(Long consultorioId) { this.consultorioId = consultorioId; }

    public Long getEquipamentoId() { return equipamentoId; }
    public void setEquipamentoId(Long equipamentoId) { this.equipamentoId = equipamentoId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConsultorioEquipamentoId)) return false;
        ConsultorioEquipamentoId that = (ConsultorioEquipamentoId) o;
        return Objects.equals(consultorioId, that.consultorioId) &&
                Objects.equals(equipamentoId, that.equipamentoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consultorioId, equipamentoId);
    }
}