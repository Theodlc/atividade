package com.template.main;

import com.template.controller.ControllerFactory;
import com.template.model.dao.HonkaiDAO;
import com.template.model.dao.IHonkaiDAO;
import com.template.service.HonkaiService;
import com.template.service.IHonkaiService;
import com.template.validator.HonkaiValidador;
import com.template.validator.IHonkaiValidador;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Objects;

/** Ponto de composição: cria as dependências e configura a fábrica. */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        IHonkaiDAO honkaiDAO = new HonkaiDAO();
        IHonkaiService honkaiService = new HonkaiService(honkaiDAO);
        IHonkaiValidador honkaiValidador = new HonkaiValidador();

        ControllerFactory controllerFactory =
                new ControllerFactory(honkaiService, honkaiValidador);

        URL fxml = Objects.requireNonNull(
                getClass().getResource("/com/template/main.fxml"),
                "Arquivo main.fxml não encontrado."
        );
        FXMLLoader loader = new FXMLLoader(fxml);
        loader.setControllerFactory(controllerFactory);
        Parent root = loader.load();

        stage.setTitle("Terminal de Dados Honkai");
        URL icone = getClass().getResource("/com/template/icon.png");
        if (icone != null) {
            stage.getIcons().add(new Image(icone.toExternalForm()));
        }

        Rectangle2D areaVisivel = Screen.getPrimary().getVisualBounds();
        double largura = Math.min(1160, areaVisivel.getWidth() * 0.94);
        double altura = Math.min(660, areaVisivel.getHeight() * 0.90);

        stage.setScene(new Scene(root, largura, altura));
        stage.setMinWidth(Math.min(820, largura));
        stage.setMinHeight(Math.min(520, altura));
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
