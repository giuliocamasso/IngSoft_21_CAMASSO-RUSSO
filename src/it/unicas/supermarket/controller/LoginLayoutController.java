package it.unicas.supermarket.controller;

import it.unicas.supermarket.App;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.Util;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;

/**
 *  Il controller che gestisce la sezione del login
 */
public class LoginLayoutController {

    // flag per la gestione della navigazione
    private Boolean cardAccepted = false;

    // label messaggio-errori
    @FXML private Label messageLabel;

    // Form
    @FXML private TextField codiceCartaTextField;
    @FXML private PasswordField pinPasswordField;

    // Bottoni navigazione
    @FXML private Button confirmButton;
    @FXML private Button ejectButton;

    // Carta
    @FXML private Label codiceCartaLabel;
    @FXML private Label codiceClienteLabel;
    @FXML private Label massimaliLabel;
    @FXML private Label puntiFedeltaLabel;

    // getter-setter
    public Label getMessageLabel()                                      { return messageLabel;              }

    public TextField getCodiceCartaTextField()                          { return codiceCartaTextField;      }
    public PasswordField getPinPasswordField()                          { return pinPasswordField;          }

    public Label getMassimaliLabel()                                    { return massimaliLabel;            }
    public Label getPuntiFedeltaLabel()                                 { return puntiFedeltaLabel;         }
    public Button getConfirmButton()                                    { return confirmButton;             }

    // getter-setter del flag per la navigazione
    public Boolean getCardAccepted()                                    { return cardAccepted;              }
    public void setCardAccepted(Boolean cardAccepted)                   { this.cardAccepted = cardAccepted; }


    /**
     * Il metodo controlla l'integrita' dei dati inseriti nel form
     * @param codiceCarta Il contenuto della textField (codiceCartaTextField)
     * @param pin Il contenuto della passwordField (pinPasswordField)
     * @return Restituisce true se i dati sono coerenti con il formato 'xxxx-xxxx-xxxx-xxxx', '*****'
     */
    Boolean checkLoginData (String codiceCarta, String pin){

        if (codiceCarta.length() != 19 ||
                codiceCarta.charAt(4) != '-'||codiceCarta.charAt(9) != '-' || codiceCarta.charAt(14) != '-'){
            messageLabel.setText("Codice carta non valido.");
            messageLabel.setStyle("-fx-text-fill: rgb(255,255,0)");
            return false;
        }
        else if (pin.length() !=5) {
            messageLabel.setText("PIN non valido.");
            messageLabel.setStyle("-fx-text-fill: rgb(255,255,0)");
            return false;
        }
        else return true;
    }

    /**
     * Il metodo e' chiamato se la carta non risulta presente nel database<br>
     * Aggiorna le label e setta il flag cardAccepted a false
     */
    void cardRejected(){
        messageLabel.setText("Carta non trovata.");
        messageLabel.setStyle("-fx-text-fill: rgb(255,255,0)");
        if (this.cardAccepted)
            this.cardAccepted= false;
    }

    /**
     * Il metodo e' chiamato quando il pin inserito non combacia con quello letto dal db<br>
     * Aggiorna le label e setta il flag cardAccepted a false
     */
    void pinRejected(){
        messageLabel.setText("PIN errato! Riprovare.");
        messageLabel.setStyle("-fx-text-fill: rgb(255,255,0)");
        if (this.cardAccepted)
            this.cardAccepted= false;
    }

    /**
     * Il metodo e' chiamato quando il login viene effettuato con successo<br>
     * Aggiorna le variabili legate all'utente in App, aggiorna le label e il bottone di navigazione che manda ai reparti
     * @param codiceCarta Il codice della carta inserito
     * @param codiceCliente Il codice del cliente associato alla carta inserita (leto da db)
     */
    void loginSuccess(String codiceCarta, String codiceCliente) throws DAOException {

        // lettura dati utente da db
        float massimaleRimanente = Util.getMassimaleRimanenteFromCodiceCarta(codiceCarta);
        float massimaleMensile = Util.getMassimaleMensileFromCodiceCarta(codiceCarta);
        int fedelta = Util.getPuntiFedeltaFromCodiceCliente(codiceCliente);

        // salvataggio dati utente in App per accesso rapido nelle sezioni successive
        App.getInstance().setMassimaleRimanente(massimaleRimanente);
        App.getInstance().setMassimaleMensile(massimaleMensile);
        App.getInstance().setCodiceCarta(codiceCarta);
        App.getInstance().setCodiceCliente(codiceCliente);
        App.getInstance().setPuntiFedelta(fedelta);

        // Il reparto e' impostato su 'nessuno'
        App.getInstance().setReparto("None");

        // aggiornamento delle labels
        codiceCartaLabel.setText(codiceCarta);
        codiceClienteLabel.setText(codiceCliente);
        massimaliLabel.setText(App.getInstance().getMassimaliString());
        puntiFedeltaLabel.setText(String.valueOf(fedelta));

        messageLabel.setText("CARTA ACCETTATA!");
        messageLabel.setStyle("-fx-text-fill: rgb(0,255,43)");

        // aggiornamento del flag
        this.cardAccepted = true;

        // aggiornamento bottone di navigazione
        confirmButton.setText("Vai ai reparti");

        // blocco dei campi del form
        codiceCartaTextField.setEditable(false);
        pinPasswordField.setEditable(false);

    }

    /**
     * Il metodo svuota il form e ripristina le label informative ai valori/messaggi di default
     */
    public void resetForm(){

        // reset carta
        codiceCartaLabel.setText("xxxx-xxxx-xxxx-xxxx");
        codiceClienteLabel.setText("");
        massimaliLabel.setText("");
        puntiFedeltaLabel.setText("");

        // reset form
        codiceCartaTextField.setText("");
        pinPasswordField.setText("");

        // reset label di messaggio
        messageLabel.setText("Inserite la vostra carta...");
        messageLabel.setStyle("-fx-text-fill: rgb(0,255,43)");

        // reset del bottone conferma
        confirmButton.setText("Conferma");

        // ri-abilitazione dei campi del form
        codiceCartaTextField.setEditable(true);
        pinPasswordField.setEditable(true);

        // reset flag di stato
        cardAccepted = false;
    }

    /**
     * Il metodo gestisce il bottone 'Conferma' nella schermata del login
     */
    @FXML private void handleConfirm() throws DAOException, SQLException {

        // Se la carta e' stata accettata passo alla schermata di navigazione nel supermercato
        if (cardAccepted) {
            // dopo il primo login devo inizializzare la scena...
            if(!App.getInstance().getMarketSectionsVisited()) {
                App.getInstance().initMarketSectionLayout();
                return;
            }
            // ...altrimenti la aggiorno e la setto
            else{
                App.getInstance().loadNewUserMarketSection();
            }

            // riabilito la modifica dei field del form di login per gli inserimenti futuri
            codiceCartaTextField.setEditable(true);
            pinPasswordField.setEditable(true);
        }
        // if ! cardAccepted

        // leggo il contenuto del form
        String codiceCarta = codiceCartaTextField.getText();
        String pin = pinPasswordField.getText();

        // controllo integrita' dei dati
        if ( !checkLoginData(codiceCarta, pin) )
            return;

        // aggiorno la carta corrente in App
        App.getInstance().setCodiceCarta(codiceCarta);

        // leggo il pin associato alla carta dal db e lo confronto con quello inserito dall'utente
        String pinFromDB = Util.getPinFromCodiceCarta(codiceCarta);

        // caso carta inesistente
        if (pinFromDB.equals("ERROR"))
            cardRejected();
            // caso pin errato (carta esistente)
        else if (!pin.equals(pinFromDB))
            pinRejected();
            // caso carta esistente e pin corretto
        else{
            loginSuccess(codiceCarta, Util.getCodiceClienteFromCodiceCarta(codiceCarta));
        }

    }

    /**
     * Metodo chiamato dal bottone 'rimuovi'<br>
     * Esegue il reset della schermata di login
     */
    @FXML private void handleEject(){
        resetForm();
    }

}

