package it.unicas.supermarket.model.dao.mysql;

import it.unicas.supermarket.model.Carte;
import it.unicas.supermarket.model.Clienti;
import it.unicas.supermarket.model.Ordini;
import it.unicas.supermarket.model.dao.DAO;
import it.unicas.supermarket.model.dao.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static it.unicas.supermarket.model.dao.mysql.ClientiDAOMySQL.getIdClienteFromCode;

public class OrdiniDAOMySQL implements DAO<Ordini> {

    private static DAO<Ordini> dao = null;
    private static Logger logger = null;

    private OrdiniDAOMySQL() {}

    public static DAO<Ordini> getInstance(){
        if (dao == null){
            dao = new OrdiniDAOMySQL();
            logger = Logger.getLogger(CarteDAOMySQL.class.getName());
        }
        return dao;
    }


    // Testing Class
    public static void main(String[] args) throws DAOException {

        boolean initialize = false;
        // boolean initialize = false;

        OrdiniDAOMySQL c = new OrdiniDAOMySQL();

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
            c.insert(new Ordini(idCliente, "00-00-0000 00:00", "ordine___0",0f, null));
            c.insert(new Ordini(idCliente, "11-11-1111 11:11", "ordine___1",1f, null));
            c.insert(new Ordini(idCliente, "22-22-2222 22:22", "ordine___2",2f, null));


            // 2 - testing select all
            List<Ordini> list = c.selectAll();
            list.forEach(System.out::println);


            // 3 - testing delete
            // 3.1 delete all
            c.deleteAll();
            list = c.selectAll();
            list.forEach(System.out::println);


            // 3.2 delete
            idCliente = getIdClienteFromCode("codice_1");
            c.insert(new Ordini(idCliente, "11-11-1111 11:11", "ordine___1",1f, null));

            idCliente = getIdClienteFromCode("codice_2");
            c.insert(new Ordini(idCliente, "22-22-2222 22:22", "ordine___2",2f, null));


            idCliente = getIdClienteFromCode("codice_1");
            Ordini toDelete = new Ordini(idCliente, "33-33-3333 33:33", "ordine___3",3f, null);

            c.delete(toDelete);


            // show remaining tuples
            list = c.selectAll();
            list.forEach(System.out::println);


            // 3.3 update
            idCliente = getIdClienteFromCode("codice_4");
            Ordini toUpdate = new Ordini(idCliente, "44-44-4444 44:44", "ordine___4",4f, null);

            c.insert(toUpdate);

            list = c.selectAll();
            list.forEach(System.out::println);

            Ordini updated = new Ordini(idCliente, "44-44-4444 44:44", "ordine___4",40000000f, null);

            c.update(updated);

            // shows the updated tuple (all db to catch errors)
            list = c.selectAll();
            list.forEach(System.out::println);

        } // end of testing
    }


    @Override
    public List<Ordini> select(Ordini a) throws DAOException {

        ArrayList<Ordini> lista;

        if ( a==null )
            throw new DAOException("In select: called select with a 'null' instance of Ordini");

        try{
            if (a.getIdOrdine()!= null)
                throw new DAOException("In select: idOrdine must be null in select");

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "select * from ordini where (codiceOrdine ='" + a.getCodiceOrdine() + "')";

            printQuery(sql);

            lista = getQueryResult(st, sql);

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In select(): " + sq.getMessage());
        }

        return lista;
    }


    @Override
    public void delete(Ordini a) throws DAOException {

        if ( a == null ) {
            throw new DAOException("In delete: can't delete a null instance");
        }
        if (a.getCodiceOrdine() == null){
            throw new DAOException("In delete: codiceOrdine can't be null");
        }

        String query = "DELETE FROM ordini WHERE codiceOrdine='" + a.getCodiceOrdine() + "';";

        printQuery(query);

        executeUpdate(query);

    }


    public ArrayList<Ordini> selectAll() {

        ArrayList<Ordini> list = new ArrayList<>();

        try{
            Statement statement = DAOMySQLSettings.getStatement();

            String sql = "select * from ordini";

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

        String query = "delete from ordini";

        printQuery(query);

        executeUpdate(query);
    }


    @Override
    public void initialize() throws DAOException {

        for (int i = 0; i<10; i++){

            String codiceCliente_i = "codice_" + i;
            Integer id_cliente = getIdClienteFromCode(codiceCliente_i);

            String data = ""+i+i+"-"+i+i+"-"+i+i+i+i+" "+i+i+":"+i+i;

            String codiceOrdine_i = "codice___" + i;

            Float importoTotale = i*1000f;

            String pin = "pin_" + i;

            insert(new Ordini(id_cliente, data, codiceOrdine_i, importoTotale, null));

        }

        // also shows the tuples
        ArrayList<Ordini> list =  selectAll();
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
    public void insert(Ordini a) throws DAOException {

        if ( a == null)
            throw new DAOException("Cannot insert a null instance of ordini");

        //NB. idCarte = NULL (autoincrement in MySql)
        String query = "INSERT INTO ordini (idOrdine, idCliente, data, codiceOrdine, importoTotale) VALUES (NULL, '" +
                a.getIdCliente() + "', '" +
                a.getData() + "', '" +
                a.getCodiceOrdine() + "', " +
                a.getImportoTotale() + ")";

        printQuery(query);

        executeUpdate(query);
    }


    @Override
    // ACTUALLY USELESS... updates data and import of the order
    public void update(Ordini a) throws DAOException {

        if ( a == null)
            throw new DAOException("cannot update with a null instance of ordini");

        String query = "UPDATE ordini SET data = '"
                + a.getData() + "', importoTotale = "
                + a.getImportoTotale();

        query = query + " WHERE codiceOrdine = '" + a.getCodiceOrdine() + "';";

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


    private ArrayList<Ordini> getQueryResult(Statement statement, String query) throws SQLException {

        ArrayList<Ordini> list = new ArrayList<>();

        ResultSet rs = statement.executeQuery(query);

        while(rs.next()){
            try {
                list.add(new Ordini(
                        rs.getInt("idCliente"),
                        rs.getString("data"),
                        rs.getString("codiceOrdine"),
                        rs.getFloat("importoTotale"),
                        rs.getInt("idOrdine"))
                );
            } catch (DAOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }


    public static Integer getIdOrdineFromCode(String codiceOrdine) throws DAOException {

        List<Ordini> ordiniList = OrdiniDAOMySQL.getInstance().select(new Ordini(codiceOrdine));

        return ordiniList.get(0).getIdOrdine();
    }

}
