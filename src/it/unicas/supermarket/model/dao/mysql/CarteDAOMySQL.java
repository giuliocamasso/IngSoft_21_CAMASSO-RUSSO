package it.unicas.supermarket.model.dao.mysql;

import it.unicas.supermarket.model.Carte;
import it.unicas.supermarket.model.dao.DAO;
import it.unicas.supermarket.model.dao.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


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

    // Testing Class
    public static void main(String[] args) throws DAOException {

        boolean initialize = false;
        // boolean initialize = false;

        CarteDAOMySQL c = new CarteDAOMySQL();

        if (initialize){
            c.deleteAll();
            //c.initialize();
        }

        // testing
        else {

            c.deleteAll();

            // 1 - testing Insert
            c.insert(new Carte(1, 1, 1000f, 100f));

            // 2 - testing select all
            // NB. select(null) is deprecated, a new method(selectAll()) was created
            List<Carte> list = c.selectAll();

            list.forEach(System.out::println);

            // 3 - testing delete
            // 3.1 delete all
            c.deleteAll();
            list = c.selectAll();

            list.forEach(System.out::println);

            // 3.2 delete
            // add 2 tuples
            c.insert(new Carte(1, 1, 1000f, 100f));
            c.insert(new Carte(2, 2, 2000f, 200f));

            // could be improved... actually only removes tuple by idCarta
            Carte toDelete = new Carte(2, null, null, null );

            c.delete(toDelete);

            // show remaining tuples
            list = c.selectAll();
            list.forEach(System.out::println);

            // 3.3 update
            // insert a new tuple to update
            Carte toUpdate = new Carte(3, 3, 3000f, 300f);
            c.insert(toUpdate);

            // print the new tuple
            list = c.select(toUpdate);
            list.forEach(System.out::println);
            // call the update()

            // NB. idCliente is a foreignKey for 'Carte', and cannot be updated
            // Carte updated = new Carte(3, 30, 3333f, 333f);
            Carte updated = new Carte(3, 3, 3333f, 333f);
            c.update(updated);

            // shows the updated tuple (all db to catch errors)
            list = c.selectAll();
            list.forEach(System.out::println);
        } // end of testing
    }

    @Override
    public List<Carte> select(Carte a) throws DAOException {

        ArrayList<Carte> lista;

        if (a==null)
            throw new DAOException("In select: called select with a 'null' instance of Carte");

        try{

            if (    a.getIdCarta()              == null ||
                    a.getCliente_idCliente()    == null ||
                    a.getMassimaleRimanente()   == null ||
                    a.getMassimaleRimanente()   == null ) {
                throw new DAOException("In select: any field can be null");
            }

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "select * from carta where (idCarta =";
            sql += a.getIdCarta()+ ")";

            printQuery(sql);

            lista = getQueryResult(st, sql);

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In select(): " + sq.getMessage());
        }

        return lista;
    }

    @Override
    // delete key-based
    public void delete(Carte a) throws DAOException {

        if (a == null) {
            throw new DAOException("In delete: can't delete a null instance");
        }
        if (a.getIdCarta() == null || a.getCliente_idCliente() == null){
            throw new DAOException("In delete: idCarta or id Colleghi cannot be null");
        }

        String query = "DELETE FROM carta WHERE idCarta='" + a.getIdCarta() + "';";

        printQuery(query);

        executeUpdate(query);

    }

    public ArrayList<Carte> selectAll() {

        ArrayList<Carte> list = new ArrayList<>();

        try {
            Statement statement = DAOMySQLSettings.getStatement();

            String sql = "select * from carta";

            printQuery(sql);

            list = getQueryResult(statement, sql);

            DAOMySQLSettings.closeStatement(statement);
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void deleteAll() throws DAOException {

        String query = "delete from carta";

        printQuery(query);

        executeUpdate(query);
    }

    @Override
    public void initialize() throws DAOException {

        deleteAll();

        for (int i = 1; i<=10; i++){
            Integer id_carta = i;
            Integer id_cliente = i;
            Float massimaleMensile = i*1000f;
            Float massimaleRimanente = i*100f;

            insert(new Carte(id_carta, id_cliente, massimaleMensile, massimaleRimanente));

        }

        // also shows the tuples
        ArrayList<Carte> list =  selectAll();
        list.forEach(System.out::println);
    }

    // NB. called by update()... could be modified
    private void verifyObject(Carte a) throws DAOException {
        if (       a == null
                || a.getIdCarta()             == null
                || a.getCliente_idCliente()   == null
                || a.getMassimaleMensile()    == null
                || a.getMassimaleRimanente()  == null )
    {
            throw new DAOException("verifyObject: any field can be null");
        }
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

    @Override
    public void insert(Carte a) throws DAOException {

        verifyObject(a);

        //NB. qui la chiave e' inserita manualmente...
        String query = "INSERT INTO carta (idCarta, massimaleMensile, massimaleRimanente, Cliente_idCliente) VALUES  ("
                + a.getIdCarta() + ", "
                + a.getMassimaleMensile() + ", "
                + a.getMassimaleRimanente() + ", "
                + a.getCliente_idCliente() + ")";

        printQuery(query);

        executeUpdate(query);
    }

    @Override
    // update key-based
    public void update(Carte a) throws DAOException {

        verifyObject(a);

        String query = "UPDATE carta SET massimaleMensile = "
                + a.getMassimaleMensile() + ", massimaleRimanente = "
                + a.getMassimaleRimanente() + ",  Cliente_idCliente = "
                + a.getCliente_idCliente();

        query = query + " WHERE idCarta = " + a.getIdCarta() + ";";

        printQuery(query);

        executeUpdate(query);

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
            list.add(new Carte(
                    rs.getInt("idCarta"),
                    rs.getInt("Cliente_idCliente"),
                    rs.getFloat("massimaleMensile"),
                    rs.getFloat("massimaleRimanente"))
            );
        }

        return list;
    }
}
