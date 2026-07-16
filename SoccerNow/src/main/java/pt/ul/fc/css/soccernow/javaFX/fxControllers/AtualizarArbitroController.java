package pt.ul.fc.css.soccernow.javaFX.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;
import pt.ul.fc.css.soccernow.javaFX.AppFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.UtilizadorControllerFX;

public class AtualizarArbitroController {
    @FXML
    private TextField nomeField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox certificadoCheck;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label mensagemLabel;

    private ArbitroDTO arbitro;
    private final UtilizadorControllerFX controller = new UtilizadorControllerFX();

    @FXML
    public void initialize() {
        arbitro = GestaoUtilizadoresController.getArbitroParaAtualizar();
        if (arbitro != null) {
            nomeField.setText(arbitro.getNome());
            usernameField.setText(arbitro.getUsername());
            passwordField.setText(arbitro.getPassword());
            certificadoCheck.setSelected(arbitro.getIsCertificated());
        }
        btnGuardar.setOnAction(e -> guardar());
        btnCancelar.setOnAction(e -> voltar());
    }

    private void guardar() {
        arbitro.setNome(nomeField.getText());
        arbitro.setUsername(usernameField.getText());
        arbitro.setPassword(passwordField.getText());
        arbitro.setIsCertificated(certificadoCheck.isSelected());
        try {
            controller.atualizarArbitro(arbitro.getId(), arbitro);
            mensagemLabel.setText("Árbitro atualizado!");
            AppFX.setRoot("gestaoUtilizadores");
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao atualizar árbitro.");
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