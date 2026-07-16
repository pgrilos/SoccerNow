package pt.ul.fc.css.soccernow.javaFX.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import pt.ul.fc.css.soccernow.javaFX.AppFX;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button btnLogin;
    @FXML
    private Label mensagemLabel;

    @FXML
    public void initialize() {
        btnLogin.setOnAction(e -> autenticar());
    }

    private void autenticar() {
        mensagemLabel.setText("Login aceite!");
        try {
            AppFX.setRoot("menu"); // Mostra o menu principal após login
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao mudar de ecrã.");
        }
    }
}