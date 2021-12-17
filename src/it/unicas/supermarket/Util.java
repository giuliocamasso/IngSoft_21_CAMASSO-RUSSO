package it.unicas.supermarket;
import it.unicas.supermarket.controller.LoginLayoutController;
import it.unicas.supermarket.model.*;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.mysql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * La classe Util e' stata creata per raggruppare tutti i metodi di utilita' necessari all'applicazione<br>
 * Contiene anche due flag booleani 'queryPrintingEnabled' e 'dbInitializationEnabled' che abilitano o meno l'inizializzazione del db da file, e la stampa delle query eseguite su console
 */
public class Util {

    private static final boolean queryPrintingEnabled = false;
    private static final boolean dbInitializationEnabled = false;
    private static final Logger logger =  Logger.getLogger(LoginLayoutController.class.getName());

    // getter dei flag
    public static boolean isQueryPrintingEnabled()              { return queryPrintingEnabled; }
    public static boolean isDbInitializationEnabled()           { return dbInitializationEnabled; }

    /**
     * Metodo di utilita' che cerca sul db il PIN di una carta
     * @param codiceCarta Il codice della carta di cui si vuole leggere il PIN
     * @return Resituisce il pin associato alla carta data in input
     */
    public static String getPinFromCodiceCarta(String codiceCarta) throws DAOException {
        List<Carte> cardToBeChecked = CarteDAOMySQL.getInstance().select(new Carte(-1, codiceCarta));
        if (cardToBeChecked.size() == 1)
            return cardToBeChecked.get(0).getPin();
        else throw new DAOException("Error");
    }

    /**
     * Il metodo resituisce il codice del cliente associato alla carta passata in ingresso
     * @param codiceCarta In ingresso riceve il codice della carta
     * @return Restituisce il codice del cliente
     */
    public static String getCodiceClienteFromCodiceCarta(String codiceCarta) throws SQLException {

        Statement statement = DAOMySQLSettings.getStatement();

        String query = "SELECT clienti.codiceCliente " +
                "FROM Clienti JOIN Carte " +
                "ON Clienti.idCliente = Carte.idCliente " +
                "WHERE codiceCarta = '" + codiceCarta + "';";

        if (queryPrintingEnabled){
            try {
                logger.info("SQL: " + query);
            }
            catch (NullPointerException nullPointerException) {
                System.out.println("SQL: " + query);
            }
        }

        ArrayList<String> result = new ArrayList<>();

        ResultSet rs = statement.executeQuery(query);
        while(rs.next()){
            result.add(rs.getString("codiceCliente"));
        }

        DAOMySQLSettings.closeStatement(statement);

        if (result.size() == 1) {
            App.getInstance().setCodiceCliente(result.get(0));
            return result.get(0);
        }

        // 1 - more than one client is correlated to the given card
        // OR
        // 2 - client not found (db error, something went wrong)
        else throw new SQLException("Error");
    }

    /**
     * Il metodo restituisce il massimale mensile concordato con il proprietario della carta
     * @param codiceCarta In ingresso riceve il codice della carta
     * @return Restituisce il massimale associato alla carta
     */
    public static float getMassimaleMensileFromCodiceCarta(String codiceCarta) throws DAOException {
        // nb. la select() e' basata sul codice carta
        List<Carte> card = CarteDAOMySQL.getInstance().select(new Carte(-1, codiceCarta));
        if( card.size() != 1)
            throw new DAOException("Error");
        else
            return card.get(0).getMassimaleMensile();
    }

    /**
     * Il metodo restituisce il massimale rimanente concordato con il proprietario della carta
     * @param codiceCarta In ingresso riceve il codice della carta
     * @return Restituisce il massimale rimanente associato alla carta
     */
    public static Float getMassimaleRimanenteFromCodiceCarta(String codiceCarta) throws DAOException {

        List<Carte> card = CarteDAOMySQL.getInstance().select(new Carte(-1, codiceCarta));
        if( card.size() != 1)
            throw new DAOException("Error");
        else
            return card.get(0).getMassimaleRimanente();
    }

    /**
     * Il metodo restituisce i punti fedelta' posseduti dal cliente
     * @param codiceCliente In ingresso riceve il codice del cliente
     * @return Restituisce i punti fedelta' del cliente
     */
    public static int getPuntiFedeltaFromCodiceCliente(String codiceCliente) throws DAOException {

        List<Clienti> customer = ClientiDAOMySQL.getInstance().select(new Clienti("","",codiceCliente));
        if( customer.size() != 1)
            throw new DAOException("Error");
        else
            return customer.get(0).getPuntiFedelta();
    }

    /**
     * Il metodo restituisce il nome di un articolo a partire dal barcode che lo identifica
     * @param barcode In ingresso riceve il codice a barre dell'articolo da ricercare
     * @return Restituisce il nome dell'articolo
     */
    public static String getNomeArticoloFromBarcode(String barcode) throws DAOException {

        List<Articoli> article = ArticoliDAOMySQL.getInstance().select(new Articoli("", barcode));
        if( article.size() != 1)
            throw new DAOException("Error");
        else
            return article.get(0).getNome();
    }

    /**
     * Il metodo restituisce il prezzo di un articolo a partire dal suo barcode
     * @param barcode In ingresso riceve il codice dell'articolo
     * @return Restituisce il prezzo dell'articolo cercato
     */
    public static Float getPrezzoArticoloFromBarcode(String barcode) throws DAOException {

        List<Articoli> article = ArticoliDAOMySQL.getInstance().select(new Articoli("", barcode));
        if( article.size() != 1)
           throw new DAOException("Error");
        else
            return article.get(0).getPrezzo();
    }

    /**
     * Il metodo restituisce l'ammontare delle scorte presenti in magazzino, dell'articolo passato in ingresso
     * @param barcode In ingresso riceve il codice a barre dell'articolo da controllare
     * @return Restituisce la quantita' presente in magazzino dell'articolo selezionato
     */
    public static int getScorteFromBarcode(String barcode) throws DAOException {

        List<Articoli> article = ArticoliDAOMySQL.getInstance().select(new Articoli("", barcode));
        if( article.size() != 1)
            throw new DAOException("Error");
        else
            return article.get(0).getScorteMagazzino();
    }

    /**
     * Il metodo aggiorna sul db i dati di un cliente dopo un acquisto<br>
     * In particolare si aggiornano il massimale rimanente dell'utente, e i suoi punti fedelta'
     * @param codiceCarta Il codice della carta associata al cliente (NB. 1:1)
     * @param codiceCliente Il codice del cliente da aggiornare
     * @param newMassimaleRimanente Il nuovo massimale da inserire nel db
     * @param newFidelity I nuovi punti fedelta'
     */
    public static void updateClienteAfterPayment(String codiceCarta, String codiceCliente, float newMassimaleRimanente, int newFidelity) throws DAOException {

        List<Clienti> customerToUpdate = ClientiDAOMySQL.getInstance().select(new Clienti("","",codiceCliente));
        List<Carte> cardToUpdate = CarteDAOMySQL.getInstance().select(new Carte(-1,codiceCarta));

        if(customerToUpdate.size()!=1)
            throw new DAOException("Impossibile aggiornare i dati del cliente...");
        else{
            customerToUpdate.get(0).setPuntiFedelta(newFidelity);
            ClientiDAOMySQL.getInstance().update(customerToUpdate.get(0));
        }

        if(cardToUpdate.size()!=1)
            throw new DAOException("Impossibile aggiornare i dati della carta");
        else{
            cardToUpdate.get(0).setMassimaleRimanente(newMassimaleRimanente);
            CarteDAOMySQL.getInstance().update(cardToUpdate.get(0));
        }
    }

    /**
     * Il metodo restituisce l'id (autoincrement nel db) posseduto dall'articolo identificato dal barcode
     * @param barcode In ingresso riceve il codice identificativo dell'articolo
     * @return Restituisce l'id del'articolo nel db
     */
    public static int getIdArticoloFromBarcode(String barcode) throws DAOException {

        List<Articoli> article = ArticoliDAOMySQL.getInstance().select(new Articoli("", barcode));
        if( article.size() != 1)
            throw new DAOException("Articolo non trovato");
        else
            return article.get(0).getIdArticolo();
    }

    /**
     * Il metodo restituisce l'id autoincrement di un ordine, identificato dal suo codice
     * @param codiceOrdine Il codice dell'ordine da cercare
     * @return Restituisce l'id dell'ordine nel db
     */
    public static Integer getIdOrdineFromCodiceOrdine(String codiceOrdine) throws DAOException {
        List<Ordini> ordiniList = OrdiniDAOMySQL.getInstance().select(new Ordini(codiceOrdine));
        if (ordiniList.size()!=1)
            throw new DAOException("Error");
        else
            return ordiniList.get(0).getIdOrdine();
    }

    /**
     * Il metodo e' chiamato alla finalizzazione di un pagamento<br>
     * Inserisce l'ordine nel db, e aggiorna la tabella Composizioni inserendo gli articoli acquistati
     * nell'ordine con le relative quantita'
     * NB.1 Le altre informazioni legate all'utente (a parte l'importo) sono presenti in App e non serve passarle in ingresso<br>
     * NB.2 Viene generato un codice ordine univoco associato alla data e all'ora corrente al momento della chiamata
     * @param totalImport l'importo totale associato all'ordine da inserire nel db
     */
    public static void sendOrderToDB(float totalImport) throws DAOException, SQLException {

        String customerCode = App.getInstance().getCodiceCliente();
        int customerId = getIdClienteFromCodiceCliente(customerCode);

        // formato data atteso: XX-XX-XXXX XX:XX
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String data = dtf.format(now);

        // generazione codice ordine univoco
        DateTimeFormatter dtfOrdine = DateTimeFormatter.ofPattern("ddMMHHmmss");
        String orderCode = dtfOrdine.format(now);

        Ordini newOrder = new Ordini(customerId, data, orderCode, totalImport, null);

        // inserisco nuovo ordine
        OrdiniDAOMySQL.getInstance().insert(newOrder);

        // leggo l'id (autoincrement) dell'ordine appena inserito
        Integer idOrdine = getIdOrdineFromCodiceOrdine(orderCode);

        // per ciascun elemento dell'ordine, lo inserisco in composizioni passando l'id dell'ordine
        for (String barcode : App.getInstance().getCartMap().keySet() ){
            int quantita = App.getInstance().getCartMap().get(barcode);
            // nella mappa carrello possono essere presenti articoli la cui quantita' e' stata portata a zero
            if (quantita>0){

                int idArticolo = getIdArticoloFromBarcode(barcode);
                float prezzo = getPrezzoArticoloFromBarcode(barcode);

                Composizioni articoloInOrdine = new Composizioni(idArticolo, idOrdine, prezzo, quantita);
                ComposizioniDAOMySQL.getInstance().insert(articoloInOrdine);
            }
        }

        // log su console dell'inserimento
        printOrderDetailsFromCodiceOrdine(orderCode);
    }

    /**
     * Il metodo restituisce l'id autoincrement del cliente associato al codice passato in ingresso
     * @param codiceCliente Il codice del cliente da cercare
     * @return L'id autoincrement del cliente nel db
     */
    public static int getIdClienteFromCodiceCliente(String codiceCliente) throws DAOException {

        List<Clienti> customer = ClientiDAOMySQL.getInstance().select(new Clienti("","",codiceCliente));
        if( customer.size() != 1)
            throw new DAOException("Cliente non trovato...");
        else
            return customer.get(0).getIdCliente();
    }

    /**
     * Il metodo stampa su console un log con i dettagli dell'ordine di pagamento (codice ordine, codice cliente, iban, lista articoli)
     * @param codiceOrdine Il codice dell'ordine di cui si vuole stampare il log
     */
    public static void printOrderDetailsFromCodiceOrdine(String codiceOrdine) throws DAOException, SQLException {

        int idOrdine = getIdOrdineFromCodiceOrdine(codiceOrdine);

        Statement statement = DAOMySQLSettings.getStatement();

        // primo join Composizioni-Articoli per collezionare i dettagli degli articoli
        String query =  "SELECT Articoli.nome, Articoli.prezzo, Composizioni.quantita" +
                        " FROM Composizioni JOIN Articoli" +
                        " ON Composizioni.idArticolo = Articoli.idArticolo" +
                        " WHERE idOrdine = " + idOrdine + ";";

        if(queryPrintingEnabled) {
            try {
                logger.info("SQL: " + query);
            }
            catch (NullPointerException nullPointerException) {
                System.out.println("SQL: " + query);
            }
        }

        ArrayList<String> articleNames = new ArrayList<>();
        ArrayList<Float> articlePrices = new ArrayList<>();
        ArrayList<Integer> articleQuantities = new ArrayList<>();

        ResultSet rs = statement.executeQuery(query);

        while(rs.next()){
            articleNames.add(rs.getString("Articoli.nome"));
            articlePrices.add(rs.getFloat("Articoli.prezzo"));
            articleQuantities.add(rs.getInt("Composizioni.quantita"));
        }

        // secondo join Clienti-Ordini per collezionare i dati del cliente
        query = "SELECT Clienti.codiceCliente, Clienti.iban" +
                " FROM Ordini JOIN Clienti" +
                " ON Ordini.idCliente = CLienti.idCliente" +
                " WHERE idOrdine = " + idOrdine + ";";

        if(queryPrintingEnabled) {
            try {
                logger.info("SQL: " + query);
            }
            catch (NullPointerException nullPointerException) {
                System.out.println("SQL: " + query);
            }
        }

        ArrayList<String> codiceCliente = new ArrayList<>();
        ArrayList<String> ibanCliente = new ArrayList<>();

        rs = statement.executeQuery(query);

        while(rs.next()){
            codiceCliente.add(rs.getString("Clienti.codiceCliente"));
            ibanCliente.add(rs.getString("Clienti.iban"));
        }

        DAOMySQLSettings.closeStatement(statement);

        // stampa dell'ordine di pagamento su console
        System.out.println("--- FATTURA ---");
        System.out.println("Ordine N. " + codiceOrdine);
        System.out.println("Cliente " + codiceCliente.get(0) + "\n");

        for (int i = 0; i< articleNames.size(); i++){
            System.out.println(articleNames.get(i) + "\t( " + articleQuantities.get(i) + "x " + articlePrices.get(i) + " €)");
        }
        System.out.println("\n --- Ordine di pagamento inviato a " + ibanCliente.get(0) + " ---\n");
    }
}
