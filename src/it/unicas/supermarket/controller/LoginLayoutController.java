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
 *  Il controller che gestisce la sezione del login<br>
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

    @FXML
    private void handleConfirm() throws DAOException, SQLException {

        if (cardAccepted) {
            if(!App.getInstance().getMarketSectionsVisited()) {
                App.getInstance().initMarketSectionLayout();
                return;
            }
            else{
                App.getInstance().loadNewUserMarketSection();
            }
            codiceCartaTextField.setEditable(true);
            pinPasswordField.setEditable(true);
        }
        // else
        String codiceCarta = codiceCartaTextField.getText();
        App.getInstance().setCodiceCarta(codiceCarta);
        String pin = pinPasswordField.getText();

        // checking data before accessing db
        if ( !checkLoginData(codiceCarta, pin) )
            return;

        // checking PIN
        String pinFromDB = Util.getPinFromCodiceCarta(codiceCarta);

        if (pinFromDB.equals("ERROR"))
           cardRejected();

        else if (!pin.equals(pinFromDB))
            pinRejected();

        // pin.equals(pinFromDB))
        else{
           loginSuccess(codiceCarta, Util.getCodiceClienteFromCodiceCarta(codiceCarta));
        }

    }

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

    void cardRejected(){
        messageLabel.setText("Carta non trovata.");
        messageLabel.setStyle("-fx-text-fill: rgb(255,255,0)");
        if (this.cardAccepted)
            this.cardAccepted= false;       // updating login-state
    }

    void pinRejected(){
        messageLabel.setText("PIN errato! Riprovare.");
        messageLabel.setStyle("-fx-text-fill: rgb(255,255,0)");
        if (this.cardAccepted)
            this.cardAccepted= false;       // updating login-state
    }

    void loginSuccess(String codiceCarta, String codiceCliente) throws DAOException {

        float massimaleRimanente = Util.getMassimaleRimanenteFromCodiceCarta(codiceCarta);
        float massimaleMensile = Util.getMassimaleMensileFromCodiceCarta(codiceCarta);
        int fedelta = Util.getPuntiFedeltaFromCodiceCliente(codiceCliente);

        App.getInstance().setMassimaleRimanente(massimaleRimanente);
        App.getInstance().setMassimaleMensile(massimaleMensile);
        App.getInstance().setCodiceCarta(codiceCarta);
        App.getInstance().setCodiceCliente(codiceCliente);
        App.getInstance().setPuntiFedelta(fedelta);
        App.getInstance().setReparto("None");

        codiceClienteLabel.setText(codiceCliente);
        massimaliLabel.setText(App.getInstance().getMassimaliString());
        puntiFedeltaLabel.setText(String.valueOf(fedelta));

        // updating login label
        messageLabel.setText("CARTA ACCETTATA!");
        messageLabel.setStyle("-fx-text-fill: rgb(0,255,43)");

        // updating login-state
        this.cardAccepted = true;

        // updating navigation button
        confirmButton.setText("Vai ai reparti");
        codiceCartaTextField.setEditable(false);
        pinPasswordField.setEditable(false);
        codiceCartaLabel.setText(codiceCarta);
    }

    public void resetForm(){
        codiceCartaTextField.setText("");
        pinPasswordField.setText("");
        codiceCartaLabel.setText("xxxx-xxxx-xxxx-xxxx");
        codiceClienteLabel.setText("");
        massimaliLabel.setText("");
        puntiFedeltaLabel.setText("");

        messageLabel.setText("Inserite la vostra carta...");
        messageLabel.setStyle("-fx-text-fill: rgb(0,255,43)");

        confirmButton.setText("Conferma");

        codiceCartaTextField.setEditable(true);
        pinPasswordField.setEditable(true);

        // also resets the card-state!
        cardAccepted = false;
    }

    // called by 'eject' button
    @FXML
    private void handleEject(){
        resetForm();
    }

}

