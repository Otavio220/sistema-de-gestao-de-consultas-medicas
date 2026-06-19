package com.example.clinica.clinica_api.dto;

public class RecepcionistaDTO {
    private Long id;
    private String nome;
    private String email;
    private Boolean ativo;

    public RecepcionistaDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
