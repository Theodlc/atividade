package com.template.model.dao;

import com.template.model.dto.HonkaiDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Responsável exclusivamente pela persistência PostgreSQL de HonkaiDTO. */
public class HonkaiDAO implements IHonkaiDAO {
    private static final Logger LOGGER = Logger.getLogger(HonkaiDAO.class.getName());

    private static final String CRIAR_TABELA = """
            CREATE TABLE IF NOT EXISTS honkai (
                id SERIAL PRIMARY KEY,
                nome VARCHAR(100) NOT NULL,
                elemento VARCHAR(30) NOT NULL,
                raridade VARCHAR(30) NOT NULL,
                efeito TEXT NOT NULL
            )
            """;

    private static final String INSERIR = """
            INSERT INTO honkai (nome, elemento, raridade, efeito)
            VALUES (?, ?, ?, ?)
            """;

    private static final String ATUALIZAR = """
            UPDATE honkai
               SET nome = ?, elemento = ?, raridade = ?, efeito = ?
             WHERE id = ?
            """;

    private static final String EXCLUIR = "DELETE FROM honkai WHERE id = ?";

    private static final String LISTAR = """
            SELECT id, nome, elemento, raridade, efeito
              FROM honkai
             ORDER BY id
            """;

    @Override
    public void salvar(HonkaiDTO honkai) throws Exception {
        try (Connection conexao = abrirConexaoPreparada();
             PreparedStatement comando = conexao.prepareStatement(
                     INSERIR,
                     Statement.RETURN_GENERATED_KEYS
             )) {
            preencherCampos(comando, honkai);
            comando.executeUpdate();

            try (ResultSet chaves = comando.getGeneratedKeys()) {
                if (chaves.next()) {
                    honkai.setId(chaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw traduzirErro("salvar", e);
        }
    }

    @Override
    public void atualizar(HonkaiDTO honkai) throws Exception {
        if (honkai.getId() == null) {
            throw new Exception("Selecione um registro para atualizar.");
        }

        try (Connection conexao = abrirConexaoPreparada();
             PreparedStatement comando = conexao.prepareStatement(ATUALIZAR)) {
            preencherCampos(comando, honkai);
            comando.setInt(5, honkai.getId());

            if (comando.executeUpdate() == 0) {
                throw new Exception("Registro não encontrado no servidor.");
            }
        } catch (SQLException e) {
            throw traduzirErro("atualizar", e);
        }
    }

    @Override
    public void deletar(int id) throws Exception {
        try (Connection conexao = abrirConexaoPreparada();
             PreparedStatement comando = conexao.prepareStatement(EXCLUIR)) {
            comando.setInt(1, id);
            if (comando.executeUpdate() == 0) {
                throw new Exception("Registro não encontrado no servidor.");
            }
        } catch (SQLException e) {
            throw traduzirErro("excluir", e);
        }
    }

    @Override
    public List<HonkaiDTO> listar() throws Exception {
        List<HonkaiDTO> registros = new ArrayList<>();

        try (Connection conexao = abrirConexaoPreparada();
             PreparedStatement comando = conexao.prepareStatement(LISTAR);
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                registros.add(new HonkaiDTO(
                        resultado.getInt("id"),
                        resultado.getString("nome"),
                        resultado.getString("elemento"),
                        resultado.getString("raridade"),
                        resultado.getString("efeito")
                ));
            }
            return registros;
        } catch (SQLException e) {
            throw traduzirErro("listar", e);
        }
    }

    private Connection abrirConexaoPreparada() throws SQLException {
        Connection conexao = Database.conectar();
        try (Statement comando = conexao.createStatement()) {
            comando.execute(CRIAR_TABELA);
        } catch (SQLException e) {
            conexao.close();
            throw e;
        }
        return conexao;
    }

    private void preencherCampos(PreparedStatement comando, HonkaiDTO honkai)
            throws SQLException {
        comando.setString(1, honkai.getNome());
        comando.setString(2, honkai.getElemento());
        comando.setString(3, honkai.getRaridade());
        comando.setString(4, honkai.getEfeito());
    }

    private Exception traduzirErro(String operacao, SQLException erro) {
        LOGGER.log(Level.SEVERE, "Erro ao " + operacao + " registro Honkai", erro);
        return new Exception(
                "Não foi possível acessar o PostgreSQL em localhost:5432/bd_honkai. "
                        + "Verifique se o servidor está iniciado e se o usuário e a senha são postgres.",
                erro
        );
    }
}
