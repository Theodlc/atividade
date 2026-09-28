package com.template.validator;

import com.template.model.dto.HonkaiDTO;
import java.util.ArrayList;
import java.util.List;

/** Reúne e executa as regras de validação do cadastro de Honkai. */
public class HonkaiValidador implements IHonkaiValidador {
    @Override
    public void validar(HonkaiDTO honkai) throws Exception {
        if (honkai == null) {
            throw new IllegalArgumentException("Os dados do Honkai não podem ser nulos.");
        }

        List<Validador<String>> validadores = new ArrayList<>();
        validadores.add(new CamposObrigatoriosValidador("Nome", honkai.getNome()));
        validadores.add(new CamposObrigatoriosValidador("Elemento", honkai.getElemento()));
        validadores.add(new CamposObrigatoriosValidador("Raridade", honkai.getRaridade()));
        validadores.add(new CamposObrigatoriosValidador("Efeito", honkai.getEfeito()));
        validadores.add(new TamanhoMinimoValidador("Nome", honkai.getNome(), 3));
        validadores.add(new TamanhoMinimoValidador("Efeito", honkai.getEfeito(), 10));

        // O foreach usa apenas os métodos definidos pela interface Validador.
        for (Validador<String> validador : validadores) {
            if (!validador.validar(validador.getValor())) {
                throw new Exception(validador.getMensagemErro());
            }
        }
    }
}
