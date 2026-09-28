package com.template.controller;

import com.template.model.dto.HonkaiDTO;
import com.template.service.IHonkaiService;
import com.template.util.DialogUtil;
import com.template.validator.IHonkaiValidador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/** Orquestra a tela e depende apenas das abstrações recebidas pela fábrica. */
public class MainController {
    private final IHonkaiService service;
    private final IHonkaiValidador validador;
    private final ObservableList<HonkaiDTO> listaHonkai = FXCollections.observableArrayList();

    public MainController(IHonkaiService service, IHonkaiValidador validador) {
        this.service = service;
        this.validador = validador;
    }

    @FXML private TextField txtId;
    @FXML private TextField txtNome;
    @FXML private ComboBox<String> comboElemento;
    @FXML private ComboBox<String> comboRaridade;
    @FXML private TextArea txtEfeito;
    @FXML private Label lblMensagem;
    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;
    @FXML private TableView<HonkaiDTO> tabelaItens;
    @FXML private TableColumn<HonkaiDTO, Integer> colId;
    @FXML private TableColumn<HonkaiDTO, String> colNome;
    @FXML private TableColumn<HonkaiDTO, String> colElemento;
    @FXML private TableColumn<HonkaiDTO, String> colRaridade;
    @FXML private TableColumn<HonkaiDTO, String> colEfeito;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colElemento.setCellValueFactory(new PropertyValueFactory<>("elemento"));
        colRaridade.setCellValueFactory(new PropertyValueFactory<>("raridade"));
        colEfeito.setCellValueFactory(new PropertyValueFactory<>("efeito"));
        tabelaItens.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaItens.setItems(listaHonkai);
        alterarModoEdicao(false);
        carregarTabela();
    }

    @FXML
    public void onBotaoSalvarClick(ActionEvent event) {
        try {
            HonkaiDTO honkai = montarDtoComCamposDaTela();
            validador.validar(honkai);
            service.cadastrar(honkai);
            carregarTabela();
            limparCampos();
            exibirMensagem("Registro salvo com sucesso!", true);
            DialogUtil.showInfo("Registro salvo com sucesso!");
        } catch (Exception e) {
            tratarErro("Erro ao salvar: ", e);
        }
    }

    @FXML
    public void onBotaoEditarClick(ActionEvent event) {
        try {
            HonkaiDTO honkai = montarDtoComCamposDaTela();
            honkai.setId(obterIdSelecionado());
            validador.validar(honkai);
            service.atualizar(honkai);
            carregarTabela();
            limparCampos();
            exibirMensagem("Registro atualizado com sucesso!", true);
            DialogUtil.showInfo("Registro atualizado com sucesso!");
        } catch (Exception e) {
            tratarErro("Erro ao atualizar: ", e);
        }
    }

    @FXML
    public void onBotaoExcluirClick(ActionEvent event) {
        if (!DialogUtil.showConfirmation("Deseja realmente excluir este registro?")) {
            return;
        }

        try {
            service.excluir(obterIdSelecionado());
            carregarTabela();
            limparCampos();
            exibirMensagem("Registro excluído com sucesso!", true);
        } catch (Exception e) {
            tratarErro("Erro ao excluir: ", e);
        }
    }

    @FXML
    public void onTabelaClicada(MouseEvent event) {
        HonkaiDTO selecionado = tabelaItens.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            preencherCamposComItemSelecionado(selecionado);
        }
    }

    @FXML
    public void onBotaoLimparClick(ActionEvent event) {
        limparCampos();
    }

    private HonkaiDTO montarDtoComCamposDaTela() {
        return new HonkaiDTO(
                txtNome.getText().trim(),
                comboElemento.getValue(),
                comboRaridade.getValue(),
                txtEfeito.getText().trim()
        );
    }

    private int obterIdSelecionado() throws Exception {
        if (txtId.getText() == null || txtId.getText().isBlank()) {
            throw new Exception("Selecione um registro na tabela.");
        }
        return Integer.parseInt(txtId.getText());
    }

    private void carregarTabela() {
        try {
            listaHonkai.setAll(service.listar());
        } catch (Exception e) {
            tratarErro("Erro ao carregar os registros: ", e);
        }
    }

    private void preencherCamposComItemSelecionado(HonkaiDTO honkai) {
        txtId.setText(String.valueOf(honkai.getId()));
        txtNome.setText(honkai.getNome());
        comboElemento.setValue(honkai.getElemento());
        comboRaridade.setValue(honkai.getRaridade());
        txtEfeito.setText(honkai.getEfeito());
        alterarModoEdicao(true);
    }

    private void limparCampos() {
        txtId.clear();
        txtNome.clear();
        comboElemento.setValue(null);
        comboRaridade.setValue(null);
        txtEfeito.clear();
        tabelaItens.getSelectionModel().clearSelection();
        alterarModoEdicao(false);
    }

    private void alterarModoEdicao(boolean editando) {
        btnEditar.setDisable(!editando);
        btnExcluir.setDisable(!editando);
    }

    private void tratarErro(String prefixo, Exception erro) {
        String mensagem = erro.getMessage() == null ? "Erro inesperado." : erro.getMessage();
        exibirMensagem(prefixo + mensagem, false);
        DialogUtil.showError(mensagem);
    }

    private void exibirMensagem(String texto, boolean sucesso) {
        lblMensagem.setText(texto);
        lblMensagem.setStyle(sucesso
                ? "-fx-text-fill: #2ecc71; -fx-font-weight: bold;"
                : "-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
    }
}
