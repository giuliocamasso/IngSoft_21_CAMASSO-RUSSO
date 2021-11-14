package it.unicas.supermarket.model;

import javafx.beans.property.*;

import java.util.Objects;

/**
 * Model class for a Clienti.
 * @author GC-GR
 */
public class Cliente {

    private IntegerProperty idCliente;
    private StringProperty nome;                // varchar(45)
    private StringProperty cognome;
    private StringProperty telefono;
    private IntegerProperty puntiFedelta;

    /**
     * @author GC-GR
     */
    public Cliente(String nome, String cognome, String telefono, Integer puntiFedelta, Integer idCliente) {

        if (idCliente != null){
            this.idCliente = new SimpleIntegerProperty(idCliente);
        } else {
            this.idCliente = null;
        }

        this.nome = new SimpleStringProperty(Objects.requireNonNullElse(nome, ""));

        this.cognome = new SimpleStringProperty(Objects.requireNonNullElse(cognome, ""));

        this.telefono = new SimpleStringProperty(Objects.requireNonNullElse(telefono, ""));

        if (puntiFedelta != null){
            this.puntiFedelta = new SimpleIntegerProperty(puntiFedelta);
        } else {
            this.puntiFedelta = null;
        }
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

    // Getter setter and property: @nome
    public String getNome()                             { return nome.get(); }

    public void setNome(String nome)                    { this.nome.set(nome); }

    public StringProperty nomeProperty()                { return nome; }

    // Getter setter and property: @cognome
    public String getCognome()                          { return cognome.get(); }

    public void setCognome(String cognome)              { this.cognome.set(cognome); }

    public StringProperty cognomeProperty()             { return cognome; }

    // Getter setter and property: @telefono
    public String getTelefono()                         { return telefono.get(); }

    public void setTelefono(String telefono)            { this.telefono.set(telefono); }

    public StringProperty telefonoProperty()            { return telefono; }

    // Getter setter and property: @puntiFedelta
    public Integer getPuntiFedelta()                    { return puntiFedelta.get(); }

    public void setPuntiFedelta(Integer puntiFedelta)   {
        if (this.puntiFedelta == null)
            this.puntiFedelta = new SimpleIntegerProperty();

        this.puntiFedelta.set(puntiFedelta);
    }

    public IntegerProperty puntiFedeltaProperty()       { return puntiFedelta; }

    // toString() method
    public String toString(){
        return "idCliente: " + idCliente.getValue() + "\n" + nome.getValue() + " - " + cognome.getValue() + " - " + telefono.getValue() + "\npunti: " + puntiFedelta.getValue() +"\n";
    }


    public static void main(String[] args) {
        Cliente test = new Cliente("Mario", "Rossi", "----------", 123, 999);
        System.out.println(test);
    }

}

