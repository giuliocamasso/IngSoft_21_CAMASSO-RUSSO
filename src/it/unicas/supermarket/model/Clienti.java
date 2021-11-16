package it.unicas.supermarket.model;

import it.unicas.supermarket.model.dao.DAOException;
import javafx.beans.property.*;

import java.util.Objects;

/**
 * Model class for clients.
 * @author GC-GR
 */
public class Clienti {

    private IntegerProperty idCliente;
    private StringProperty nome;                // varchar(45)
    private StringProperty cognome;
    private StringProperty telefono;
    private IntegerProperty puntiFedelta;
    private StringProperty iban;
    private StringProperty codiceCliente;

    public Clienti(String nome, String cognome, String telefono, Integer puntiFedelta, String iban, String codiceCliente, Integer idCliente) throws DAOException {

        if (idCliente != null)
            this.idCliente = new SimpleIntegerProperty(idCliente);

        else this.idCliente = null;

        this.nome = new SimpleStringProperty(Objects.requireNonNullElse(nome, ""));

        this.cognome = new SimpleStringProperty(Objects.requireNonNullElse(cognome, ""));

        this.telefono = new SimpleStringProperty(Objects.requireNonNullElse(telefono, ""));

        if (puntiFedelta != null){
            this.puntiFedelta = new SimpleIntegerProperty(puntiFedelta);
        } else {
            this.puntiFedelta = null;
        }

        this.iban = new SimpleStringProperty(Objects.requireNonNullElse(iban, ""));

        this.codiceCliente = new SimpleStringProperty(Objects.requireNonNullElse(codiceCliente, ""));

    }

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

    // Getter setter and property: @nome
    public String getNome()                                 { return nome.get(); }

    public void setNome(String nome)                        { this.nome.set(nome); }

    public StringProperty nomeProperty()                    { return nome; }

    // Getter setter and property: @cognome
    public String getCognome()                              { return cognome.get(); }

    public void setCognome(String cognome)                  { this.cognome.set(cognome); }

    public StringProperty cognomeProperty()                 { return cognome; }

    // Getter setter and property: @telefono
    public String getTelefono()                             { return telefono.get(); }

    public void setTelefono(String telefono)                { this.telefono.set(telefono); }

    public StringProperty telefonoProperty()                { return telefono; }


    // Getter setter and property: @puntiFedelta
    public Integer getPuntiFedelta()                        { return puntiFedelta.get(); }

    public void setPuntiFedelta(Integer puntiFedelta)       {
        if (this.puntiFedelta == null)
            this.puntiFedelta = new SimpleIntegerProperty();

        this.puntiFedelta.set(puntiFedelta);
    }

    public IntegerProperty puntiFedeltaProperty()           { return puntiFedelta; }

    // Getter setter and property: @iban
    public String getIban()                                 { return iban.get(); }

    public void setIban(String iban)                        { this.iban.set(iban); }

    public StringProperty ibanProperty()                    { return iban; }

    // Getter setter and property: @codiceCliente
    public String getCodiceCliente()                        { return codiceCliente.get(); }

    public void setCodiceCliente(String codiceCliente)      { this.codiceCliente.set(codiceCliente); }

    public StringProperty codiceClienteProperty()           { return codiceCliente; }

    // toString() method
    public String toString(){
        return "id: " + Objects.requireNonNullElse(idCliente.getValue(), "null") + " - codice: " + codiceCliente.getValue() + "\n" +
                nome.getValue() + " - " + cognome.getValue() + " - " + telefono.getValue() +
                "\npunti: " + puntiFedelta.getValue() + " - IBAN:" + iban.getValue() +"\n";
    }


    public static void main(String[] args) {
        Clienti test = null;
        try {
            test = new Clienti("Mario", "Rossi", "", 123, "sdfsdv999", "clientedv999", null);
        } catch (DAOException e) {
            e.printStackTrace();
        }
        System.out.println(test);
    }

}

