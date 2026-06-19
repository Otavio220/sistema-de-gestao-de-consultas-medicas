package com.example.clinica.clinica_api.dto;

public class EspecialidadeDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Integer duracaoPadraoMinutos;

    public EspecialidadeDTO() {}

    public EspecialidadeDTO(Long id, String nome, String descricao, Integer duracaoPadraoMinutos) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.duracaoPadraoMinutos = duracaoPadraoMinutos;
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