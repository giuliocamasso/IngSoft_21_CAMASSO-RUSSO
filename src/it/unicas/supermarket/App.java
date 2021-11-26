package it.unicas.supermarket;

import it.unicas.supermarket.view.LoginLayoutController;
import it.unicas.supermarket.view.MarketSectionLayoutController;
import it.unicas.supermarket.view.OrderSummaryLayoutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class App {
    private static App app = null;

    private Stage primaryStage;

    private AnchorPane loginLayout;
    private AnchorPane marketSectionLayout;
    private AnchorPane orderSummaryLayout;

    public static App getInstance(){
        if (app == null){
            app = new App();
        }
        return app;
    }

    public void launch(Stage primaryStage){
        System.out.println("Starting...");

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Market app");
        this.primaryStage.setResizable(false);

        // Set the application icon.
        this.primaryStage.getIcons().add(new Image("file:resources/images/shopping-cart.png"));

        initLoginLayout();

        // Clienti_test_mainpage();
        this.primaryStage.show();

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void initLoginLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/LoginLayout.fxml"));

            loginLayout = loader.load();

            // Show the scene containing the root layout.
            Scene loginScene = new Scene(loginLayout);
            primaryStage.setScene(loginScene);


            primaryStage.setOnCloseRequest(event -> {
                event.consume();
                handleExit();
            });


            // Give the controller access to the main app.
            LoginLayoutController controller = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleExit() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Exit");
        alert.setContentText("Exit from application.");

        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeOne){
            System.exit(0);
        }

    }


    public void handleValidate() {
        System.out.println("Validating card...");
        initMarketSectionLayout();

    }


    public void initMarketSectionLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MarketSectionLayout.fxml"));

            marketSectionLayout = loader.load();

            // Show the scene containing the root layout.
            Scene marketSectionScene = new Scene(marketSectionLayout);
            primaryStage.setScene(marketSectionScene);


            primaryStage.setOnCloseRequest(event -> {
                event.consume();
                handleExit();
            });


            // Give the controller access to the main app.
            MarketSectionLayoutController controller = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initOrderSummaryLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/OrderSummaryLayout.fxml"));

            orderSummaryLayout = loader.load();

            // Show the scene containing the root layout.
            Scene orderSummaryScene = new Scene(orderSummaryLayout);
            primaryStage.setScene(orderSummaryScene);


            primaryStage.setOnCloseRequest(event -> {
                event.consume();
                handleExit();
            });


            // Give the controller access to the main app.
            OrderSummaryLayoutController controller = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
