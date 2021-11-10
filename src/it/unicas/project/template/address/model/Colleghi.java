package it.unicas.project.template.address.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.unicas.project.template.address.util.DateUtil;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * Model class for a Colleghi.
 *
 * @author Mario Molinara
 */
public class Colleghi {

    private StringProperty nome;
    private StringProperty cognome;
    private StringProperty telefono;
    private StringProperty email;
    private ObjectProperty<LocalDate> compleanno;
    private IntegerProperty idColleghi;  //wrapper

    private static String attributoStaticoDiEsempio;

    /**
     * Default constructor.
     */
    public Colleghi() {
        this(null, null);
    }

    public Colleghi(String nome, String cognome, String telefono, String email, String compleanno, Integer idColleghi) {
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        this.telefono = new SimpleStringProperty(telefono);
        this.email = new SimpleStringProperty(email);
        this.compleanno = new SimpleObjectProperty<>(DateUtil.parse(compleanno));
        if (idColleghi != null){
            this.idColleghi = new SimpleIntegerProperty(idColleghi);
        } else {
            this.idColleghi = null;
        }
    }

    /**
     * Constructor with some initial data.
     *
     * @param nome
     * @param cognome
     */
    public Colleghi(String nome, String cognome) {
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        // Some initial dummy data, just for convenient testing.
        this.telefono = new SimpleStringProperty("telefono");
        this.email = new SimpleStringProperty("email@email.com");
        this.compleanno = new SimpleObjectProperty<>(DateUtil.parse("24-10-2017"));
        this.idColleghi = null;
    }

    public Integer getIdColleghi(){
        if (idColleghi == null){
            idColleghi = new SimpleIntegerProperty(-1);
        }
        return idColleghi.get();
    }

    public void setIdColleghi(Integer idColleghi) {
        if (this.idColleghi == null){
            this.idColleghi = new SimpleIntegerProperty();
        }
        this.idColleghi.set(idColleghi);
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

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }
    
    public LocalDate getCompleanno() {
        return compleanno.get();
    }

    public void setCompleanno(LocalDate compleanno) {
        this.compleanno.set(compleanno);
    }

    public ObjectProperty<LocalDate> compleannoProperty() {
        return compleanno;
    }


    public String toString(){
        return nome.getValue() + ", " + cognome.getValue() + ", " + telefono.getValue() + ", " + email.getValue() + ", " + DateUtil.format(compleanno.getValue()) + ", (" + idColleghi.getValue() + ")";
    }

    public static void pippo(){
        System.out.println("Ciao");
        attributoStaticoDiEsempio = "Mario";
    }


    public static void main(String[] args) {

        // https://en.wikipedia.org/wiki/Observer_pattern
        Colleghi collega = new Colleghi();
        collega.setNome("Ciao");

        collega.compleannoProperty().addListener(
            new ChangeListener(){
            @Override
            public void changed(ObservableValue o, Object oldVal,
                                Object newVal){
                System.out.println("Compleanno property has changed!");
            }
        });

        collega.compleannoProperty().addListener(
                (o, old, newVal)-> System.out.println("Compleanno property has changed! (Lambda implementation)")
        );


        collega.setCompleanno(DateUtil.parse("30-10-1971"));



        // Use Java Collections to create the List.
        List<Colleghi> list = new ArrayList<>();

        // Now add observability by wrapping it with ObservableList.
        ObservableList<Colleghi> observableList = FXCollections.observableList(list);
        observableList.addListener(new ListChangeListener() {

            @Override
            public void onChanged(ListChangeListener.Change change) {
                System.out.println("Detected a change! ");
            }
        });

        Colleghi c1 = new Colleghi();
        Colleghi c2 = new Colleghi();

        c1.nomeProperty().addListener(
                (o, old, newValue)->System.out.println("Ciao")
        );

        c1.setNome("Pippo");

        // Changes to the observableList WILL be reported.
        // This line will print out "Detected a change!"
        observableList.add(c1);

        // Changes to the underlying list will NOT be reported
        // Nothing will be printed as a result of the next line.
        observableList.add(c2);

        observableList.get(1).setNome("Nuovo valore");

        System.out.println("Size: "+observableList.size());

    }


}
