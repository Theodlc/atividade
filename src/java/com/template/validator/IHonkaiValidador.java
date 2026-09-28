package com.template.validator;

import com.template.model.dto.HonkaiDTO;

/** Abstração usada pelo Controller para validar os dados do cadastro. */
public interface IHonkaiValidador {
    void validar(HonkaiDTO honkai) throws Exception;
}
