package it.unicas.supermarket.model.dao;

import it.unicas.supermarket.App;
import it.unicas.supermarket.controller.LoginLayoutController;
import it.unicas.supermarket.model.*;
import it.unicas.supermarket.model.dao.mysql.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Util {

    private static final Logger logger =  Logger.getLogger(LoginLayoutController.class.getName());

    // from login Layout
    public static String getPinFromCodiceCarta(String codiceCarta) throws DAOException {
        List<Carte> cardToBeChecked = CarteDAOMySQL.getInstance().select(new Carte(-1, codiceCarta));
        if (cardToBeChecked.size() == 1)
            return cardToBeChecked.get(0).getPin();
        else return "ERROR";
    }

    public static String getCodiceClienteFromCodiceCarta(String codiceCarta) throws SQLException {

        Statement statement = DAOMySQLSettings.getStatement();

        String query = "SELECT clienti.codiceCliente " +
                "FROM Clienti JOIN Carte " +
                "ON Clienti.idCliente = Carte.idCliente " +
                "WHERE codiceCarta = '" + codiceCarta + "';";

        try{
            logger.info("SQL: " + query);
        }
        catch(NullPointerException nullPointerException){
            System.out.println("SQL: " + query);
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
        else return "ERROR";
    }

    public static float getMassimaleMensileFromCodiceCarta(String codiceCarta) throws DAOException {
        // select() function is codiceCarta-based
        List<Carte> card = CarteDAOMySQL.getInstance().select(new Carte(-1, codiceCarta));
        if( card.size() != 1)
            return -1;
        else
            return card.get(0).getMassimaleMensile();
    }

    public static Float getMassimaleRimanenteFromCodiceCarta(String codiceCarta) throws DAOException {
        // select() function is codiceCarta-based
        List<Carte> card = CarteDAOMySQL.getInstance().select(new Carte(-1, codiceCarta));
        if( card.size() != 1)
            return -1f;
        else {
            return card.get(0).getMassimaleRimanente();
            // restituisco massimale
        }
    }

    public static int getPuntiFedeltaFromCodiceCliente(String codiceCliente) throws DAOException {
        // select() function is codiceCliente-based
        List<Clienti> customer = ClientiDAOMySQL.getInstance().select(new Clienti("","",codiceCliente));
        if( customer.size() != 1)
            return -1;
        else {
            return customer.get(0).getPuntiFedelta();
        }
    }

    //from market section
    public static String getNomeArticoloFromBarcode(String barcode) throws DAOException {
        // select() function is barcode-based
        List<Articoli> article = ArticoliDAOMySQL.getInstance().select(new Articoli("", barcode));
        if( article.size() != 1)
            return "ERROR";
        else
            return article.get(0).getNome();
    }

    public static Float getPrezzoArticoloFromBarcode(String barcode) throws DAOException {
        // select() function is barcode-based
        List<Articoli> article = ArticoliDAOMySQL.getInstance().select(new Articoli("", barcode));
        if( article.size() != 1)
            return -1f;
        else
            return article.get(0).getPrezzo();
    }

    // from order-summary
    public static int getScorteFromBarcode(String barcode) throws DAOException {
        // select() function is barcode-based
        List<Articoli> article = ArticoliDAOMySQL.getInstance().select(new Articoli("", barcode));
        if( article.size() != 1)
            return -1;
        else
            return article.get(0).getScorteMagazzino();
    }

    public static void updateClienteAfterPayment(String codiceCarta, String codiceCliente, float newMassimaleRimanente, int newFidelity) throws DAOException {
        //copio dettagli clienti da select from barcode
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

    public static int getIdArticoloFromBarcode(String barcode) throws DAOException {
        // select() function is barcode-based
        List<Articoli> article = ArticoliDAOMySQL.getInstance().select(new Articoli("", barcode));
        if( article.size() != 1)
            throw new DAOException("Articolo non trovato");
        else
            return article.get(0).getIdArticolo();
    }

    public static Integer getIdOrdineFromCodiceOrdine(String codiceOrdine) throws DAOException {

        List<Ordini> ordiniList = OrdiniDAOMySQL.getInstance().select(new Ordini(codiceOrdine));
        System.out.println("getIdOrdine...");
        return ordiniList.get(0).getIdOrdine();
    }

    public static void sendOrderToDB(float totalImport) throws DAOException, SQLException {

        String customerCode = App.getInstance().getCodiceCliente();
        int customerId = getIdClienteFromCodiceCliente(customerCode);

        //XX-XX-XXXX XX:XX
        String data= "XX-XX-XXXX XX:XX";
        String orderCode ="Ordine_011";
        Ordini newOrder = new Ordini(customerId, data, orderCode, totalImport, null);
        System.out.println("Ordine Creato");
        // 1 - inserisco nuovo ordine
        OrdiniDAOMySQL.getInstance().insert(newOrder);
        System.out.println("Ordine Inserito");

        Integer idOrdine = getIdOrdineFromCodiceOrdine(orderCode);
        System.out.println("idOrdine ottenuto: " + idOrdine);

        // 2 - leggo idOrdine appena inserito (e' autoincrement)
        // 3 - per ciascun elemento dell'ordine, lo inserisco in composizioni passando l'id dell'ordine
        for (String barcode : App.getInstance().getCartMap().keySet() ){
            int quantita = App.getInstance().getCartMap().get(barcode);
            System.out.println("quantita:" + quantita);
            if (quantita>0){

                int idArticolo = getIdArticoloFromBarcode(barcode);
                float prezzo = getPrezzoArticoloFromBarcode(barcode);

                Composizioni articoloInOrdine = new Composizioni(idArticolo, idOrdine, prezzo, quantita);
                ComposizioniDAOMySQL.getInstance().insert(articoloInOrdine);
                System.out.println("Inserito in composizioni");
            }
        }

        printOrderDetailsFromCodiceOrdine(orderCode);
    }

    public static int getIdClienteFromCodiceCliente(String codiceCliente) throws DAOException {
        // select() function is barcode-based
        List<Clienti> customer = ClientiDAOMySQL.getInstance().select(new Clienti("","",codiceCliente));
        if( customer.size() != 1)
            throw new DAOException("Cliente non trovato...");
        else
            return customer.get(0).getIdCliente();
    }

    public static void printOrderDetailsFromCodiceOrdine(String codiceOrdine) throws DAOException, SQLException {
        /*
        if (articoloInOrdine == null)
            throw new DAOException("null instance of 'composizioni'...");
        // INSERT INTO Composizioni(idArticolo, idOrdini, prezzo, quantita) VALUES([...]);
        String query = "INSERT INTO Composizioni (idArticolo, idOrdini, prezzo, quantita) VALUES (" +
                articoloInOrdine.getIdArticolo() + ", " +
                articoloInOrdine.getIdOrdine() + ", " +
                articoloInOrdine.getPrezzo() + ", " +
                articoloInOrdine.getQuantita() + ")";

        printQuery(query);

        executeUpdate(query);
        */
        int idOrdine = getIdOrdineFromCodiceOrdine(codiceOrdine);
        System.out.println("IdOrdine =" + idOrdine);
        Statement statement = DAOMySQLSettings.getStatement();

        String query = "SELECT Articoli.nome, Articoli.prezzo, Composizioni.quantita" +
                " FROM Composizioni JOIN Articoli" +
                " ON Composizioni.idArticolo = Articoli.idArticolo" +
                " WHERE idOrdine = " + idOrdine + ";";

        // printQuery()
        try {
            logger.info("SQL: " + query);
        } catch (NullPointerException nullPointerException) {
            System.out.println("SQL: " + query);
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

        DAOMySQLSettings.closeStatement(statement);

        System.out.println("--- ORDINE " + codiceOrdine + " ---");

        for (int i = 0; i< articleNames.size(); i++){
            System.out.println(articleNames.get(i) + "\t( " + articleQuantities.get(i) + "x " + articlePrices.get(i) + " €)");
        }
    }

    /*
    01) inserimento ordine in DB
    es. ordine fatto da idCLiente in data 'data'...
    INSERT INTO Ordini(idOrdine, idCliente, data, codiceOrdine, importoTotale) VALUES([...]);
    -> inserimento articoli dell'ordine
    INSERT INTO Composizioni(idArticolo, idOrdini, prezzo, quantita) VALUES([...]);
    INSERT INTO Composizioni(idArticolo, idOrdini, prezzo, quantita) VALUES([...]);
    INSERT INTO Composizioni(idArticolo, idOrdini, prezzo, quantita) VALUES([...]);
    INSERT INTO Composizioni(idArticolo, idOrdini, prezzo, quantita) VALUES([...]);
    INSERT INTO Composizioni(idArticolo, idOrdini, prezzo, quantita) VALUES([...]);

    02) tutti gli articoli contenuti nell'ordine
    SELECT Articoli.idArticolo, Composizioni.quantita
    FROM Composizioni
	JOIN Articoli ON Composizioni.idArticolo = Articolo.idArticolo
    WHERE idOrdine = 1;
    */
}
