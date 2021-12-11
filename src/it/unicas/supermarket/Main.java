package it.unicas.supermarket;

import it.unicas.supermarket.dataloader.Dataloader;

import it.unicas.supermarket.model.Articoli;
import it.unicas.supermarket.model.Carte;
import it.unicas.supermarket.model.Clienti;
import it.unicas.supermarket.model.Ordini;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.mysql.ArticoliDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.CarteDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.ClientiDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.OrdiniDAOMySQL;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class Main extends Application {

    // PROVA MODIFICA
    public Main() {}

    private void initializeDB() throws DAOException, IOException {

        System.out.println("---initializeDB()---\n");

        System.out.println("Resetting DB: ...");

        // NB.
        // delete order: cards/orders     ->  clients/articles
        // insert order: clients/articles ->  cards/orders
        CarteDAOMySQL.getInstance().deleteAll();
        OrdiniDAOMySQL.getInstance().deleteAll();
        ClientiDAOMySQL.getInstance().deleteAll();
        ArticoliDAOMySQL.getInstance().deleteAll();

        Dataloader.loadClienti("src/it/unicas/supermarket/dataloader/Clienti.txt", " - ");
        Dataloader.loadArticoli("src/it/unicas/supermarket/dataloader/Articoli.txt", " - ");
        Dataloader.loadCarte("src/it/unicas/supermarket/dataloader/Carte.txt", " - ");
        Dataloader.loadOrdini("src/it/unicas/supermarket/dataloader/Ordini.txt", " - ");

        /*
        // DEPRECATED
        ClientiDAOMySQL.getInstance().initialize();
        ArticoliDAOMySQL.getInstance().initialize();
        CarteDAOMySQL.getInstance().initialize();
        OrdiniDAOMySQL.getInstance().initialize();
        */

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

        boolean initializeDB = false;

        try {
            if (initializeDB)
                initializeDB();
            App.getInstance().launch(primaryStage);
        }
        catch (DAOException | IOException e){
            e.printStackTrace();
        }
    }
}