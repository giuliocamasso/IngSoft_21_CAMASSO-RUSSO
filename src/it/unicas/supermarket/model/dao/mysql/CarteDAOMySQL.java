package it.unicas.supermarket.model.dao.mysql;
import it.unicas.supermarket.model.Carte;
import it.unicas.supermarket.model.Clienti;
import it.unicas.supermarket.model.dao.DAO;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static it.unicas.supermarket.model.dao.mysql.ClientiDAOMySQL.getIdClienteFromCode;

/**
 * Classe DAO della tabella Carte
 */
public class CarteDAOMySQL implements DAO<Carte> {

    private static DAO<Carte> dao = null;
    private static Logger logger = null;

    private CarteDAOMySQL() {}

    public static DAO<Carte> getInstance(){
        if (dao == null){
            dao = new CarteDAOMySQL();
            logger = Logger.getLogger(CarteDAOMySQL.class.getName());
        }
        return dao;
    }

    /**
     * Il metodo inizializza la tabella per poter eseguire dei test
     */
    @Override
    public void initialize() throws DAOException {

        for (int i = 0; i<10; i++){
            Integer id_carta = i;
            Float massimaleMensile = i*1000f;
            Float massimaleRimanente = i*100f;
            String codiceCliente_i = "codice_" + i;
            Integer id_cliente = getIdClienteFromCode(codiceCliente_i);
            String pin = "pin_" + i;
            String seed4_i = ""+ i + i + i + i;

            insert(new Carte( massimaleMensile, massimaleRimanente, id_cliente, pin, Carte.ghostCardCode(seed4_i), null));

        }

        // also shows the tuples
        ArrayList<Carte> list =  selectAll();
        list.forEach(System.out::println);
    }


    /**
     * Metodo di test per le CRUD della classe Articoli
     */
    public static void main(String[] args) throws DAOException {

        boolean initialize = false;
        // boolean initialize = false;

        CarteDAOMySQL c = new CarteDAOMySQL();

        if (initialize){
            c.deleteAll();
            c.initialize();
        }

        // testing
        else {

            c.deleteAll();

            // 1 - testing Insert
            List<Clienti> clientiList = ClientiDAOMySQL.getInstance().selectAll();
            clientiList.forEach(System.out::println);

            Integer idCliente = getIdClienteFromCode("codice_1");
            c.insert(new Carte(100f, 10f, idCliente, "11111", "1111-1111-1111-1111", null));

            idCliente = getIdClienteFromCode("codice_2");
            c.insert(new Carte(idCliente,"2222-2222-2222-2222"));

            // 2 - testing select all
            List<Carte> list = c.selectAll();
            list.forEach(System.out::println);

            // 3 - testing delete
            // 3.1 delete all
            c.deleteAll();
            list = c.selectAll();
            list.forEach(System.out::println);

            // 3.2 delete
            idCliente = getIdClienteFromCode("codice_1");
            c.insert(new Carte(100f, 10f, idCliente,"11111", "1111-1111-1111-1111", null));

            idCliente = getIdClienteFromCode("codice_2");
            c.insert(new Carte(idCliente, "2222-2222-2222-2222"));

            idCliente = getIdClienteFromCode("codice_3");
            c.insert(new Carte(idCliente, "3333-3333-3333-3333"));

            idCliente = getIdClienteFromCode("codice_1");
            Carte toDelete = new Carte(0f, 0f, idCliente,"11111", "1111-1111-1111-1111", null);

            idCliente = getIdClienteFromCode("codice_2");
            Carte toDelete2 = new Carte(idCliente, "2222-2222-2222-2222");

            c.delete(toDelete);
            c.delete(toDelete2);

            // show remaining tuples
            list = c.selectAll();
            list.forEach(System.out::println);

            // 3.3 update
            idCliente = getIdClienteFromCode("codice_4");
            Carte toUpdate = new Carte(400f, 40f, idCliente,"44444", "4444-4444-4444-4444", null);

            idCliente = getIdClienteFromCode("codice_5");
            Carte toUpdate2 = new Carte(idCliente, "5555-5555-5555-5555");

            c.insert(toUpdate);
            c.insert(toUpdate2);

            list = c.selectAll();
            list.forEach(System.out::println);

            idCliente = getIdClienteFromCode("codice_4");
            Carte updated = new Carte(400f, 40f,idCliente,"44444", "4444-4444-4444-4444", null);

            idCliente =getIdClienteFromCode("codice_5");
            Carte updated2 = new Carte(500f, 50f,idCliente,"55555", "5555-5555-5555-5555", null);

            c.update(updated);
            c.update(updated2);

            // shows the updated tuple (all db to catch errors)
            list = c.selectAll();
            list.forEach(System.out::println);

        } // end of testing
    }


    /**
     * Select
     * @param a Carta da cui estrarre i campi di selezione
     * @return Lista di carte estratti dalla query
     */
    @Override
    public List<Carte> select(Carte a) throws DAOException {

        ArrayList<Carte> lista;

        if (a==null)
            throw new DAOException("In select: called select with a 'null' instance of Carte");

        try{
            if (a.getIdCarta()!= null)
                throw new DAOException("In select: idCarta must be null in select");

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "select * from carte where (codiceCarta ='" + a.getCodiceCarta() + "')";

            if(Util.isQueryPrintingEnabled())
                printQuery(sql);

            lista = getQueryResult(st, sql);

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In select(): " + sq.getMessage());
        }

        return lista;
    }


    /**
     * Select di tutta la tabella Carte
     * @return
     */
    public ArrayList<Carte> selectAll() {

        ArrayList<Carte> list = new ArrayList<>();

        try {
            Statement statement = DAOMySQLSettings.getStatement();

            String sql = "select * from carte";

            if (Util.isQueryPrintingEnabled())
                printQuery(sql);

            list = getQueryResult(statement, sql);

            DAOMySQLSettings.closeStatement(statement);
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    /**
     * Delete
     * @param a Carta da cui estrarre i campi per l'eliminazione
     */
    @Override
    public void delete(Carte a) throws DAOException {

        if (a == null) {
            throw new DAOException("In delete: can't delete a null instance");
        }
        if (a.getCodiceCarta() == null){
            throw new DAOException("In delete: codiceCarta can't be null");
        }

        String query = "DELETE FROM carte WHERE codiceCarta='" + a.getCodiceCarta() + "';";

        if (Util.isQueryPrintingEnabled())
            printQuery(query);

        executeUpdate(query);

    }


    /**
     * Delete di tutta la tabella Carte
     */
    public void deleteAll() throws DAOException {

        String query = "delete from carte";

        if (Util.isQueryPrintingEnabled())
            printQuery(query);

        executeUpdate(query);
    }


    /**
     * Insert
     * @param a Carta da cui estrarre i campi per l'inserimento
     */
    @Override
    public void insert(Carte a) throws DAOException {

        if ( a == null)
            throw new DAOException("Cannot insert a null instance of carte");

        //NB. idCarte = NULL (autoincrement in MySql)
        String query = "INSERT INTO carte (idCarta, massimaleMensile, massimaleRimanente, idCliente, pin, codiceCarta) VALUES (NULL, '" +
                a.getMassimaleMensile() + "', '" +
                a.getMassimaleRimanente() + "', " +
                a.getIdCliente() + ", '" +
                a.getPin() + "', '" +
                a.getCodiceCarta() + "')";
        if (Util.isQueryPrintingEnabled())
            printQuery(query);

        executeUpdate(query);
    }


    /**
     * Alter Tabel
     * @param a Carta da cui estrarre i campi per la modifica della tabella. Basata sul codice della carta
     */
    @Override
    public void update(Carte a) throws DAOException {

        if ( a == null)
            throw new DAOException("cannot update with a null instance of carte");

        if ( a.getCodiceCarta() == null)
            throw new DAOException("cannot update with a null codiceCarta");

        String query = "UPDATE carte SET massimaleMensile = "
                + a.getMassimaleMensile() + ", massimaleRimanente = "
                + a.getMassimaleRimanente();

        query = query + " WHERE codiceCarta = '" + a.getCodiceCarta() + "';";

        if (Util.isQueryPrintingEnabled())
            printQuery(query);

        executeUpdate(query);

    }


    private void executeUpdate(String query) throws DAOException{
        try {
            Statement st = DAOMySQLSettings.getStatement();
            int n = st.executeUpdate(query);

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException("In insert(): " + e.getMessage());
        }
    }


    private void printQuery(String query){
        try{
            logger.info("SQL: " + query);
        }
        catch(NullPointerException nullPointerException){
            System.out.println("SQL: " + query);
        }
    }


    private ArrayList<Carte> getQueryResult(Statement statement, String query) throws SQLException {

        ArrayList<Carte> list = new ArrayList<>();

        ResultSet rs = statement.executeQuery(query);

        while(rs.next()){
            try {
                list.add(new Carte(
                       rs.getFloat("massimaleMensile"),
                       rs.getFloat("massimaleRimanente"),
                       rs.getInt("idCliente"),
                       rs.getString("pin"),
                       rs.getString("codiceCarta"),
                       rs.getInt("idCarta"))
                        );
            } catch (DAOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

}