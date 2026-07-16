
package pt.ul.fc.css.soccernow.javaFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppFX extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        primaryStage.getIcons().add(new Image(AppFX.class.getResourceAsStream("/fxViews/shoot.png")));
        setRoot("login"); // Começa pelo login
        primaryStage.setTitle("SoccerNow");
        primaryStage.show();
    }

    // Método utilitário para trocar de view
    public static void setRoot(String fxmlName) throws Exception {
        FXMLLoader loader = new FXMLLoader(AppFX.class.getResource("/fxViews/" + fxmlName + ".fxml"));
        Scene scene = new Scene(loader.load());
        mainStage.setScene(scene);
        mainStage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}