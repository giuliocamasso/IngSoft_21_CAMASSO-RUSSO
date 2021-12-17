package it.unicas.supermarket.model.dao.mysql;
import it.unicas.supermarket.model.Composizioni;
import it.unicas.supermarket.model.dao.DAO;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.Util;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

public class ComposizioniDAOMySQL implements DAO<Composizioni> {

    private static DAO<Composizioni> dao = null;
    private static Logger logger = null;

    private ComposizioniDAOMySQL() {
    }

    public static DAO<Composizioni> getInstance() {
        if (dao == null) {
            dao = new ComposizioniDAOMySQL();
            logger = Logger.getLogger(OrdiniDAOMySQL.class.getName());
        }
        return dao;
    }

    @Override
    public List<Composizioni> select(Composizioni articoloOrdine) throws DAOException {
        return null;
    }

    @Override
    public void update(Composizioni articoloOrdine) throws DAOException {

    }

    @Override
    public void insert(Composizioni articoloInOrdine) throws DAOException {
        if (articoloInOrdine == null)
            throw new DAOException("null instance of 'composizioni'...");
        // INSERT INTO Composizioni(idArticolo, idOrdini, prezzo, quantita) VALUES([...]);
        String query = "INSERT INTO Composizioni (idArticolo, idOrdine, prezzo, quantita) VALUES (" +
                articoloInOrdine.getIdArticolo() + ", " +
                articoloInOrdine.getIdOrdine() + ", " +
                articoloInOrdine.getPrezzo() + ", " +
                articoloInOrdine.getQuantita() + ")";

        if (Util.isQueryPrintingEnabled())
            printQuery(query);

        executeUpdate(query);
    }

    @Override
    public void delete(Composizioni articoloOrdine) throws DAOException {

    }

    @Override
    public List<Composizioni> selectAll() throws DAOException {
        return null;
    }

    @Override
    public void deleteAll() throws DAOException {
        String query = "delete from Composizioni";

        if (Util.isQueryPrintingEnabled())
            printQuery(query);

        executeUpdate(query);
    }

    @Override
    public void initialize() throws DAOException {

    }

    private void printQuery(String query) {
        try {
            logger.info("SQL: " + query);
        } catch (NullPointerException nullPointerException) {
            System.out.println("SQL: " + query);
        }
    }

    private void executeUpdate(String query) throws DAOException {
        try {
            Statement st = DAOMySQLSettings.getStatement();
            int n = st.executeUpdate(query);

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException("executeUpdate(): DAOException " + e.getMessage());
        }
    }
}
