package it.unicas.supermarket.model;

import javafx.beans.property.*;

import java.util.Objects;

/**
 * Model class for client cards.
 * @author GC-GR
 */
public class Carta {

    private IntegerProperty idCarta;
    private IntegerProperty Cliente_idCliente;
    private FloatProperty massimaleMensile;
    private FloatProperty massimaleRimanente;

    public Carta(Integer idCarta, Integer idCliente, Float massimaleMensile, Float massimaleRimanente ) {

        if (idCarta != null){
            this.idCarta = new SimpleIntegerProperty(idCarta);
        } else {
            this.idCarta = null;
        }

        if (idCliente != null){
            this.Cliente_idCliente = new SimpleIntegerProperty(idCliente);
        } else {
            this.Cliente_idCliente = null;
        }

        this.massimaleMensile = new SimpleFloatProperty(Objects.requireNonNullElse(massimaleMensile, 0f));

        this.massimaleRimanente = new SimpleFloatProperty(Objects.requireNonNullElse(massimaleRimanente, 0f));

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
        Carta test = new Carta(666, 999, 1000.0f, 666.999f);
        System.out.println(test);
    }

}

