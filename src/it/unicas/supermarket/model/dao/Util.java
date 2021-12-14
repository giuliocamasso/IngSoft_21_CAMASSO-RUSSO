package it.unicas.supermarket.model.dao;

import it.unicas.supermarket.App;
import it.unicas.supermarket.controller.LoginLayoutController;
import it.unicas.supermarket.model.Articoli;
import it.unicas.supermarket.model.Carte;
import it.unicas.supermarket.model.Clienti;
import it.unicas.supermarket.model.dao.mysql.ArticoliDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.CarteDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.ClientiDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.DAOMySQLSettings;

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
}
