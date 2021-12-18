package it.unicas.supermarket.model;
import it.unicas.supermarket.model.dao.DAOException;
import javafx.beans.property.*;

/**
 * Classe modello per la tabella Carte con idCarta chiave primaria autoincrementale
 */
public class Carte {

    private IntegerProperty idCarta;
    private FloatProperty massimaleMensile;
    private FloatProperty massimaleRimanente;
    private IntegerProperty idCliente;
    private final StringProperty pin;
    private final StringProperty codiceCarta;

    /**
     * Costruttore basato sull'idCliente e il codice della Carta
     */
    public Carte(Integer idCliente, String codiceCarta) throws DAOException {

        this.idCarta = null;

        this.massimaleMensile = new SimpleFloatProperty(0f);

        this.massimaleRimanente = new SimpleFloatProperty(0f);

        if (idCliente == null)
            throw new DAOException("idCliente can't be null to create a new card");
        else
            this.idCliente = new SimpleIntegerProperty(idCliente);

        this.pin = new SimpleStringProperty("00000");

        this.codiceCarta = new SimpleStringProperty(codiceCarta);
    }


    /**
     * Costruttore a partire da tutti i campi
     */
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


    // Getter e setter: idCarta
    public Integer getIdCarta() throws DAOException                 {
        if (idCarta != null){
            throw new DAOException("idCarta must be null");
        }
        else return null;
    }
    public void setIdCarta(Integer idCarta)                         {
        if (this.idCarta == null){
            this.idCarta = new SimpleIntegerProperty();
        }
        this.idCarta.set(idCarta);
    }
    public IntegerProperty idCartaProperty()                        { return idCarta; }


    // Getter setter e property: massimaleMensile
    public Float getMassimaleMensile()                              { return massimaleMensile.get(); }
    public void setMassimaleMensile(Float massimaleMensile)         {
        if (this.massimaleMensile == null)
            this.massimaleMensile = new SimpleFloatProperty();

        this.massimaleMensile.set(massimaleMensile);
    }
    public FloatProperty massimaleMensileProperty()                 { return massimaleMensile; }


    // Getter setter e property: massimaleRimanente
    public Float getMassimaleRimanente()                            { return massimaleRimanente.get(); }
    public void setMassimaleRimanente(Float massimaleRimanente)     {
        if (this.massimaleRimanente == null)
            this.massimaleRimanente = new SimpleFloatProperty();

        this.massimaleRimanente.set(massimaleRimanente);
    }
    public FloatProperty massimaleRimanenteProperty()               { return massimaleRimanente; }


    // Getter setter e property: idCliente
    public Integer getIdCliente() throws DAOException               {
        if (idCliente == null){
            throw new DAOException("idCliente is null");
        }
        else return idCliente.get();
    }
    public void setIdCliente(Integer idCliente)                     {
        if (this.idCliente == null){
            this.idCliente = new SimpleIntegerProperty();
        }
        this.idCliente.set(idCliente);
    }
    public IntegerProperty idClienteProperty()                      { return idCliente; }


    // Getter setter e property: pin
    public String getPin()                                          { return pin.get(); }
    public void setPin(String pin)                                  { this.pin.set(pin); }
    public StringProperty pinProperty()                             { return pin; }


    // Getter setter e property: codiceCarta
    public String getCodiceCarta()                                  { return codiceCarta.get(); }
    public void setCodiceCarta(String codiceCarta)                  { this.codiceCarta.set(codiceCarta); }
    public StringProperty codiceCartaProperty()                     { return codiceCarta; }


    // toString()
    public String toString(){
        String id;

        if (idCartaProperty()==null)
            id = "null";

        else id = idCarta.getValue().toString();
        return "id: " + id + " - cliente: " + idCliente.getValue() + "\nmassimale: " +
                massimaleRimanente.getValue() + " / " + massimaleMensile.getValue() + "\n" +
                "codice: " + codiceCarta.getValue() + " - pin: " + pin.getValue() + "\n";
    }


    /**
     * Per semplicita' di test, il metodo crea un codice carta fittizio
     */
    public static String ghostCardCode(String seed4)         { return "****-****-****-" + seed4; }


    /**
     * Classe di test per i costruttori
     */
    public static void main(String[] args) {

        try {
            // costruttore 1
            Carte test1 = new Carte(1551f, 154f, 123, "12345", ghostCardCode("1111"), null );
            // costruttore 2
            Carte test2 = new Carte(12,ghostCardCode("2222"));

            System.out.println(test1);
            System.out.println(test2);
        }
        catch (DAOException e) {
            e.printStackTrace();
        }

    }

}