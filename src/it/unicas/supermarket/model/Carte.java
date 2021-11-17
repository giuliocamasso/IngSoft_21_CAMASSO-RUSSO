package it.unicas.supermarket.model;

import javafx.beans.property.*;

import java.util.Objects;

/**
 * Model class for client cards.
 * @author GC-GR
 */
public class Carte {

    private IntegerProperty idCarta;
    private IntegerProperty Cliente_idCliente;
    private FloatProperty massimaleMensile;
    private FloatProperty massimaleRimanente;

    public Carte(Integer idCarta, Integer idCliente, Float massimaleMensile, Float massimaleRimanente ) {

        if (idCarta != null){
            this.idCarta = new SimpleIntegerProperty(idCarta);
        } else {
            this.idCarta = null;
        }

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

    // getter and setter: @idCarta
    public Integer getIdCarta(){
        if (idCarta == null){
            idCarta = new SimpleIntegerProperty(-1);
        }
        return idCarta.get();
    }

    public void setIdCarta(Integer idCarta) {
        if (this.idCarta == null){
            this.idCarta = new SimpleIntegerProperty();
        }
        this.idCarta.set(idCarta);
    }

    // getter and setter: @idCliente
    public Integer getCliente_idCliente(){
        if (Cliente_idCliente == null){
            Cliente_idCliente = new SimpleIntegerProperty(-1);
        }
        return Cliente_idCliente.get();
    }

    public void setCliente_idCliente(Integer cliente_idCliente) {
        if (this.Cliente_idCliente == null){
            this.Cliente_idCliente = new SimpleIntegerProperty();
        }
        this.Cliente_idCliente.set(cliente_idCliente);
    }

    // Getter setter and property: @massimaleMensile
    public Float getMassimaleMensile()                             { return massimaleMensile.get(); }

    public void setMassimaleMensile(Float massimaleMensile)   {
        if (this.massimaleMensile == null)
            this.massimaleMensile = new SimpleFloatProperty();

        this.massimaleMensile.set(massimaleMensile);
    }

    public FloatProperty massimaleMensileProperty()             { return massimaleMensile; }

    // Getter setter and property: @massimaleRimanente
    public Float getMassimaleRimanente()                           { return massimaleRimanente.get(); }

    public void setMassimaleRimanente(Float massimaleRimanente){
        if (this.massimaleRimanente == null)
            this.massimaleRimanente = new SimpleFloatProperty();

        this.massimaleRimanente.set(massimaleRimanente);
    }

    public FloatProperty massimaleRimanenteProperty()           { return massimaleRimanente; }


    // toString() method
    public String toString(){
        return "idCarta: " + Cliente_idCliente.getValue() + "\nidCliente: " + Cliente_idCliente.getValue() + "\nrimanente/mensile: " + massimaleRimanente.getValue() + " / " + massimaleMensile.getValue() +"\n";
    }


    public static void main(String[] args) {
        Carte test = new Carte(666, 999, 1000.0f, 666.999f);
        System.out.println(test);
    }

}

