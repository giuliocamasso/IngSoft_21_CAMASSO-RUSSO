package it.unicas.supermarket;
import it.unicas.supermarket.dataloader.Dataloader;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.mysql.*;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * <p>
 *     Progetto Software Engineering 2021-2022 <br>
 *     Docente: Mario Molinara <br>
 *     Traccia: Supermercato <br>
 * </p>
 *

 * @see <a href="https://www.unicas.it/siti/dipartimenti/diei/didattica/corsi-di-studio/schedacds/insegnamento.aspx?UID=688b00c1-7d87-4ff7-99db-1efdd619242b&Insegnamento=93646&Curriculum=5af57d14-89ca-47a4-9c25-13efd2d558f6">Pagina del corso</a>
 *
 * @author Camasso Giulio - Russo Giulio
 */
public class Main extends Application {

    public Main() {}

    /**
     * Quando chiamata dallo start() di Main() (it/unicas/supermarket/Main.java), svuota il db corrente
     * e lo riempie leggendo i dati dai file .txt contenuti in it/unicas/supermarket/dataloader.
     */
    private void initializeDB() throws DAOException, IOException {
        // svuoto il database
        CarteDAOMySQL.getInstance().deleteAll();
        ComposizioniDAOMySQL.getInstance().deleteAll();
        OrdiniDAOMySQL.getInstance().deleteAll();
        ClientiDAOMySQL.getInstance().deleteAll();
        ArticoliDAOMySQL.getInstance().deleteAll();

        // leggo da file
        Dataloader.loadClienti("src/it/unicas/supermarket/dataloader/Clienti.txt", " - ");
        Dataloader.loadArticoli("src/it/unicas/supermarket/dataloader/Articoli.txt", " - ");
        Dataloader.loadCarte("src/it/unicas/supermarket/dataloader/Carte.txt", " - ");
        Dataloader.loadOrdini("src/it/unicas/supermarket/dataloader/Ordini.txt", " - ");
    }

    /**
     * Il primo metodo lanciato dall'applicazione. Opzionalmente resetta il database,
     * e chiama il metodo launch() di App. (it/unicas/supermarket/App.java)
     */
    @Override
    public void start(Stage primaryStage) {
        // Inizializzo il db [opzionale]
        /*
        try {
            initializeDB();
        } catch (DAOException | IOException e) {
            e.printStackTrace();
        }
        */

        App.getInstance().launch(primaryStage);
    }
}