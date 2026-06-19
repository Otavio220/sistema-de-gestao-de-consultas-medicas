package com.example.clinica.clinica_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "consultorio_equipamento")
public class ConsultorioEquipamento {

    @EmbeddedId
    private ConsultorioEquipamentoId id = new ConsultorioEquipamentoId();

    @ManyToOne
    @MapsId("consultorioId")
    @JoinColumn(name = "consultorio_id")
    private Consultorio consultorio;

    @ManyToOne
    @MapsId("equipamentoId")
    @JoinColumn(name = "equipamento_id")
    private Equipamento equipamento;

    public ConsultorioEquipamento() {
    }

    public ConsultorioEquipamento(Consultorio consultorio, Equipamento equipamento) {
        this.consultorio = consultorio;
        this.equipamento = equipamento;
        this.id = new ConsultorioEquipamentoId(consultorio.getId(), equipamento.getId());
    }

    public ConsultorioEquipamentoId getId() { return id; }
    public void setId(ConsultorioEquipamentoId id) { this.id = id; }

    public Consultorio getConsultorio() { return consultorio; }
    public void setConsultorio(Consultorio consultorio) { this.consultorio = consultorio; }

    public Equipamento getEquipamento() { return equipamento; }
    public void setEquipamento(Equipamento equipamento) { this.equipamento = equipamento; }
}