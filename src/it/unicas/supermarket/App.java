package it.unicas.supermarket;

import it.unicas.supermarket.controller.LoginLayoutController;
import it.unicas.supermarket.controller.MarketSectionLayoutController;
import it.unicas.supermarket.controller.OrderSummaryLayoutController;

import it.unicas.supermarket.controller.ReceiptController;
import it.unicas.supermarket.model.dao.DAOException;
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
import java.util.*;
import java.util.logging.Logger;

import static it.unicas.supermarket.controller.MarketSectionLayoutController.getNomeArticoloFromBarcode;
import static it.unicas.supermarket.controller.MarketSectionLayoutController.getPrezzoArticoloFromBarcode;

public class App {
    private static App app = null;

    private Stage mainStage;

    private Scene marketSectionScene;
    private Scene paymentSectionScene;
    private Scene loginSectionScene;

    private LoginLayoutController loginController;
    private OrderSummaryLayoutController orderSummaryLayoutController;

    public LoginLayoutController getLoginController() {
        return loginController;
    }

    public OrderSummaryLayoutController getOrderSummaryLayoutController() {
        return orderSummaryLayoutController;
    }

    public MarketSectionLayoutController getMarketSectionLayoutController() {
        return marketSectionLayoutController;
    }

    private MarketSectionLayoutController marketSectionLayoutController;

    private static final Logger logger =  Logger.getLogger(LoginLayoutController.class.getName());

    public String codiceCarta;
    public String codiceCliente;
    public String massimali;
    public String puntiFedelta;

    public Float getMassimaleRimanente() {
        return massimaleRimanente;
    }

    public void setMassimaleRimanente(Float massimaleRimanente) {
        this.massimaleRimanente = massimaleRimanente;
    }

    public Float massimaleRimanente;

    public String reparto = "None";

    public LinkedHashMap<String, Integer> cartMap = new LinkedHashMap<>();

    public HashMap<String, Integer> getCartMap() {
        return cartMap;
    }

    public Scene getMarketSectionScene() {
        return marketSectionScene;
    }

    public String getReparto()                   { return reparto; }

    public void setReparto(String reparto)       { this.reparto = reparto; }

    public void setCodiceCarta(String codiceCarta){ this.codiceCarta = codiceCarta; }

    public String getCodiceCarta(){
        return codiceCarta;
    }

    public void setCodiceCliente(String codiceCliente){
        this.codiceCliente = codiceCliente;
    }

    public String getCodiceCliente(){
        return codiceCliente;
    }

    public void setMassimali(String massimali) { this.massimali = massimali; }

    public String getMassimali(){
        return massimali;
    }

    public void setPuntiFedelta(String puntiFedelta) { this.puntiFedelta = puntiFedelta; }

    public String getPuntiFedelta(){
        return puntiFedelta;
    }

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

            AnchorPane loginLayout = loader.load();

            // Show the scene containing the root layout.
            Scene loginScene = new Scene(loginLayout);
            mainStage.setScene(loginScene);
            this.loginSectionScene = loginScene;

            mainStage.setOnCloseRequest(event -> {
                event.consume();
                handleExit();
            });

            // Give the controller access to the main app.
            LoginLayoutController controller = loader.getController();
            this.loginController = controller;

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
            System.out.println("Entrando nei reparti");
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MarketSectionLayout.fxml"));

            AnchorPane marketSectionLayout = loader.load();

            // Show the scene containing the root layout.
            Scene marketSectionScene = new Scene(marketSectionLayout);
            mainStage.setScene(marketSectionScene);
            this.marketSectionScene = marketSectionScene;


            mainStage.setOnCloseRequest(event -> {
                event.consume();
                handleExit();
            });

            // Give the controller access to the main app.
            MarketSectionLayoutController controller = loader.getController();
            this.marketSectionLayoutController = controller;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initOrderSummaryLayout() {
        /*
        if (paymentVisited){
            mainStage.setScene(paymentSectionScene);
            return;
        }
        */
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/OrderSummaryLayout.fxml"));

            AnchorPane orderSummaryLayout = loader.load();

            // Show the scene containing the root layout.
            Scene orderSummaryScene = new Scene(orderSummaryLayout);
            mainStage.setScene(orderSummaryScene);
            this.paymentSectionScene = orderSummaryScene;

            mainStage.setOnCloseRequest(event -> {
                event.consume();
                handleExit();
            });

            // Give the controller access to the main app.
            OrderSummaryLayoutController controller = loader.getController();
            this.orderSummaryLayoutController = controller;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to show the receipt
     */
    public void showReceipt() {
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

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<String> getCartListArticles() {
       return new ArrayList<>(this.cartMap.keySet());
    }

    public List<Integer> getCartListQuantity() {
        return new ArrayList<>(this.cartMap.values());
    }

    public int getCartListQuantityFromKey(String Key){
        List<String> listKey = getCartListArticles();
        List<Integer> listQuantity = getCartListQuantity();
        int index = 0;
        for (int i=0; i<listKey.size(); i++){
            if(listKey.get(i).equals(Key))
                index = i;
        }
        return listQuantity.get(index);
    }

    public void printCart() throws DAOException {
        int size = this.cartMap.size();
        System.out.println("---Carrello---");

        List<String> articles = getCartListArticles();
        List<Integer> quantities = getCartListQuantity();

        for (int i = 0; i<size; i++){
            String articleName = getNomeArticoloFromBarcode(articles.get(i));
            Float price = getPrezzoArticoloFromBarcode(articles.get(i));
            Integer quantity = quantities.get(i);

            System.out.println(articleName + " " +quantity + "x " + price + " € = " +quantity*price + " €");
        }
    }

    public void backToSectionsFromPayment() {
        try {
            marketSectionLayoutController.syncCartPane();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        mainStage.setScene(marketSectionScene);
    }

    public void ejectCard(){
        loginController.resetForm();
        mainStage.setScene(loginSectionScene);
    }

    public void ejectCardAfterPayment(){
        //loginController.getMassimaliLabel().setText(massimali);
        loginController.getMassimaliLabel().setText("new massimali");
        //loginController.getPuntiFedeltaLabel().setText(puntiFedelta);
        loginController.getPuntiFedeltaLabel().setText("new punti fedelta");
        loginController.getMessageLabel().setText("Grazie per averci scelto!");
        mainStage.setScene(loginSectionScene);
    }
}
