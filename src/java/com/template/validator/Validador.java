package com.template.validator;

/**
 * Contrato genérico de validação. O tipo T permite validar String,
 * Integer, LocalDate ou qualquer outro tipo sem duplicar a interface.
 */
public interface Validador<T> {
    boolean validar(T valorAtual);
    String getMensagemErro();
    T getValor();
}
