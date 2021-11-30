package it.unicas.indian.src.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static final String CURRENCY = "$";
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("C:/Users/giuli/Desktop/Archivio/Universita/LM32/Esami/Software-Engineering/progetto_github_folder/IngSoft_21_CAMASSO-RUSSO/src/it/unicas/indian/src/views/market.fxml"));
        primaryStage.setTitle("Fruits Marker");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
