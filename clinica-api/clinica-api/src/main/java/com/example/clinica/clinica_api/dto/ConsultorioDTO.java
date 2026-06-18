package com.example.clinica.clinica_api.dto;

public class ConsultorioDTO {
    private Long id;
    private String nome;
    private String andar;
    private String descricao;
    private Boolean ativo;

    public ConsultorioDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getAndar() { return andar; }
    public void setAndar(String andar) { this.andar = andar; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
