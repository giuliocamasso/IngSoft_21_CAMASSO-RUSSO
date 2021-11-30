package it.unicas.supermarket;

import it.unicas.supermarket.controller.LoginLayoutController;
import it.unicas.supermarket.controller.MarketController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainSample extends Application {

    private Stage mainStage;

    private AnchorPane loginLayout;
    private AnchorPane marketSectionLayout;
    private AnchorPane orderSummaryLayout;

    public static final String CURRENCY = "$";

    @Override
    public void start(Stage primaryStage) throws Exception{


        mainStage = primaryStage;
        mainStage.setTitle("Fruits Marker");

        initLoginLayout();

        // mainStage.setFullScreen(true);
        mainStage.show();
    }

    public void initLoginLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainSample.class.getResource("view/Market.fxml"));

            loginLayout = loader.load();

            // Show the scene containing the root layout.
            Scene loginScene = new Scene(loginLayout);
            mainStage.setScene(loginScene);

            mainStage.setOnCloseRequest(event -> {
                event.consume();
                App.getInstance().handleExit();
            });


            // Give the controller access to the main app.
            MarketController controller = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}