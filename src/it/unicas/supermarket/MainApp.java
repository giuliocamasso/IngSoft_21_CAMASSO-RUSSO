package it.unicas.supermarket;
import java.util.Optional;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    /**
     * Constructor
     */
    public MainApp() {
        System.out.println("Applicazione in esecuzione da MainApp...");
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Market app");

        // Set the application icon.
        this.primaryStage.getIcons().add(new Image("file:resources/images/shopping-cart.png"));

        this.primaryStage.show();

    }


    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
        System.out.println("Applicazione in esecuzione...");
    }
}
