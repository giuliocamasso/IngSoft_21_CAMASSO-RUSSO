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
    private StringProperty reparto;
    private StringProperty produttore;
    private StringProperty descrizioneProdotto;
    private StringProperty descrizioneQuantita;

    private String imageURL = null;

    public Articoli(String nome, String barcode) throws DAOException {

        this.idArticolo = null;
        this.prezzo = new SimpleFloatProperty(-1f);
        this.scorteMagazzino = new SimpleIntegerProperty(-1);

        this.reparto = new SimpleStringProperty("");
        this.produttore = new SimpleStringProperty("");
        this.descrizioneProdotto = new SimpleStringProperty("");
        this.descrizioneQuantita = new SimpleStringProperty("");

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


    public Articoli(String nome, Float prezzo, Integer scorteMagazzino, String barcode, String reparto, String produttore, String descrizioneProdotto, String descrizioneQuantita, Integer idArticolo) throws DAOException {

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

        if (reparto == null || reparto.length() > 45)
            throw new DAOException("Invalid reparto");
        this.reparto = new SimpleStringProperty(reparto);

        if (produttore == null || produttore.length() > 45)
            throw new DAOException("Invalid produttore");
        this.produttore = new SimpleStringProperty(produttore);

        if (descrizioneProdotto == null || descrizioneProdotto.length() > 45)
            throw new DAOException("Invalid descrizioneProdotto");
        this.descrizioneProdotto = new SimpleStringProperty(descrizioneProdotto);

        if (descrizioneQuantita == null || descrizioneQuantita.length() > 45)
            throw new DAOException("Invalid descrizioneQuantita");
        this.descrizioneQuantita = new SimpleStringProperty(descrizioneQuantita);

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

    // Getter setter and property: @produttore
    public String getProduttore()                                   { return produttore.get(); }

    public void setProduttore(String produttore)                    { this.produttore.set(produttore); }

    public StringProperty produttoreProperty()                      { return produttore; }

    // Getter setter and property: @reparto
    public String getReparto()                                      { return reparto.get(); }

    public void setReparto(String reparto)                          { this.reparto.set(reparto); }

    public StringProperty repartoProperty()                         { return reparto; }

    // Getter setter and property: @descrizioneProdotto
    public String getDescrizioneProdotto()                          { return descrizioneProdotto.get(); }

    public void setDescrizioneProdotto(String descrizioneProdotto)  { this.descrizioneProdotto.set(descrizioneProdotto); }

    public StringProperty descrizioneProdottoProperty()             { return descrizioneProdotto; }

    // Getter setter and property: @descrizioneQuantita
    public String getDescrizioneQuantita()                          { return descrizioneQuantita.get(); }

    public void setDescrizioneQuantita(String descrizioneQuantita)  { this.descrizioneQuantita.set(descrizioneQuantita); }

    public StringProperty descrizioneQuantitaProperty()             { return descrizioneQuantita; }


    public String getImageURL()                                     { return imageURL; }
    public void setImageURL(String imageURL)                        { this.imageURL = imageURL; };

    // toString() method
    public String toString(){
        String id;

        if (idArticoloProperty()==null)
            id = "null";

        else id = idArticolo.getValue().toString();

        return "id: " + id + " - barcode: " + barcode.getValue() + "\n" +
                nome.getValue() + " - " + prezzo.getValue() + " - " + scorteMagazzino.getValue() + "\n" +
                "produttore: " + produttore.getValue() + "  reparto: " + reparto.getValue() + "\n";
    }

    public static void main(String[] args) {

        try {
            Articoli test1 = new Articoli("nome", 1000f, 10, "1234567890123","Macelleria", "CONAD", "Bistecca suino", "500g", null);

            System.out.println(test1);

        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    boolean controlloReparti(String reparto){
        if (    reparto.equals("Macelleria") || reparto.equals("Pescheria")  ||
                reparto.equals("Ortofrutta") || reparto.equals("Alimentari") ||
                reparto.equals("Forno")      || reparto.equals("Bevande")    ||
                reparto.equals("Surgelati")  || reparto.equals("Snacks")     ||
                reparto.equals("Baby")       || reparto.equals("Cartoleria") ||
                reparto.equals("Pet")        || reparto.equals("Benessere")  ||
                reparto.equals("Casalinghi")
        )
            return true;
        else
            return false;
    }

    public static String getURLfromCode(String barcode){
        return switch (barcode) {
            // Ortofrutta
            case "000000_BANANA" -> "resources/images/Ortofrutta/banana.png";
            case "000000_MELE01" -> "resources/images/Ortofrutta/yellowApples.png";
            case "000000_MELE02" -> "resources/images/Ortofrutta/red-apples.png";
            case "00000_ICEBERG" -> "resources/images/Ortofrutta/insalata.png";

            // Forno
            case "0000_BAGUETTE" -> "resources/images/Forno/baguette.png";
            case "000_CROISSANT" -> "resources/images/Forno/cornetto.png";
            case "00000000_PANE" -> "resources/images/Forno/pane.png";

            default -> "ERROR";
        };
    }
}


