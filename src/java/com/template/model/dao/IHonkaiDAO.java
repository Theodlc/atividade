package com.template.model.dao;

import com.template.model.dto.HonkaiDTO;

import java.util.List;

/**
 * Abstração (DIP): o Service depende desta interface, não da
 * implementação concreta de acesso a dados.
 */
public interface IHonkaiDAO {
    void salvar(HonkaiDTO honkai) throws Exception;
    void atualizar(HonkaiDTO honkai) throws Exception;
    void deletar(int id) throws Exception;
    List<HonkaiDTO> listar() throws Exception;
}
