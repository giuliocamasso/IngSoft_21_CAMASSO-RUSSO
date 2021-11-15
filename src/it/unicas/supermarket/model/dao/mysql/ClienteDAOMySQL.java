package it.unicas.supermarket.model.dao.mysql;

import it.unicas.supermarket.model.Cliente;
import it.unicas.supermarket.model.dao.DAO;
import it.unicas.supermarket.model.dao.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class ClienteDAOMySQL implements DAO<Cliente> {

    private static DAO<Cliente> dao = null;
    private static Logger logger = null;

    private ClienteDAOMySQL() {}

    public static DAO<Cliente> getInstance(){
        if (dao == null){
            dao = new ClienteDAOMySQL();
            logger = Logger.getLogger(ClienteDAOMySQL.class.getName());
        }
        return dao;
    }

    // Testing Class
    public static void main(String[] args) throws DAOException {

        boolean initialize = true;

        ClienteDAOMySQL c = new ClienteDAOMySQL();

        if (initialize){
            // c.deleteAll();
            // c.initialize();
        }

        // testing
        else {

            c.deleteAll();

            // 1 - testing Insert
            c.insert(new Cliente("Giulio", "Camasso", "-----------", 1234, 1));

            // 2 - testing select all
            // NB. select(null) is deprecated, a new method(selectAll()) was created
            List<Cliente> list = c.selectAll();

            list.forEach(System.out::println);

            // 3 - testing delete
            // 3.1 delete all
            c.deleteAll();
            list = c.selectAll();

            list.forEach(System.out::println);

            // 3.2 delete
            // add 2 tuples
            c.insert(new Cliente("Nome1", "Cognome1", "telefono1", 111, 1));
            c.insert(new Cliente("Nome2", "Cognome2", "telefono2", 222, 2));

            // could be improved... actually only removes tuple by idCliente
            Cliente toDelete = new Cliente(null, null, null, null, 2);
            c.delete(toDelete);

            // show remaining tuples
            list = c.selectAll();
            list.forEach(System.out::println);

            // 3.3 update
            // insert a new tuple to update
            Cliente toUpdate = new Cliente("Nome3", "Cognome3", "telefono3", 333, 3);
            c.insert(toUpdate);
            // print the new tuple
            list = c.select(toUpdate);
            list.forEach(System.out::println);
            // call the update()
            Cliente updated = new Cliente("NEW_Nome3", "NEW_Cognome3", "NEW_telefono3", 999, 3);
            c.update(updated);

            // shows the updated tuple (all db to catch errors)
            list = c.selectAll();
            list.forEach(System.out::println);
        } // end of testing
    }

    @Override
    public List<Cliente> select(Cliente a) throws DAOException {

        ArrayList<Cliente> lista;

        if (a==null)
            throw new DAOException("In select: called select with a 'null' instance of Cliente");

        try{

            if (    a.getNome()     == null || a.getCognome()      == null ||
                    a.getTelefono() == null || a.getPuntiFedelta() == null ) {
                throw new DAOException("In select: any field can be null");
            }

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "select * from cliente where (cognome like '";
            sql += a.getCognome() + "%' and nome like '" + a.getNome();
            sql += "%' and telefono like '" + a.getTelefono() + "%' "
                    + "and punti_fedelta=" + a.getPuntiFedelta() + ")";

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
    public void delete(Cliente a) throws DAOException {

        if (a == null) {
            throw new DAOException("In delete: can't delete a null instance");
        }
        if (a.getIdCliente() == null){
            throw new DAOException("In delete: idColleghi cannot be null");
        }

        String query = "DELETE FROM cliente WHERE idCliente='" + a.getIdCliente() + "';";

        printQuery(query);

        executeUpdate(query);

    }

    public ArrayList<Cliente> selectAll() {

        ArrayList<Cliente> list = new ArrayList<>();

        try {
            Statement statement = DAOMySQLSettings.getStatement();

            String sql = "select * from cliente";

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

        String query = "delete from cliente";

        printQuery(query);

        executeUpdate(query);
    }

    @Override
    public void initialize() throws DAOException {

        deleteAll();

        for (int i = 1; i<=10; i++){
            String nome_i = "nome_" + i;
            String cognome_i = "cognome_" + i;
            String telefono_i = "telefono_" + i;
            Integer punti_fedelta = i;
            Integer id_cliente = i;

            insert(new Cliente(nome_i, cognome_i, telefono_i, punti_fedelta, id_cliente));

        }

        // also shows the tuples
        ArrayList<Cliente> list =  selectAll();
        list.forEach(System.out::println);
    }

    // NB. called by update()... could be modified
    private void verifyObject(Cliente a) throws DAOException {
        if (       a == null
                || a.getCognome()       == null
                || a.getNome()          == null
                || a.getTelefono()      == null
                || a.getPuntiFedelta()  == null )
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
    public void insert(Cliente a) throws DAOException {

        verifyObject(a);
        /*
        // NB. qui l'idCliente e' autoincrement...
        String query = "INSERT INTO cliente (nome, cognome, telefono, punti_fedelta, idCliente) VALUES  ('" +
                a.getNome() + "', '" + a.getCognome() + "', '" +
                a.getTelefono() + "', " + a.getPuntiFedelta() + ", NULL)";
        */

        //NB. qui la chiave e' inserita manualmente...
        String query = "INSERT INTO cliente (nome, cognome, telefono, punti_fedelta, idCliente) VALUES  ('"
                + a.getNome() + "', '"
                + a.getCognome() + "', '"
                + a.getTelefono() + "', "
                + a.getPuntiFedelta() + ", "
                + a.getIdCliente() + ")";

        printQuery(query);

        executeUpdate(query);
    }

    @Override
    // update key-based
    public void update(Cliente a) throws DAOException {

        verifyObject(a);

        String query = "UPDATE cliente SET nome = '"
                + a.getNome() + "', cognome = '"
                + a.getCognome() + "',  telefono = '"
                + a.getTelefono() + "',  punti_fedelta = "
                + a.getPuntiFedelta();

        query = query + " WHERE idCliente = " + a.getIdCliente() + ";";

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

    private ArrayList<Cliente> getQueryResult(Statement statement, String query) throws SQLException {

        ArrayList<Cliente> list = new ArrayList<>();

        ResultSet rs = statement.executeQuery(query);

        while(rs.next()){
            list.add(new Cliente(
                    rs.getString("nome"),
                    rs.getString("cognome"),
                    rs.getString("telefono"),
                    rs.getInt("punti_fedelta"),
                    rs.getInt("idCliente") )
                    );
        }

        return list;
    }
}
