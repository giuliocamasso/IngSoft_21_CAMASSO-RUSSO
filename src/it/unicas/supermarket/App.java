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
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;

public class App {
    private static App app = null;

    private Stage mainStage;

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

        mainStage = primaryStage;
        mainStage.setTitle("Market app");

        // mainStage.setWidth(1280.0);
        // mainStage.setHeight(720.0);
        mainStage.setResizable(false);

        // mainStage.initStyle(StageStyle.DECORATED);       // default
        // mainStage.initStyle(StageStyle.UNDECORATED);     // only content, without OS-style windows
        // mainStage.initStyle(StageStyle.TRANSPARENT);     // useless//
        // mainStage.initStyle(StageStyle.UTILITY);         // orribile

        mainStage.initStyle(StageStyle.UNIFIED);         // solves the 'white border' glitch
        // ("A unified Stage is like a decorated stage, except it has no border between the decoration area and the main content area."

        // Set the application icon.
        mainStage.getIcons().add(new Image("file:resources/images/shopping-cart.png"));

        initLoginLayout();

        // mainStage.setFullScreen(true);
        mainStage.show();

    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void initLoginLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/LoginLayout.fxml"));

            loginLayout = loader.load();

            // Show the scene containing the root layout.
            Scene loginScene = new Scene(loginLayout);
            mainStage.setScene(loginScene);


            mainStage.setOnCloseRequest(event -> {
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
            mainStage.setScene(marketSectionScene);


            mainStage.setOnCloseRequest(event -> {
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
            mainStage.setScene(orderSummaryScene);


            mainStage.setOnCloseRequest(event -> {
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
