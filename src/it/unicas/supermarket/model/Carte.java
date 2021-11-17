package it.unicas.supermarket.model;

import it.unicas.supermarket.model.dao.DAOException;
import javafx.beans.property.*;

import java.util.Objects;

/**
 * Model class for client cards.
 * @author GC-GR
 */
public class Carte {

    private IntegerProperty idCarta;
    private FloatProperty massimaleMensile;
    private FloatProperty massimaleRimanente;
    private IntegerProperty idCliente;
    private StringProperty pin;
    private StringProperty codiceCarta;


    public Carte(Float massimaleMensile, Float massimaleRimanente, Integer idCliente, String pin, String codiceCarta, Integer idCarta) throws DAOException {

        if (idCarta != null)
            this.idCarta = new SimpleIntegerProperty(idCarta);
        else
            this.idCarta = null;

        if (massimaleMensile == null || massimaleMensile < 0f)
            throw new DAOException("massimaleMensile can't be null or negative");
        this.massimaleMensile = new SimpleFloatProperty(massimaleMensile);

        if (massimaleRimanente == null || massimaleRimanente < 0f || massimaleRimanente > massimaleMensile)
            throw new DAOException("massimaleRimanente can't be null, negative or greater than massimaleMensile");
        this.massimaleRimanente = new SimpleFloatProperty(massimaleRimanente);

        if (idCliente == null)
            throw new DAOException("idCliente can't be null to create a new card");
        else
            this.idCliente = new SimpleIntegerProperty(idCliente);

        if ( pin==null || pin.length() != 5 )
            throw new DAOException("pin must be non null and long 5 characters");
        else
            this.pin = new SimpleStringProperty(pin);

        char delim = '-';
        if ( codiceCarta == null )
            throw new DAOException("codiceCarta can't be null");
        else if( codiceCarta.length() != 19 || codiceCarta.charAt(4) != delim || codiceCarta.charAt(9) != delim || codiceCarta.charAt(14) != delim)
            throw new DAOException("Invalid card code while creating a new card");
        else
            this.codiceCarta = new SimpleStringProperty(codiceCarta);
    }


    // Getter and setter: @idCarta
    public Integer getIdCarta() throws DAOException{
        if (idCarta != null){
            throw new DAOException("idCarta must be null");
        }
        else return null;
    }

    public void setIdCarta(Integer idCarta) {
        if (this.idCarta == null){
            this.idCarta = new SimpleIntegerProperty();
        }
        this.idCarta.set(idCarta);
    }

    public IntegerProperty idCartaProperty()                        { return idCarta; }


    // Getter setter and property: @massimaleMensile
    public Float getMassimaleMensile()                              { return massimaleMensile.get(); }

    public void setMassimaleMensile(Float massimaleMensile)   {
        if (this.massimaleMensile == null)
            this.massimaleMensile = new SimpleFloatProperty();

        this.massimaleMensile.set(massimaleMensile);
    }

    public FloatProperty massimaleMensileProperty()                 { return massimaleMensile; }


    // Getter setter and property: @massimaleRimanente
    public Float getMassimaleRimanente()                            { return massimaleRimanente.get(); }

    public void setMassimaleRimanente(Float massimaleRimanente){
        if (this.massimaleRimanente == null)
            this.massimaleRimanente = new SimpleFloatProperty();

        this.massimaleRimanente.set(massimaleRimanente);
    }

    public FloatProperty massimaleRimanenteProperty()               { return massimaleRimanente; }


    // Getter setter and property: @idCliente
    public Integer getIdCliente() throws DAOException {
        if (idCliente != null){
            throw new DAOException("idCliente must be null");
        }
        else return null;
    }

    public void setIdCliente(Integer idCliente) {
        if (this.idCliente == null){
            this.idCliente = new SimpleIntegerProperty();
        }
        this.idCliente.set(idCliente);
    }

    public IntegerProperty idClienteProperty()                      { return idCliente; }


    // Getter setter and property: @pin
    public String getPin()                                          { return pin.get(); }

    public void setPin(String pin)                                  { this.pin.set(pin); }

    public StringProperty pinProperty()                             { return pin; }



    // Getter setter and property: @codiceCarta
    public String getCodiceCarta()                                  { return codiceCarta.get(); }

    public void setCodiceCarta(String codiceCarta)                  { this.codiceCarta.set(codiceCarta); }

    public StringProperty codiceCartaProperty()                     { return codiceCarta; }


    // toString() method
    public String toString(){
        String id;

        if (idCartaProperty()==null)
            id = "null";

        else id = idCarta.getValue().toString();
        return "id: " + id + " - cliente: " + idCliente.getValue() + "\nmassimale: " +
                massimaleRimanente.getValue() + " / " + massimaleMensile.getValue() + "\n" +
                "codice: " + codiceCarta.getValue() + " - pin: " + pin.getValue() + "\n";
    }


    public static void main(String[] args) {
        Carte test = null;
        try {
            test = new Carte(1551f, 154f, 123, "12345", "1234-1234-1234-1234", null );
        } catch (DAOException e) {
            e.printStackTrace();
        }
        System.out.println(test);
    }

}

