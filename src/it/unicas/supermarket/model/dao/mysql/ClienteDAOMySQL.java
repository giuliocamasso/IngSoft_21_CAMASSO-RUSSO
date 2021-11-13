package it.unicas.supermarket.model.dao.mysql;
//***

import it.unicas.supermarket.model.Cliente;
import it.unicas.supermarket.model.dao.DAO;
import it.unicas.supermarket.model.dao.DAOException;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
//***

public class ClienteDAOMySQL implements DAO<Cliente> {
    private ClienteDAOMySQL(){}

    private static DAO dao = null;
    private static Logger logger = null;

    public static DAO getInstance(){
        if (dao == null){
            dao = new ClienteDAOMySQL();
            logger = Logger.getLogger(ClienteDAOMySQL.class.getName());
        }
        return dao;
    }

    public static void main(String args[]) throws DAOException {
        ClienteDAOMySQL c = new ClienteDAOMySQL();

        c.insert(new Cliente("Mario", "Molinara", "082fsd4981", 1545, null));
        /*
        c.insert(new Colleghi("Mario", "Rossi", "0824981", "molinara@uni.it", "21-10-2017", null));
        c.insert(new Colleghi("Carlo", "Ciampi", "0824982", "ciampi@uni.it", "22-02-2017", null));
        c.insert(new Colleghi("Ornella", "Vaniglia", "0824983", "vaniglia@uni.it", "23-05-2017", null));
        c.insert(new Colleghi("Cornelia", "Crudelia", "0824984", "crudelia@uni.it", "24-05-2017", null));
        c.insert(new Colleghi("Franco", "Bertolucci", "0824985", "bertolucci@uni.it", "25-10-2017", null));
        c.insert(new Colleghi("Carmine", "Labagnara", "0824986", "lagbagnara@uni.it", "26-10-2017", null));
        c.insert(new Colleghi("Mauro", "Cresta", "0824987", "cresta@uni.it", "27-12-2017", null));
        c.insert(new Colleghi("Andrea", "Coluccio", "0824988", "coluccio@uni.it", "28-01-2017", null));
        */

        List<Cliente> list = c.select(null);

        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }


        Cliente toDelete = new Cliente();
        toDelete.setNome("");
        toDelete.setCognome("");
        toDelete.setTelefono("");
        toDelete.setpunti_fedelta(-1);
        toDelete.setIdCliente(56);

        c.delete(toDelete);

        list = c.select(null);

        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }

    }

    @Override
    public List<Cliente> select(Cliente a) throws DAOException {

        if (a == null){
            a = new Cliente("", "", "", -1, null); // Cerca tutti gli elementi
        }

        ArrayList<Cliente> lista = new ArrayList<>();
        try{

            if ( a == null
                    || a.getCognome() == null
                    || a.getNome() == null
                    || a.getTelefono() == null
                    || a.getpunti_fedelta() == null){
                throw new DAOException("In select: any field can be null");
            }

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "select * from cliente where cognome like '";
            sql += a.getCognome() + "%' and nome like '" + a.getNome();
            sql += "%' and telefono like '" + a.getTelefono() + "%'";

            if (a.getpunti_fedelta() != -1){
                sql += "%' and compleanno like '" + a.getpunti_fedelta() + "%'";
            }

            try{
                logger.info("SQL: " + sql);
            }

            catch(NullPointerException nullPointerException){
                System.out.println("SQL: " + sql);
            }

            ResultSet rs = st.executeQuery(sql);

            while(rs.next()){
                lista.add(new Cliente(rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("telefono"),
                        rs.getInt("punti_fedelta"),
                        rs.getInt("idCliente")));
            }
            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In select(): " + sq.getMessage());
        }
        return lista;
    }

    @Override
    public void delete(Cliente a) throws DAOException {
        if (a == null || a.getIdCliente() == null){
            throw new DAOException("In delete: idColleghi cannot be null");
        }
        String query = "DELETE FROM cliente WHERE idCliente='" + a.getIdCliente() + "';";
        try{
            logger.info("SQL: " + query);
        }
        catch(NullPointerException nullPointerException){
            System.out.println("SQL: " + query);
        }

        executeUpdate(query);

    }

    private void verifyObject(Cliente a) throws DAOException {
        if (a == null || a.getCognome() == null
                || a.getNome() == null
                || a.getTelefono() == null
                || a.getpunti_fedelta() == -1)
    {
            throw new DAOException("In select: any field can be null");
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

        String query = "INSERT INTO cliente (nome, cognome, telefono, punti_fedelta, idcliente) VALUES  ('" +
                a.getNome() + "', '" + a.getCognome() + "', '" +
                a.getTelefono() + "', " + a.getpunti_fedelta() + ", NULL)";

        try{
            logger.info("SQL: " + query);
        }
        catch(NullPointerException nullPointerException){
            System.out.println("SQL: " + query);
        }

        executeUpdate(query);
    }





    @Override
    public void update(Cliente a) throws DAOException {

        verifyObject(a);

        String query = "UPDATE colleghi SET nome = '" + a.getNome() + "', cognome = '" + a.getCognome() + "',  telefono = '" + a.getTelefono() + "',  punti_fedelta = '" + a.getpunti_fedelta() ;
        query = query + " WHERE idcolleghi = " + a.getIdCliente() + ";";
        logger.info("SQL: " + query);

        executeUpdate(query);

    }
}
