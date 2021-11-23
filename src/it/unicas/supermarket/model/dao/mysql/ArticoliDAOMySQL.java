package it.unicas.supermarket.model.dao.mysql;

import it.unicas.supermarket.model.Articoli;
import it.unicas.supermarket.model.Clienti;
import it.unicas.supermarket.model.dao.DAO;
import it.unicas.supermarket.model.dao.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class ArticoliDAOMySQL implements DAO<Articoli> {

    private static DAO<Articoli> dao = null;
    private static Logger logger = null;

    private ArticoliDAOMySQL() {}

    public static DAO<Articoli> getInstance(){
        if (dao == null){
            dao = new ArticoliDAOMySQL();
            logger = Logger.getLogger(ArticoliDAOMySQL.class.getName());
        }
        return dao;
    }

    // Testing Class
    public static void main(String[] args) throws DAOException {

        boolean initialize = false;

        ArticoliDAOMySQL c = new ArticoliDAOMySQL();

        if (initialize){
            c.deleteAll();
            c.initialize();
        }

        // testing
        else {

            c.deleteAll();

            // 1 - testing Insert
            c.insert(new Articoli("nome_0", 0f, 0, "barcode_____0", null ));
            c.insert(new Articoli("nome_1", 100f, 100, "barcode_____1", null ));

            // 2 - testing select all
            List<Articoli> list = c.selectAll();

            list.forEach(System.out::println);

            // 3 - testing delete
            // 3.1 delete all
            c.deleteAll();
            list = c.selectAll();

            list.forEach(System.out::println);

            // 3.2 delete
            // add 2 tuples
            c.insert(new Articoli("nome_0", 0f, 0, "barcode_____0", null ));
            c.insert(new Articoli("nome_1", 100f, 100, "barcode_____1", null ));

            Articoli toDelete = new Articoli("", "barcode_____0");
            Articoli toDelete2 = new Articoli("nome_1", 100f, 100, "barcode_____1", null );

            c.delete(toDelete);
            c.delete(toDelete2);

            // show remaining tuples
            list = c.selectAll();
            list.forEach(System.out::println);

            // 3.3 update
            // insert a new tuple to update
            Articoli toUpdate = new Articoli("nome_2", 200.20f, 200, "barcode_____2", null );
            Articoli toUpdate2 = new Articoli("nome_3", 300.30f, 300, "barcode_____3", null );

            c.insert(toUpdate);
            c.insert(toUpdate2);

            System.out.println("SEARCH BY NAME:");
            // search by name
            list = c.select(new Articoli("nome_2", ""));
            list.forEach(System.out::println);

            System.out.println("SEARCH BY BARCODE:");
            // search by barcode
            list = c.select(new Articoli("","barcode_____3"));
            list.forEach(System.out::println);

            // call the update()
            Articoli updated = new Articoli("NEW_nome_2", 20000.20f, 20000, "barcode_____2", null );

            c.update(updated);

            // shows the updated tuple (all db to catch errors)
            list = c.selectAll();
            list.forEach(System.out::println);
        } // end of testing
    }


    @Override
    public List<Articoli> select(Articoli a) throws DAOException {

        ArrayList<Articoli> lista;

        if (a==null)
            throw new DAOException("In select: called select with a 'null' instance of Articoli");

        try{
            //else if (a.getNome()     == null || a.getCognome()       == null ||
            if (a.getNome()     == null || a.getBarcode()       == null) {
                throw new DAOException("In select: fields can be null");
            }

            Statement st = DAOMySQLSettings.getStatement();
            String sql = "select * from articoli where ";

            // SEARCH BY NAME
            if(a.getBarcode().equals("             ")){
                sql += "(nome like '" + a.getNome() +"%')";
            }

            // SEARCH BY BARCODE
            else{
                sql += "(barcode = '" + a.getBarcode() + "')";
            }

            printQuery(sql);

            lista = getQueryResult(st, sql);

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In select(): " + sq.getMessage());
        }

        return lista;
    }


    @Override
    // delete barcode-based
    public void delete(Articoli a) throws DAOException {

        if (a == null) {
            throw new DAOException("In delete: can't delete a null instance of articoli");
        }

        if (a.getBarcode() == null){
            throw new DAOException("In delete: barcode cannot be null");
        }

        String query = "DELETE FROM articoli WHERE barcode='" + a.getBarcode() + "';";

        printQuery(query);

        executeUpdate(query);

    }


    public ArrayList<Articoli> selectAll() {

        ArrayList<Articoli> list = new ArrayList<>();

        try {
            Statement statement = DAOMySQLSettings.getStatement();

            String sql = "select * from articoli";

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

        String query = "delete from articoli";

        printQuery(query);

        executeUpdate(query);
    }


    @Override
    public void initialize() throws DAOException {

        for (int i = 0; i<10; i++){
            String nome_i = "nome_" + i;
            Float prezzo_i = 100f*i;
            Integer scorteMagazzino_i = 100*i;
            String barcode_i = "barcode_____" + i;

            insert(new Articoli(nome_i, prezzo_i, scorteMagazzino_i, barcode_i, null));

        }

        // also shows the tuples
        ArrayList<Articoli> list =  selectAll();
        list.forEach(System.out::println);
    }


    private void executeUpdate(String query) throws DAOException{
        try {
            Statement st = DAOMySQLSettings.getStatement();
            int n = st.executeUpdate(query);

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException("executeUpdate(): DAOException " + e.getMessage());
        }
    }


    @Override
    public void insert(Articoli a) throws DAOException {

        if (a == null)
            throw new DAOException("Can't insert a null instance of Articoli");

        //NB. idArticoli = NULL (autoincrement in MySql)
        String query = "INSERT INTO articoli (idArticolo, nome, prezzo, scorteMagazzino, barcode) VALUES (NULL, '" +
                a.getNome() + "', " +
                a.getPrezzo() + ", " +
                a.getScorteMagazzino() + ", '" +
                a.getBarcode() + "')";

        printQuery(query);

        executeUpdate(query);
    }


    @Override
    // update barcode-based
    public void update(Articoli a) throws DAOException {

        if (a == null)
            throw new DAOException("Can't update with a null instance of Articoli");

        if (a.getBarcode() == null)
            throw new DAOException("Can't update with a null barcode");

        String query = "UPDATE articoli SET nome = '"
                + a.getNome() + "', prezzo = "
                + a.getPrezzo() + ", scorteMagazzino = "
                + a.getScorteMagazzino();

        query = query + " WHERE barcode = '" + a.getBarcode() + "';";

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


    private ArrayList<Articoli> getQueryResult(Statement statement, String query) throws SQLException {

        ArrayList<Articoli> list = new ArrayList<>();

        ResultSet rs = statement.executeQuery(query);

        while(rs.next()){
            try {
                list.add(new Articoli(
                        rs.getString("nome"),
                        rs.getFloat("prezzo"),
                        rs.getInt("scorteMagazzino"),
                        rs.getString("barcode"),
                        rs.getInt("idArticolo"))
                        );
            }
            catch (DAOException e){
                e.printStackTrace();
            }
        }

        return list;
    }


    public Integer getIdArticoloFromBarcode(String barcode) throws DAOException {

        List<Articoli> articoliList = select(new Articoli("", barcode));

        return articoliList.get(0).getIdArticolo();
    }
}