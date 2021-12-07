package it.unicas.supermarket.controller;

import it.unicas.supermarket.App;
import it.unicas.supermarket.model.Carte;
import it.unicas.supermarket.model.Clienti;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.mysql.CarteDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.ClientiDAOMySQL;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


import java.util.List;

/**
 * [ ! MUST UPDATE THIS______________________________________________________________________________]
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 *
 * @author GC-GR
 */
public class LoginLayoutController {

    /**
     * Closes the application.
     */

    @FXML
    private TextField codiceCartaTextField;

    @FXML
    private PasswordField pinPasswordField;

    @FXML
    private Label messageLabel;

    @FXML
    private Label codiceCartaLabel;

    @FXML
    private Label codiceClienteLabel;

    @FXML
    private Label massimaliLabel;

    @FXML
    private Label puntiFedeltaLabel;

    @FXML
    private Button confirmButton;

    @FXML
    private Button ejectButton;

    private Boolean cardAccepted = false;

    @FXML
    private void handleExit() {

        App.getInstance().handleExit();

    }

    @FXML
    private void handleConfirm() throws DAOException {
        System.out.println("Validating card...");
        String codiceCarta = codiceCartaTextField.getText();
        String pin = pinPasswordField.getText();

        if (checkLoginData(codiceCarta, pin))
            System.out.println("Dati ok");
        else
            System.out.println("errore");

        String pinFromDB = getPinFromCodiceCarta(codiceCarta);
        if (pinFromDB.equals("ERROR")){
            messageLabel.setText("Carta non trovata.");
            messageLabel.setStyle("-fx-text-fill: rgb(255,255,0)");
            if (this.cardAccepted)
                this.cardAccepted= false;
        }


        else if (pin.equals(pinFromDB)) {
            System.out.println("PIN CORRETTO: " + pinFromDB);
            messageLabel.setText("CARTA ACCETTATA!");
            messageLabel.setStyle("-fx-text-fill: rgb(0,255,43)");
            this.cardAccepted = true;
            confirmButton.setText("AI REPARTI");
            codiceCartaLabel.setText(codiceCarta);
            codiceClienteLabel.setText(getCodiceClienteFromCodiceCarta(codiceCarta));

        }
        else {
            System.out.println("PIN errato(carta esistente): " + pin );
            messageLabel.setStyle("-fx-text-fill: rgb(255,255,0)");
            messageLabel.setText("PIN errato.");
            if (this.cardAccepted)
                this.cardAccepted= false;
        }

    }

    Boolean checkLoginData (String codiceCarta, String pin){

        if (codiceCarta.length() != 19 || codiceCarta.charAt(4) != '-' || codiceCarta.charAt(9) != '-' || codiceCarta.charAt(14) != '-' || pin.length() !=5)
            return false;
        else return true;
    }

    String getPinFromCodiceCarta(String codiceCarta) throws DAOException {
        List<Carte> cardToBeChecked = CarteDAOMySQL.getInstance().select(new Carte(-1, codiceCarta));
        if (cardToBeChecked.size() == 1)
            return cardToBeChecked.get(0).getPin();
        else return "ERROR";
    }

    String getCodiceClienteFromCodiceCarta(String codiceCarta) throws DAOException {
        List<Carte> card = CarteDAOMySQL.getInstance().select(new Carte(-1, codiceCarta));
        if (card.size() == 1) {
            Integer idCliente = card.get(0).getIdCliente();
            return idCliente.toString();
        } else return "ERROR";
    }

    /*
    SELECT * from Carte JOIN Clienti on Carte.idCliente = Cliente.idCliente
    WHERE codiceCarta=@codiceCarta
    * */
}

