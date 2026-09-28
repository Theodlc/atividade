package com.template.service;

import com.template.model.dao.IHonkaiDAO;
import com.template.model.dto.HonkaiDTO;
import java.util.List;

/** Contém os casos de uso e delega a persistência à abstração IHonkaiDAO. */
public class HonkaiService implements IHonkaiService {
    private final IHonkaiDAO dao;

    public HonkaiService(IHonkaiDAO dao) {
        this.dao = dao;
    }

    @Override
    public void cadastrar(HonkaiDTO honkai) throws Exception {
        dao.salvar(honkai);
    }

    @Override
    public void atualizar(HonkaiDTO honkai) throws Exception {
        dao.atualizar(honkai);
    }

    @Override
    public void excluir(int id) throws Exception {
        dao.deletar(id);
    }

    @Override
    public List<HonkaiDTO> listar() throws Exception {
        return dao.listar();
    }
}
