package it.unicas.supermarket.model;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * Model class for a Clienti.
 *
 * @author GC-GR
 */
public class Cliente {

    private IntegerProperty idCliente;  //wrapper

    private StringProperty nome;        //VARCHAR(45)
    private StringProperty cognome;
    private StringProperty telefono;

    private IntegerProperty punti_fedelta;  //wrapper

    /**
     * Default constructor.
     */
    public Cliente() {
        this(null, null);
    }

    public Cliente(String nome, String cognome, String telefono, Integer punti_fedelta, Integer idCliente) {
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        this.telefono = new SimpleStringProperty(telefono);

        this.punti_fedelta = new SimpleIntegerProperty(punti_fedelta);

        if (idCliente != null){
            this.idCliente = new SimpleIntegerProperty(idCliente);
        } else {
            this.idCliente = null;
        }
    }

    /**
     * Constructor with some initial data.
     *
     * @param nome
     * @param cognome
     */
    public Cliente(String nome, String cognome) {
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        // Some initial dummy data, just for convenient testing.
        this.telefono = new SimpleStringProperty("---");
        this.punti_fedelta = new SimpleIntegerProperty(0);

        this.idCliente = null;
    }

    public Integer getIdCliente(){
        if (idCliente == null){
            idCliente = new SimpleIntegerProperty(-1);
        }
        return idCliente.get();
    }

    public void setIdCliente(Integer idCliente) {
        if (this.idCliente == null){
            this.idCliente = new SimpleIntegerProperty();
        }
        this.idCliente.set(idCliente);
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public String getCognome() {
        return cognome.get();
    }

    public void setCognome(String cognome) {
        this.cognome.set(cognome);
    }

    public StringProperty cognomeProperty() {
        return cognome;
    }

    public String getTelefono() {
        return telefono.get();
    }

    public void setTelefono(String telefono) {
        this.telefono.set(telefono);
    }

    public StringProperty telefonoProperty() {
        return telefono;
    }

    public Integer getpunti_fedelta() {
        return punti_fedelta.get();
    }

    public void setpunti_fedelta(Integer punti_fedelta) {
        this.punti_fedelta.set(punti_fedelta);
    }

    public IntegerProperty punti_fedeltaProperty() {
        return punti_fedelta;
    }


    public String toString(){
        return nome.getValue() + ", " + cognome.getValue() + ", " + telefono.getValue() + ", " + punti_fedelta.getValue() + ", " + idCliente.getValue() + ")";
    }


    public static void main(String[] args) {

    }

}

