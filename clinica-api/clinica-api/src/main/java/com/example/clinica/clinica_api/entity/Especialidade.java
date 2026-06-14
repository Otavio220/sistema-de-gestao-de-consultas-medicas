package com.example.clinica.clinica_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "especialidades")
public class Especialidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(name = "duracao_padrao_minutos", nullable = false)
    private Integer duracaoPadraoMinutos;

    public Especialidade() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getDuracaoPadraoMinutos() { return duracaoPadraoMinutos; }
    public void setDuracaoPadraoMinutos(Integer duracaoPadraoMinutos) { this.duracaoPadraoMinutos = duracaoPadraoMinutos; }
}