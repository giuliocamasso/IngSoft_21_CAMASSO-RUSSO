package it.unicas.supermarket.model.dao.mysql;

import it.unicas.supermarket.model.Carte;
import it.unicas.supermarket.model.Clienti;
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
            c.initialize();
        }

        // testing
        else {

            c.deleteAll();

            // 1 - testing Insert
            List<Clienti> clientiList = ClientiDAOMySQL.getInstance().selectAll();
            clientiList.forEach(System.out::println);

            clientiList = ClientiDAOMySQL.getInstance().select(new Clienti("","","codice_1"));
            //clientiList.forEach(System.out::println);
            System.out.println(clientiList.get(0));
            Integer idCliente = clientiList.get(0).getIdCliente();

            // OK ORA FUNZIONA: continuare da qua_______________________________________________________________________

            c.insert(new Carte(100f, 10f,idCliente,"11111", "1111-1111-1111-1111", null));
            // c.insert(new Carte(136,"2222-2222-2222-2222"));

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
            c.insert(new Carte(100f, 10f,137,"11111", "1111-1111-1111-1111", null));
            c.insert(new Carte(138, "2222-2222-2222-2222"));
            c.insert(new Carte(139, "3333-3333-3333-3333"));

            // could be improved... actually only removes tuple by idCarta
            Carte toDelete = new Carte(null, null,2,null, "2222-2222-2222-2222", null);
            Carte toDelete2 = new Carte(1, "1111-1111-1111-1111");

            c.delete(toDelete);
            c.delete(toDelete2);

            // show remaining tuples
            list = c.selectAll();
            list.forEach(System.out::println);

            // 3.3 update
            // insert a new tuple to update
            Carte toUpdate = new Carte(300f, 30f,3,"33333", "3333-3333-3333-3333", null);
            Carte toUpdate2 = new Carte(4, "4444-4444-4444-4444");
            c.insert(toUpdate);
            c.insert(toUpdate2);

            // print the new tuple
            list = c.selectAll();
            list.forEach(System.out::println);
            // call the update()

            // Carte updated = new Carte(3, 30, 3333f, 333f);
            Carte updated = new Carte(600f, 60f,3,null, "3333-3333-3333-3333", null);
            Carte updated2 = new Carte(10f, 1f, 4, "PIN-4", "4444-4444-4444-4444", null);
            c.update(updated);
            c.update(updated2);

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
            if (a.getIdCarta()!= null)
                throw new DAOException("In select: idCarta must be null in select");

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "select * from carte where (codiceCarta ='" + a.getCodiceCarta() + "')";

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
        if (a.getCodiceCarta() == null){
            throw new DAOException("In delete: codiceCarta can't be null");
        }

        String query = "DELETE FROM carte WHERE codiceCarta='" + a.getCodiceCarta() + "';";

        printQuery(query);

        executeUpdate(query);

    }


    public ArrayList<Carte> selectAll() {

        ArrayList<Carte> list = new ArrayList<>();

        try {
            Statement statement = DAOMySQLSettings.getStatement();

            String sql = "select * from carte";

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

        String query = "delete from carte";

        printQuery(query);

        executeUpdate(query);
    }


    @Override
    public void initialize() throws DAOException {

        deleteAll();

        for (int i = 1; i<=10; i++){
            Integer id_carta = i;
            Float massimaleMensile = i*1000f;
            Float massimaleRimanente = i*100f;
            Integer id_cliente = i;
            String pin = "pin_" + i;
            String seed4_i = ""+ i + i + i + i;

            insert(new Carte( massimaleMensile, massimaleRimanente, id_cliente, pin, Carte.ghostCardCode(seed4_i), id_carta));

        }

        // also shows the tuples
        ArrayList<Carte> list =  selectAll();
        list.forEach(System.out::println);
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

        if ( a == null)
            throw new DAOException("Cannot insert a null instance of carte");

        //NB. idCarte = NULL (autoincrement in MySql)
        String query = "INSERT INTO carte (idCarta, massimaleMensile, massimaleRimanente, idCliente, pin, codiceCarta) VALUES (NULL, '" +
                a.getMassimaleMensile() + "', '" +
                a.getMassimaleRimanente() + "', " +
                a.getIdCliente() + ", '" +
                a.getPin() + "', '" +
                a.getCodiceCarta() + "')";

        printQuery(query);

        executeUpdate(query);
    }


    @Override
    // update key-based
    public void update(Carte a) throws DAOException {

        if ( a == null)
            throw new DAOException("cannot update with a null instance of carte");

        String query = "UPDATE carta SET massimaleMensile = "
                + a.getMassimaleMensile() + ", massimaleRimanente = "
                + a.getMassimaleRimanente();

        query = query + " WHERE codiceCarta = " + a.getCodiceCarta() + ";";

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

    public Integer getIdClienteFromCode(String codiceCliente){
        try {
            Clienti target = new Clienti("", "", codiceCliente);
            List<Clienti> cliente = ClientiDAOMySQL.getInstance().select(target);
            System.out.println(cliente.get(0));

        } catch (DAOException e) {
            e.printStackTrace();
        }

        return 1;
    }
}
