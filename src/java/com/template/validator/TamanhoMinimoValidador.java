package com.template.validator;

/** Valida a quantidade mínima de caracteres de um texto. */
public class TamanhoMinimoValidador implements Validador<String> {
    private final String nomeCampo;
    private final String valor;
    private final int tamanhoMinimo;

    public TamanhoMinimoValidador(String nomeCampo, String valor, int tamanhoMinimo) {
        this.nomeCampo = nomeCampo;
        this.valor = valor;
        this.tamanhoMinimo = tamanhoMinimo;
    }

    @Override
    public boolean validar(String valorAtual) {
        return valorAtual != null && valorAtual.trim().length() >= tamanhoMinimo;
    }

    @Override
    public String getMensagemErro() {
        return "O campo " + nomeCampo + " deve possuir ao menos "
                + tamanhoMinimo + " caracteres.";
    }

    @Override
    public String getValor() {
        return valor;
    }
}
