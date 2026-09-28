package com.template.model.dto;

/**
 * DTO (Data Transfer Object) responsável apenas por carregar os dados
 * de uma personagem/item Honkai entre as camadas da aplicação.
 * Não contém lógica de negócio nem de persistência (SRP).
 */
public class HonkaiDTO {

    private Integer id;
    private String nome;
    private String elemento;
    private String raridade;
    private String efeito;

    // Construtor para um novo registro (ainda sem id, gerado pelo banco)
    public HonkaiDTO(String nome, String elemento, String raridade, String efeito) {
        this.nome = nome;
        this.elemento = elemento;
        this.raridade = raridade;
        this.efeito = efeito;
    }

    // Construtor para um registro já existente (vindo do banco)
    public HonkaiDTO(Integer id, String nome, String elemento, String raridade, String efeito) {
        this.id = id;
        this.nome = nome;
        this.elemento = elemento;
        this.raridade = raridade;
        this.efeito = efeito;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getElemento() { return elemento; }
    public void setElemento(String elemento) { this.elemento = elemento; }

    public String getRaridade() { return raridade; }
    public void setRaridade(String raridade) { this.raridade = raridade; }

    public String getEfeito() { return efeito; }
    public void setEfeito(String efeito) { this.efeito = efeito; }
}
