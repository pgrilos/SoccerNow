package pt.ul.fc.css.soccernow.javaFX.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;
import pt.ul.fc.css.soccernow.javaFX.AppFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.UtilizadorControllerFX;

public class AdicionarArbitroController {
    @FXML
    private TextField nomeField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox certificadoCheck;
    @FXML
    private Button btnRegistar;
    @FXML
    private Button btnVoltar;
    @FXML
    private Label mensagemLabel;

    private final UtilizadorControllerFX controller = new UtilizadorControllerFX();

    @FXML
    public void initialize() {
        btnRegistar.setOnAction(e -> registarArbitro());
        btnVoltar.setOnAction(e -> voltar());
    }

    private void registarArbitro() {
        String nome = nomeField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean certificado = certificadoCheck.isSelected();

        if (nome.isEmpty() || username.isEmpty() || password.isEmpty()) {
            mensagemLabel.setText("Preenche todos os campos.");
            return;
        }
        try {
            ArbitroDTO dto = new ArbitroDTO();
            dto.setNome(nome);
            dto.setUsername(username);
            dto.setPassword(password);
            dto.setIsCertificated(certificado);
            controller.registarArbitro(dto);
            mensagemLabel.setText("Árbitro registado!");
            AppFX.setRoot("gestaoUtilizadores");
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao registar árbitro.");
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