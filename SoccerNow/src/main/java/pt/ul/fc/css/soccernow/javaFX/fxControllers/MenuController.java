package pt.ul.fc.css.soccernow.javaFX.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import pt.ul.fc.css.soccernow.javaFX.AppFX;

public class MenuController {
    @FXML
    private Button btnUtilizadores;
    @FXML
    private Button btnEquipas;
    @FXML
    private Button btnJogos;
    @FXML
    private Button btnCampeonatos;

    @FXML
    public void initialize() {
        btnUtilizadores.setOnAction(e -> abrirUtilizadores());
        btnEquipas.setOnAction(e -> abrirEquipas());
        btnCampeonatos.setOnAction(e -> abrirCampeonatos());
        btnJogos.setOnAction(e -> abrirJogos());
    }

    private void abrirUtilizadores() {
        try {
            AppFX.setRoot("gestaoUtilizadores");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirEquipas() {
        try {
            AppFX.setRoot("gestaoEquipas");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirJogos() {
        try {
            AppFX.setRoot("gestaoJogos");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirCampeonatos() {
        try {
            AppFX.setRoot("gestaoCampeonatos");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}