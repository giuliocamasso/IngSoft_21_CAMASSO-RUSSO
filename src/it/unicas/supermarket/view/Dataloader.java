package it.unicas.supermarket.view;

import it.unicas.supermarket.model.Articoli;
import it.unicas.supermarket.model.Clienti;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.mysql.ArticoliDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.ClientiDAOMySQL;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Dataloader {

    // NB. The consistence of read data is checked by class constructors
    public static void loadClienti(String filename, String delim) throws IOException, DAOException {

        BufferedReader reader = null;

        reader = new BufferedReader(new FileReader(filename));

        String line = null;
        // read the first line
        line = reader.readLine();

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

        BufferedReader reader = null;

        reader = new BufferedReader(new FileReader(filename));

        String line = null;
        // read the first line
        line = reader.readLine();

        while (line != null) {

            // tuple columns are separated by the passed delimiter
            String[] values = line.split(delim);

            String nome = values[0];
            Float prezzo = Float.valueOf(values[1]);
            Integer scorteMagazzino = Integer.valueOf(values[2]);
            String barcode = values[3];

            ArticoliDAOMySQL.getInstance().insert(new Articoli(nome, prezzo, scorteMagazzino, barcode, null));

            // reads next line
            line = reader.readLine();
        }

        reader.close();
    }


    public static void main(String[] args){

        try {
            //loadClienti("src/it/unicas/supermarket/Clienti.txt", " - ");
            loadArticoli("src/it/unicas/supermarket/Articoli.txt", " - ");
        } catch (IOException | DAOException e) {
            e.printStackTrace();
        }

        /*
        BufferedReader bufReader = null;

        try {
            bufReader = new BufferedReader(new FileReader("src/it/unicas/supermarket/view/Clienti.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<String> listOfLines = new ArrayList<>();

        String line = null;
        try {
            line = bufReader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

        while (line != null){
            listOfLines.add(line);

            String[] parts = line.split(" ");
            System.out.println(parts[0]);
            System.out.println(parts[1]);
            System.out.println(parts[2]);
            System.out.println(parts[3]);

            try {
                line = bufReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            bufReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(listOfLines);
        */
    }
}
