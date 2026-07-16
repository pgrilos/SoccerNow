package pt.ul.fc.css.soccernow.javaFX.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.javaFX.AppFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.UtilizadorControllerFX;

public class AtualizarJogadorController {
    @FXML
    private TextField nomeField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> posicaoCombo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label mensagemLabel;

    private JogadorDTO jogador;
    private final UtilizadorControllerFX controller = new UtilizadorControllerFX();

    @FXML
    public void initialize() {
        posicaoCombo.setItems(FXCollections.observableArrayList("GUARDA_REDES", "FIXO", "ALA", "PIVO"));
        jogador = GestaoUtilizadoresController.getJogadorParaAtualizar();
        if (jogador != null) {
            nomeField.setText(jogador.getNome());
            usernameField.setText(jogador.getUsername());
            passwordField.setText(jogador.getPassword());
            posicaoCombo.setValue(jogador.getPosicao());
        }
        btnGuardar.setOnAction(e -> guardar());
        btnCancelar.setOnAction(e -> voltar());
    }

    private void guardar() {
        jogador.setNome(nomeField.getText());
        jogador.setUsername(usernameField.getText());
        jogador.setPassword(passwordField.getText());
        jogador.setPosicao(posicaoCombo.getValue());
        try {
            controller.atualizarJogador(jogador.getId(), jogador);
            mensagemLabel.setText("Jogador atualizado!");
            AppFX.setRoot("gestaoUtilizadores");
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao atualizar jogador.");
        }
    }

    private void voltar() {
        try {
            AppFX.setRoot("gestaoUtilizadores");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao voltar.");
        }
    }
}