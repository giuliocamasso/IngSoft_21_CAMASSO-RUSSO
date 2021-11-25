package it.unicas.supermarket;
import it.unicas.supermarket.model.Articoli;
import it.unicas.supermarket.model.Carte;
import it.unicas.supermarket.model.Clienti;
import it.unicas.supermarket.model.Ordini;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.mysql.ArticoliDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.CarteDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.ClientiDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.OrdiniDAOMySQL;
import it.unicas.supermarket.view.LoginLayoutController;
import it.unicas.supermarket.view.MarketSectionLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MainApp extends Application {

    private Stage primaryStage;

    private AnchorPane loginLayout;

    private AnchorPane marketSectionLayout;

    /**
     * Constructor
     */
    public MainApp() {

        boolean initializeDB = false;

        try {
            if (initializeDB)
                initializeDB();
        }
        catch (DAOException e) {
            e.printStackTrace();
        }
    }

    private void initializeDB() throws DAOException {

        System.out.println("---initializeDB()---\n");

        System.out.println("Resetting DB: ...");

        // NB.
        // delete order: cards/orders     ->  clients/articles
        // insert order: clients/articles ->  cards/orders
        CarteDAOMySQL.getInstance().deleteAll();
        OrdiniDAOMySQL.getInstance().deleteAll();
        ClientiDAOMySQL.getInstance().deleteAll();
        ArticoliDAOMySQL.getInstance().deleteAll();


        ClientiDAOMySQL.getInstance().initialize();
        ArticoliDAOMySQL.getInstance().initialize();
        CarteDAOMySQL.getInstance().initialize();
        OrdiniDAOMySQL.getInstance().initialize();

        System.out.println("DB initialization: SUCCESS\nPrinting tables");

        List<Clienti> listaClienti = ClientiDAOMySQL.getInstance().selectAll();
        List<Articoli> listaArticoli = ArticoliDAOMySQL.getInstance().selectAll();
        List<Carte> listaCarte = CarteDAOMySQL.getInstance().selectAll();
        List<Ordini> listaOrdini = OrdiniDAOMySQL.getInstance().selectAll();

        System.out.println("---CLIENTI---");
        listaClienti.forEach(System.out::println);

        System.out.println("---ARTICOLI---");
        listaArticoli.forEach(System.out::println);

        System.out.println("\n---CARTE---");
        listaCarte.forEach(System.out::println);

        System.out.println("\n---ORDINI---");
        listaOrdini.forEach(System.out::println);

    }


    @Override
    public void start(Stage primaryStage) {

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

    public static void main(String[] args) {
        launch(args);
    }


    public void initLoginLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LoginLayout.fxml"));

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
            controller.setMainApp(this);

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
            loader.setLocation(MainApp.class.getResource("view/MarketSectionLayout.fxml"));

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
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
}
}

