package it.unicas.supermarket.dataloader;
import it.unicas.supermarket.Util;
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

/**
 * La classe e' usata per inizializzare il database con i dati letti dai file .txt
 * NB. Ordini e Composizioni non vengono inizializzati in questo modo poiche' inutile, vengono inseriti direttamente a runtime
 */
public class Dataloader {

    // NB. la consistenza dei dati viene gestita dai costruttori delle singole classi

    /**
     * La funzione che inserisce nel db i clienti
     * @param filename Il file che contiene i dati dei clienti
     * @param delim La sequenza che separa i campi della tupla (attuale: ' - ')
     */
    public static void loadClienti(String filename, String delim) throws IOException, DAOException {

        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String line = reader.readLine();

        while (line != null) {

            String[] values = line.split(delim);

            String nome = values[0];
            String cognome = values[1];
            String telefono = values[2];
            Integer puntiFedelta = Integer.valueOf(values[3]);
            String iban = values[4];
            String codiceCliente = values[5];

            ClientiDAOMySQL.getInstance().insert(new Clienti(nome, cognome, telefono, puntiFedelta, iban, codiceCliente, null));

            line = reader.readLine();
        }

        reader.close();
    }

    /**
     * La funzione che inserisce nel db gli articoli
     * @param filename Il file che contiene i dati degli articoli da inserire
     * @param delim La sequenza che separa i campi della tupla (attuale: ' - ')
     */
    public static void loadArticoli(String filename, String delim) throws IOException, DAOException {

        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String line = reader.readLine();

        while (line != null) {

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

            line = reader.readLine();
        }

        reader.close();
    }

    /**
     * La funzione che inserisce nel database le carte lette da file
     * @param filename Il nome del file che contiene i dati delle carte da inserire
     * @param delim La sequenza che separa i campi della tupla da inserire (default: ' - ')
     */
    public static void loadCarte(String filename, String delim) throws IOException, DAOException {

        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String line = reader.readLine();

        while (line != null) {

            String[] values = line.split(delim);

            String codiceCarta = values[0];
            String codiceCliente = values[1];

            Integer idCliente = Util.getIdClienteFromCodiceCliente(codiceCliente);
            Float massimaleMensile = Float.valueOf(values[2]);
            Float massimaleRimanente = Float.valueOf(values[3]);
            String pin = values[4];

            CarteDAOMySQL.getInstance().insert(new Carte( massimaleMensile, massimaleRimanente, idCliente, pin, codiceCarta, null));

            line = reader.readLine();
        }

        reader.close();
    }

    /**
     * [---DEPRECATED---] La funzione che inserisce nel database gli ordini
     * @param filename Il nome del file che contiene i dati degli ordini
     * @param delim La sequenza che separa i campi della tupla da inserire (default: ' - ')
     */
    public static void loadOrdini(String filename, String delim) throws IOException, DAOException {

        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String line = reader.readLine();

        while (line != null) {

            String[] values = line.split(delim);

            String codiceOrdine = values[0];

            String codiceCliente = values[1];
            // NB. idCliente is required
            Integer idCliente =  Util.getIdClienteFromCodiceCliente(codiceCliente);

            String data = values[2];

            Float importoTotale = Float.valueOf(values[3]);

            OrdiniDAOMySQL.getInstance().insert(new Ordini(idCliente, data, codiceOrdine, importoTotale, null));

            line = reader.readLine();
        }

        reader.close();
    }

    /**
     * Il main svuota il db e chiama tutte le load
     */
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
