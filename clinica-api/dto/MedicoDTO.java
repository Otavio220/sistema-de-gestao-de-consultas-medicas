package com.example.clinica.clinica_api.dto;

public class MedicoDTO {
    private Long id;
    private String nome;
    private String crm;
    private String telefone;
    private String email;
    private Boolean ativo;
    private Long especialidadeId;
    private String especialidadeNome;

    public MedicoDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public Long getEspecialidadeId() { return especialidadeId; }
    public void setEspecialidadeId(Long especialidadeId) { this.especialidadeId = especialidadeId; }
    public String getEspecialidadeNome() { return especialidadeNome; }
    public void setEspecialidadeNome(String especialidadeNome) { this.especialidadeNome = especialidadeNome; }
}
