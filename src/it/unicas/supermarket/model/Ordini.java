package it.unicas.supermarket.model;

import it.unicas.supermarket.model.dao.DAOException;
import javafx.beans.property.*;

/**
* Classe modello per la tabella Ordini
 */
public class Ordini {

    private IntegerProperty idOrdine;
    private IntegerProperty idCliente;
    private final StringProperty data;
    private final StringProperty codiceOrdine;
    private FloatProperty importoTotale;

    /**
     * Costruttore di utilita'
     * @param codiceOrdine autodescrittivo
     */
    public Ordini(String codiceOrdine) throws DAOException {

        this.idOrdine = null;
        this.idCliente = new SimpleIntegerProperty(-1);
        this.data = new SimpleStringProperty("  -  -       :  ");

        if ( codiceOrdine == null )
            throw new DAOException("codiceOrdine can't be null");
        else if( codiceOrdine.length() != 10 )
            throw new DAOException("Invalid order code while creating a new card");
        else
            this.codiceOrdine = new SimpleStringProperty(codiceOrdine);

        this.importoTotale = new SimpleFloatProperty(-1f);
    }

    /**
     * Costruttore 'completo'
     * @param idCliente autodescrittivo
     * @param data autodescrittivo (generata al momento dell'ordine)
     * @param codiceOrdine autodescrittivo (basato sulla data, generato al momento dell'ordine)
     * @param importoTotale autodescrittivo
     * @param idOrdine autodescrittivo
     */
    public Ordini(Integer idCliente, String data, String codiceOrdine, Float importoTotale, Integer idOrdine) throws DAOException {

        if (idOrdine != null)
            this.idOrdine = new SimpleIntegerProperty(idOrdine);
        else
            this.idOrdine = null;


        if (idCliente == null)
            throw new DAOException("idCliente can't be null to create a new order");
        else
            this.idCliente = new SimpleIntegerProperty(idCliente);

        // "19-11-2021 16:10"   esempio data
        char delim1 = '-';
        char delim2 = ' ';
        char delim3 = ':';

        if ( data == null )
            throw new DAOException("data can't be null");
        else if( data.length() != 16 || data.charAt(2) != delim1 || data.charAt(5) != delim1 || data.charAt(10) != delim2 || data.charAt(13) != delim3)
            throw new DAOException("Invalid data format while creating a new order (XX-XX-XXXX XX:XX)");
        else
            this.data = new SimpleStringProperty(data);


        if ( codiceOrdine == null )
            throw new DAOException("codiceOrdine can't be null");
        else if( codiceOrdine.length() != 10 )
            throw new DAOException("Invalid order code while creating a new ordine");
        else
            this.codiceOrdine = new SimpleStringProperty(codiceOrdine);


        if (importoTotale == null || importoTotale < 0f)
            throw new DAOException("importoTotale can't be null or negative");
        this.importoTotale = new SimpleFloatProperty(importoTotale);

    }

    // Getter setter and property: @idOrdine
    public Integer getIdOrdine() throws DAOException{
       if(idOrdine == null)
           throw new DAOException("idOrdine can't be null");
       else
           return idOrdine.getValue();
    }
    public void setIdOrdine(Integer idOrdine) {
        if (this.idOrdine == null){
            this.idOrdine = new SimpleIntegerProperty();
        }
        this.idOrdine.set(idOrdine);
    }
    public IntegerProperty idOrdineProperty()                       { return idOrdine; }

    // Getter setter and property: @idCliente
    public Integer getIdCliente() throws DAOException {
        if (idCliente == null){
            throw new DAOException("idCliente is null");
        }
        else return idCliente.get();
    }
    public void setIdCliente(Integer idCliente) {
        if (this.idCliente == null){
            this.idCliente = new SimpleIntegerProperty();
        }
        this.idCliente.set(idCliente);
    }
    public IntegerProperty idClienteProperty()                      { return idCliente; }

    // Getter setter and property: @data
    public String getData()                                         { return data.get(); }
    public void setData(String data)                                { this.data.set(data); }
    public StringProperty dataProperty()                            { return data; }

    // Getter setter and property: @codiceOrdine
    public String getCodiceOrdine()                                 { return codiceOrdine.get(); }
    public void setCodiceOrdine(String codiceOrdine)                { this.codiceOrdine.set(codiceOrdine); }
    public StringProperty codiceOrdineProperty()                    { return codiceOrdine; }

    // Getter setter and property: @importoTotale
    public Float getImportoTotale()                                 { return importoTotale.get(); }
    public void setImportoTotale(Float importoTotale)   {
        if (this.importoTotale == null)
            this.importoTotale = new SimpleFloatProperty();

        this.importoTotale.set(importoTotale);
    }
    public FloatProperty importoTotaleProperty()                    { return importoTotale; }


    // toString() method
    public String toString(){
        String id;

        if (idOrdineProperty()==null)
            id = "null";

        else id = idOrdine.getValue().toString();
        return "idOrdine: " + id + " - cliente: " + idCliente.getValue() + "\nimporto: " + importoTotale.getValue() + "$\ncodiceOrdine: " +
                codiceOrdine.getValue() + ", data: " + data.getValue() + "\n";
    }

    /**
     * Testing function
     */
    public static void main(String[] args) {

        try {
            // costruttore 1
            Ordini test1 = new Ordini(1, "19-11-2021 16:53", "0123456789", 9999f, null );

            // costruttore 1
            Ordini test2 = new Ordini("0123456789");

            System.out.println(test1);
            System.out.println(test2);
        }
        catch (DAOException e) {
            e.printStackTrace();
        }
    }
}

