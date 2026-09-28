package com.template.validator;

/**
 * Regra de validação genérica: campo de texto obrigatório (não nulo, não vazio).
 * Reaproveitável para nome, elemento, raridade, etc. (DRY + OCP).
 */
public class CampoObrigatorioValidador implements Validador<String> {

    private final String nomeCampo;
    private final String valor;

    public CampoObrigatorioValidador(String nomeCampo, String valor) {
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
