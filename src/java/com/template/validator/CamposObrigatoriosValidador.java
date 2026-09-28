package com.template.validator;

/** Valida textos que precisam ser preenchidos. */
public class CamposObrigatoriosValidador implements Validador<String> {
    private final String nomeCampo;
    private final String valor;

    public CamposObrigatoriosValidador(String nomeCampo, String valor) {
        this.nomeCampo = nomeCampo;
        this.valor = valor;
    }

    @Override
    public boolean validar(String valorAtual) {
        return valorAtual != null && !valorAtual.trim().isEmpty();
    }

    @Override
    public String getMensagemErro() {
        return "O campo " + nomeCampo + " é obrigatório.";
    }

    @Override
    public String getValor() {
        return valor;
    }
}
