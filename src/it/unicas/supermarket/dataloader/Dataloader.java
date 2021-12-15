package it.unicas.supermarket.dataloader;
import it.unicas.supermarket.model.Articoli;
import it.unicas.supermarket.model.Carte;
import it.unicas.supermarket.model.Clienti;
import it.unicas.supermarket.model.Ordini;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.mysql.ArticoliDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.CarteDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.ClientiDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.OrdiniDAOMySQL;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static it.unicas.supermarket.model.dao.mysql.ClientiDAOMySQL.getIdClienteFromCode;

public class Dataloader {

    // NB. The consistence of read data is checked by class constructors
    public static void loadClienti(String filename, String delim) throws IOException, DAOException {

        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String line = reader.readLine();

        while (line != null) {

            // tuple columns are separated by the passed delimiter
            String[] values = line.split(delim);

            String nome = values[0];
            String cognome = values[1];
            String telefono = values[2];
            Integer puntiFedelta = Integer.valueOf(values[3]);
            String iban = values[4];
            String codiceCliente = values[5];

            ClientiDAOMySQL.getInstance().insert(new Clienti(nome, cognome, telefono, puntiFedelta, iban, codiceCliente, null));

            // reads next line
            line = reader.readLine();
        }

        reader.close();
    }


    public static void loadArticoli(String filename, String delim) throws IOException, DAOException {

        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String line = reader.readLine();

        while (line != null) {

            // tuple columns are separated by the passed delimiter
            String[] values = line.split(delim);

            String nome = values[0];
            Float prezzo = Float.valueOf(values[1]);
            Integer scorteMagazzino = Integer.valueOf(values[2]);
            String barcode = values[3];

            String produttore = values[4];
            String descrizioneProdotto = values[5];
            String descrizioneQuantita = values[6];
            String reparto = values[7];

            ArticoliDAOMySQL.getInstance().insert(new Articoli(nome, prezzo, scorteMagazzino, barcode, reparto, produttore, descrizioneProdotto, descrizioneQuantita, null));

            // reads next line
            line = reader.readLine();
        }

        reader.close();
    }


    public static void loadCarte(String filename, String delim) throws IOException, DAOException {

        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String line = reader.readLine();

        while (line != null) {

            // tuple columns are separated by the passed delimiter
            String[] values = line.split(delim);

            String codiceCarta = values[0];
            String codiceCliente = values[1];

            // NB. idCliente is required (foreign key for table 'Carte')
            Integer idCliente = getIdClienteFromCode(codiceCliente);
            Float massimaleMensile = Float.valueOf(values[2]);
            Float massimaleRimanente = Float.valueOf(values[3]);
            String pin = values[4];

            CarteDAOMySQL.getInstance().insert(new Carte( massimaleMensile, massimaleRimanente, idCliente, pin, codiceCarta, null));

            // reads next line
            line = reader.readLine();
        }

        reader.close();
    }


    public static void loadOrdini(String filename, String delim) throws IOException, DAOException {

        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String line = reader.readLine();

        while (line != null) {

            // tuple columns are separated by the passed delimiter
            String[] values = line.split(delim);

            String codiceOrdine = values[0];

            String codiceCliente = values[1];
            // NB. idCliente is required
            Integer idCliente = getIdClienteFromCode(codiceCliente);

            String data = values[2];

            Float importoTotale = Float.valueOf(values[3]);

            OrdiniDAOMySQL.getInstance().insert(new Ordini(idCliente, data, codiceOrdine, importoTotale, null));

            // reads next line
            line = reader.readLine();
        }

        reader.close();
    }


    public static void main(String[] args){

        try {
            CarteDAOMySQL.getInstance().deleteAll();
            OrdiniDAOMySQL.getInstance().deleteAll();
            ClientiDAOMySQL.getInstance().deleteAll();
            ArticoliDAOMySQL.getInstance().deleteAll();

            loadClienti("src/it/unicas/supermarket/dataloader/Clienti.txt", " - ");
            loadArticoli("src/it/unicas/supermarket/dataloader/Articoli.txt", " - ");
            loadCarte("src/it/unicas/supermarket/dataloader/Carte.txt", " - ");
            loadOrdini("src/it/unicas/supermarket/dataloader/Ordini.txt", " - ");

        } catch (IOException | DAOException e) {
            e.printStackTrace();
        }

    }
}
