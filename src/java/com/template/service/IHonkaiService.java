package com.template.service;

import com.template.model.dto.HonkaiDTO;

import java.util.List;

/**
 * Abstração (DIP): o Controller depende desta interface, não da
 * implementação concreta do Service.
 */
public interface IHonkaiService {
    void cadastrar(HonkaiDTO honkai) throws Exception;
    void atualizar(HonkaiDTO honkai) throws Exception;
    void excluir(int id) throws Exception;
    List<HonkaiDTO> listar() throws Exception;
}
