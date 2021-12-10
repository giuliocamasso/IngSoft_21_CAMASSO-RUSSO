package it.unicas.supermarket;

import it.unicas.supermarket.controller.LoginLayoutController;
import it.unicas.supermarket.controller.MarketSectionLayoutController;
import it.unicas.supermarket.controller.OrderSummaryLayoutController;

import it.unicas.supermarket.controller.ReceiptController;
import it.unicas.supermarket.model.Carte;
import it.unicas.supermarket.model.Clienti;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.mysql.CarteDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.ClientiDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.DAOMySQLSettings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class App {
    private static App app = null;

    private Stage mainStage;

    private AnchorPane loginLayout;
    private AnchorPane marketSectionLayout;
    private AnchorPane orderSummaryLayout;

    private static Logger logger =  Logger.getLogger(LoginLayoutController.class.getName());

    public String codiceCarta;
    public String codiceCliente;
    public String massimali;
    public String puntiFedelta;

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

        // mainStage.initStyle(StageStyle.DECORATED);       // default
        // mainStage.initStyle(StageStyle.UNDECORATED);     // only content, without OS-style windows
        // mainStage.initStyle(StageStyle.TRANSPARENT);     // useless//
        // mainStage.initStyle(StageStyle.UTILITY);         // orribile
        // mainStage.initStyle(StageStyle.UNIFIED);         // solves the 'white border' glitch

        // ("A unified Stage is like a decorated stage, except it has no border between the decoration area and the main content area."
        mainStage.initStyle(StageStyle.UNIFIED);

        // mainStage.setWidth(1280.0);
        // mainStage.setHeight(720.0);

        mainStage.setResizable(false);

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
        alert.setTitle("Uscita");
        alert.setHeaderText("Vuoi uscire dal supermercato?");
        alert.setContentText("Grazie per averci scelto.");

        ButtonType buttonTypeOne = new ButtonType("Si");
        ButtonType buttonTypeCancel = new ButtonType("Torna al supermercato", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeOne){
            System.exit(0);
        }

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

    /**
     * Opens a dialog to show the receipt
     */
    public boolean showReceipt() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("view/Receipt.fxml"));
            Stage dialogStage = new Stage();

            dialogStage.setTitle("Receipt");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainStage);

            dialogStage.setScene(new Scene(loader.load()));

            // Set the Colleghis into the controller.
            ReceiptController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            // Set the dialog icon.
            // dialogStage.getIcons().add(new Image("file:resources/images/calendar.png"));

            dialogStage.showAndWait();

            return controller.isBackClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getCodiceCarta(String codiceCarta){
        return this.codiceCarta = codiceCarta;
    }

    public String getCodiceCliente(String codiceCliente){
        return this.codiceCliente = codiceCliente;
    }

    public String getMassimali(String massimali){
        return this.massimali = massimali;
    }

    public String getPuntiFedelta(String puntiFedelta){
        return this.puntiFedelta = puntiFedelta;
    }




}
