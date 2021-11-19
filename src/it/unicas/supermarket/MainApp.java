package it.unicas.supermarket;
import it.unicas.supermarket.model.Carte;
import it.unicas.supermarket.model.Clienti;
import it.unicas.supermarket.model.Ordini;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.mysql.CarteDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.ClientiDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.OrdiniDAOMySQL;
import it.unicas.supermarket.view.RootLayoutController;
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
    // private BorderPane rootLayout;
    private AnchorPane rootLayout;

    /**
     * Constructor
     */
    public MainApp() {

        try {
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

        ClientiDAOMySQL.getInstance().initialize();
        CarteDAOMySQL.getInstance().initialize();
        OrdiniDAOMySQL.getInstance().initialize();

        System.out.println("DB initialization: SUCCESS\nPrinting tables");

        List<Clienti> listaClienti = ClientiDAOMySQL.getInstance().selectAll();
        List<Carte> listaCarte = CarteDAOMySQL.getInstance().selectAll();
        List<Ordini> listaOrdini = OrdiniDAOMySQL.getInstance().selectAll();

        System.out.println("---CLIENTI---");
        listaClienti.forEach(System.out::println);

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

        // Set the application icon.
        this.primaryStage.getIcons().add(new Image("file:resources/images/shopping-cart.png"));

        initRootLayout();

        // Clienti_test_mainpage();
        this.primaryStage.show();

    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }


    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/RootLayout.fxml"));

            // rootLayout = (BorderPane)loader.load();
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            primaryStage.setOnCloseRequest(event -> {
                event.consume();
                handleExit();
            });


            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
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

}
