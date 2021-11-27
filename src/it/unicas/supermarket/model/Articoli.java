package it.unicas.supermarket.model;

import it.unicas.supermarket.model.dao.DAOException;
import javafx.beans.property.*;

import java.util.Objects;

/**
 * Model class for clients.
 * @author GC-GR
 */
public class Articoli {

    private IntegerProperty idArticolo;
    private StringProperty nome;
    private FloatProperty prezzo;
    private IntegerProperty scorteMagazzino;
    private StringProperty barcode;


    public Articoli(String nome, String barcode) throws DAOException {

        this.idArticolo = null;
        this.prezzo = new SimpleFloatProperty(-1f);
        this.scorteMagazzino = new SimpleIntegerProperty(-1);

        if (nome == null || barcode == null)
            throw new DAOException("nome or barcode can't be null");

        if (barcode.length() != 13){
            if (barcode.equals("")){
                if (nome.length() <= 45) {
                    this.nome = new SimpleStringProperty(nome);
                    this.barcode = new SimpleStringProperty("             ");
                }
                else
                    throw new DAOException("nome can't be longer than 45 chars");
            }
            else{
                throw new DAOException("barcode must be long 13 chars");
            }
        }
        else if (nome.equals("")){
            this.barcode = new SimpleStringProperty(barcode);
            this.nome = new SimpleStringProperty("");
        }
        else
            throw new DAOException("barcode or nome must be ''");
    }


    public Articoli(String nome, Float prezzo, Integer scorteMagazzino, String barcode, Integer idArticolo) throws DAOException {

        if (idArticolo != null)
            this.idArticolo = new SimpleIntegerProperty(idArticolo);

        else this.idArticolo = null;

        if (nome == null || nome.length() > 45)
            throw new DAOException("Invalid nome");
        this.nome = new SimpleStringProperty(nome);

        if (prezzo == null || prezzo < 0f)
            throw new DAOException("prezzo can't be null or negative");
        this.prezzo = new SimpleFloatProperty(prezzo);

        if (scorteMagazzino == null || scorteMagazzino < 0)
            throw new DAOException("scorteMagazzino can't be null or negative");
        this.scorteMagazzino = new SimpleIntegerProperty(scorteMagazzino);

        if (barcode == null || barcode.length() != 13)
            throw new DAOException("Invalid barcode");
        this.barcode = new SimpleStringProperty(barcode);
    }


    // Getter setter and property: @idArticolo
    public Integer getIdArticolo() throws DAOException              {
        if (idArticolo == null){
            throw new DAOException("idArticolo is null");
        }
        else return idArticolo.getValue();
    }

    public void setIdArticolo(Integer idArticolo)                   {
        if (this.idArticolo == null){
            this.idArticolo = new SimpleIntegerProperty(idArticolo);
        }
        this.idArticolo.set(idArticolo);
    }

    public IntegerProperty idArticoloProperty()                     { return idArticolo; }


    // Getter setter and property: @nome
    public String getNome()                                         { return nome.get(); }

    public void setNome(String nome)                                { this.nome.set(nome); }

    public StringProperty nomeProperty()                            { return nome; }


    // Getter setter and property: @prezzo
    public Float getPrezzo()                                        { return prezzo.get(); }

    public void setPrezzo(Float prezzo)                             {
        if (this.prezzo == null) {
            this.prezzo = new SimpleFloatProperty();
        }
        this.prezzo.set(prezzo);
    }

    public FloatProperty prezzoProperty()                           { return prezzo; }


    // Getter setter and property: @scorteMagazzino
    public Integer getScorteMagazzino()                             { return scorteMagazzino.get(); }

    public void setScorteMagazzino(Integer scorteMagazzino)         {
        if (this.scorteMagazzino == null) {
            this.scorteMagazzino = new SimpleIntegerProperty();
        }
        this.scorteMagazzino.set(scorteMagazzino);
    }

    public IntegerProperty scorteMagazzinoProperty()                { return scorteMagazzino; }


    // Getter setter and property: @barcode
    public String getBarcode()                                      { return barcode.get(); }

    public void setBarcode(String nome)                             { this.barcode.set(nome); }

    public StringProperty barcodeProperty()                         { return barcode; }


    // toString() method
    public String toString(){
        String id;

        if (idArticoloProperty()==null)
            id = "null";

        else id = idArticolo.getValue().toString();

        return "id: " + id + " - barcode: " + barcode.getValue() + "\n" +
                nome.getValue() + " - " + prezzo.getValue() + " - " + scorteMagazzino.getValue() + "\n";
    }

    public static void main(String[] args) {

        try {
            Articoli test1 = new Articoli("nome", 1000f, 10, "1234567890123", null);

            System.out.println(test1);

        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
