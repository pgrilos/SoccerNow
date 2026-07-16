package pt.ul.fc.css.soccernow.javaFX.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.javaFX.AppFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.UtilizadorControllerFX;

public class AdicionarJogadorController {
    @FXML
    private TextField nomeField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> posicaoCombo;
    @FXML
    private Button btnRegistar;
    @FXML
    private Button btnVoltar;
    @FXML
    private Label mensagemLabel;

    private final UtilizadorControllerFX controller = new UtilizadorControllerFX();

    @FXML
    public void initialize() {
        posicaoCombo.setItems(FXCollections.observableArrayList("GUARDA_REDES", "FIXO", "ALA", "PIVO"));
        btnRegistar.setOnAction(e -> registarJogador());
        btnVoltar.setOnAction(e -> voltar());
    }

    private void registarJogador() {
        String nome = nomeField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String posicao = posicaoCombo.getValue();

        if (nome.isEmpty() || username.isEmpty() || password.isEmpty() || posicao == null) {
            mensagemLabel.setText("Preenche todos os campos.");
            return;
        }
        try {
            JogadorDTO dto = new JogadorDTO();
            dto.setNome(nome);
            dto.setUsername(username);
            dto.setPassword(password);
            dto.setPosicao(posicao);
            controller.registarJogador(dto);
            mensagemLabel.setText("Jogador registado!");
            AppFX.setRoot("gestaoUtilizadores");
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao registar jogador.");
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