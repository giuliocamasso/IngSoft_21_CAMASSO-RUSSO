package it.unicas.supermarket.model.dao.mysql;

import it.unicas.supermarket.model.Clienti;
import it.unicas.supermarket.model.dao.DAO;
import it.unicas.supermarket.model.dao.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static it.unicas.supermarket.model.Clienti.*;


public class ClientiDAOMySQL implements DAO<Clienti> {

    private static DAO<Clienti> dao = null;
    private static Logger logger = null;

    private ClientiDAOMySQL() {}

    public static DAO<Clienti> getInstance(){
        if (dao == null){
            dao = new ClientiDAOMySQL();
            logger = Logger.getLogger(ClientiDAOMySQL.class.getName());
        }
        return dao;
    }

    // Testing Class
    public static void main(String[] args) throws DAOException {

        boolean initialize = false;

        ClientiDAOMySQL c = new ClientiDAOMySQL();

        if (initialize){
            c.deleteAll();
            //c.initialize();
        }

        // testing
        else {

            c.deleteAll();

            // 1 - testing Insert
            c.insert(new Clienti("Giulio", "Camasso", ghostPhone('a'), 1234, ghostIBAN('a'), ghostClientCode('a'), null));

            // 2 - testing select all
            List<Clienti> list = c.selectAll();

            list.forEach(System.out::println);

            // 3 - testing delete
            // 3.1 delete all
            c.deleteAll();
            list = c.selectAll();

            list.forEach(System.out::println);

            // 3.2 delete
            // add 2 tuples
            c.insert(new Clienti("Nome1", "Cognome1", ghostPhone('1'), 111, ghostIBAN('1'), ghostClientCode('1'), null));
            c.insert(new Clienti("Nome2", "Cognome2", ghostPhone('2'), 222, ghostIBAN('2'), ghostClientCode('2'), null));
            c.insert(new Clienti("Nome3", "Cognome3", ghostPhone('3'), 333, ghostIBAN('3'), ghostClientCode('3'), null));

            // could be improved... actually only removes tuple by codiceCliente
            Clienti toDelete = new Clienti("", "", ghostPhone(' '), 0, ghostIBAN(' '), ghostClientCode('1'), null);
            c.delete(toDelete);

            // show remaining tuples
            list = c.selectAll();
            list.forEach(System.out::println);

            // 3.3 update
            // insert a new tuple to update
            Clienti toUpdate = new Clienti("", "", ghostPhone('4'), 4, ghostIBAN('4'), ghostClientCode('4'), null);
            c.insert(toUpdate);
            // print the new tuple
            list = c.select(toUpdate);
            list.forEach(System.out::println);
            // call the update()
            Clienti updated = new Clienti("NEW", "NEW", ghostPhone('N'), 55119451, ghostIBAN('N'), ghostClientCode('4'), null);
            c.update(updated);

            // shows the updated tuple (all db to catch errors)
            list = c.selectAll();
            list.forEach(System.out::println);
        } // end of testing
    }

    @Override
    public List<Clienti> select(Clienti a) throws DAOException {

        ArrayList<Clienti> lista;

        if (a==null)
            throw new DAOException("In select: called select with a 'null' instance of Clienti");

        try{
            if (a.getIdCliente()!= null)
                throw new DAOException("In select: idCliente must be null");
            else if (a.getNome()     == null || a.getCognome()       == null ||
                     a.getTelefono() == null || a.getPuntiFedelta()  == null ||
                     a.getIban()     == null || a.getCodiceCliente() == null ) {
                throw new DAOException("In select: any field can be null");
            }

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "select * from clienti where (cognome like '";
            sql += a.getCognome() + "%' and nome like '" + a.getNome()
                +   "%' and telefono like '" + a.getTelefono() + "%' "
                +   "and iban like '" + a.getIban() + "%' "
                +   "and codiceCliente like '" + a.getCodiceCliente() + "%')";

            printQuery(sql);

            lista = getQueryResult(st, sql);

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In select(): " + sq.getMessage());
        }

        return lista;
    }

    @Override
    // delete codiceCliente-based
    public void delete(Clienti a) throws DAOException {

        if (a == null) {
            throw new DAOException("In delete: can't delete a null instance");
        }
        if (a.getCodiceCliente() == null){
            throw new DAOException("In delete: codiceCliente cannot be null");
        }
        if (a.getIdCliente() != null) {
            throw new DAOException("In delete: idCliente must be null");
        }

        String query = "DELETE FROM clienti WHERE codiceCliente='" + a.getCodiceCliente() + "';";

        printQuery(query);

        executeUpdate(query);

    }

    public ArrayList<Clienti> selectAll() {

        ArrayList<Clienti> list = new ArrayList<>();

        try {
            Statement statement = DAOMySQLSettings.getStatement();

            String sql = "select * from clienti";

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

        String query = "delete from clienti";

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
            Integer puntiFedelta = i;
            String iban_i = "IBAN____" + i;
            String codiceCliente_i = "codice_" + i;

            insert(new Clienti(nome_i, cognome_i, telefono_i, puntiFedelta, iban_i, codiceCliente_i, null));

        }

        // also shows the tuples
        ArrayList<Clienti> list =  selectAll();
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
    public void insert(Clienti a) throws DAOException {

        if (a == null)
            throw new DAOException("Can't insert a null instance of clienti");

        //NB. idCliente = NULL (autoincrement in MySql)
        String query = "INSERT INTO clienti (idCliente, nome, cognome, telefono, puntiFedelta, iban, codiceCliente) VALUES (NULL, '" +
                a.getNome() + "', '" +
                a.getCognome() + "', '" +
                a.getTelefono() + "', " +
                a.getPuntiFedelta() + ", '" +
                a.getIban() + "', '" +
                a.getCodiceCliente() + "')";

        printQuery(query);

        executeUpdate(query);
    }

    @Override
    // update key-based
    public void update(Clienti a) throws DAOException {

        if (a == null)
            throw new DAOException("Can't update with a null instance of clienti");

        String query = "UPDATE clienti SET nome = '"
                + a.getNome() + "', cognome = '"
                + a.getCognome() + "',  telefono = '"
                + a.getTelefono() + "',  puntiFedelta = "
                + a.getPuntiFedelta() + ", iban = '"
                + a.getIban() + "', codiceCliente = '"
                + a.getCodiceCliente() + "'";

        query = query + " WHERE codiceCliente = '" + a.getCodiceCliente() + "';";

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

    private ArrayList<Clienti> getQueryResult(Statement statement, String query) throws SQLException {

        ArrayList<Clienti> list = new ArrayList<>();

        ResultSet rs = statement.executeQuery(query);

        while(rs.next()){
            try {
                list.add(new Clienti(
                                rs.getString("nome"),
                                rs.getString("cognome"),
                                rs.getString("telefono"),
                                rs.getInt("puntiFedelta"),
                                rs.getString("iban"),
                                rs.getString("codiceCliente"),
                                rs.getInt("idCliente")
                                                    )
                        );
            } catch (DAOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}
