package it.unicas.supermarket.controller;

import it.unicas.supermarket.App;
import it.unicas.supermarket.model.Carte;
import it.unicas.supermarket.model.Clienti;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.mysql.CarteDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.ClientiDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.DAOMySQLSettings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * [ ! MUST UPDATE THIS______________________________________________________________________________]
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 *
 * @author GC-GR
 */
public class LoginLayoutController {

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

    private static Logger logger =  Logger.getLogger(LoginLayoutController.class.getName());

    @FXML
    private void handleConfirm() throws DAOException, SQLException {

        // debug per andare subito ai reparti

        if (cardAccepted) {
            App.getInstance().initMarketSectionLayout();
            resetForm();
            return;
        }
        // else
        String codiceCarta = codiceCartaTextField.getText();
        App.getInstance().getCodiceCarta(codiceCarta);
        String pin = pinPasswordField.getText();

        // checking data before accessing db
        if ( !checkLoginData(codiceCarta, pin) )
            return;

        // checking PIN
        String pinFromDB = getPinFromCodiceCarta(codiceCarta);

        if (pinFromDB.equals("ERROR"))
           cardRejected();

        else if (!pin.equals(pinFromDB))
            pinRejected();

        // pin.equals(pinFromDB))
        else{
           loginSuccess(codiceCarta, getCodiceClienteFromCodiceCarta(codiceCarta));
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

    void loginSuccess(String codiceCarta, String codiceCliente) throws SQLException, DAOException {

        codiceClienteLabel.setText(codiceCarta);
        massimaliLabel.setText(getMassimaliFromCodiceCarta(codiceCarta));
        puntiFedeltaLabel.setText(getPuntiFedeltaFromCodiceCliente(codiceCliente));

        // updating login label
        messageLabel.setText("CARTA ACCETTATA!");
        messageLabel.setStyle("-fx-text-fill: rgb(0,255,43)");

        // updating login-state
        this.cardAccepted = true;

        // updating navigation button
        confirmButton.setText("AI REPARTI");
        codiceCartaLabel.setText(codiceCarta);
    }

    void resetForm(){
        codiceCartaTextField.setText("");
        pinPasswordField.setText("");
        codiceCartaLabel.setText("xxxx-xxxx-xxxx-xxxx");
        codiceClienteLabel.setText("");
        massimaliLabel.setText("");
        puntiFedeltaLabel.setText("");

        messageLabel.setText("Inserite la vostra carta...");
        messageLabel.setStyle("-fx-text-fill: rgb(0,255,43)");

        // also resets the card-state!
        cardAccepted = false;
    }

    // called by 'eject' button
    @FXML
    private void handleEject(){
        resetForm();
    }

    public String getPinFromCodiceCarta(String codiceCarta) throws DAOException {
        List<Carte> cardToBeChecked = CarteDAOMySQL.getInstance().select(new Carte(-1, codiceCarta));
        if (cardToBeChecked.size() == 1)
            return cardToBeChecked.get(0).getPin();
        else return "ERROR";
    }

    public String getCodiceClienteFromCodiceCarta(String codiceCarta) throws SQLException {

        Statement statement = DAOMySQLSettings.getStatement();

        String query = "SELECT clienti.codiceCliente " +
                "FROM Clienti JOIN Carte " +
                "ON Clienti.idCliente = Carte.idCliente " +
                "WHERE codiceCarta = '" + codiceCarta + "';";

        try{
            logger.info("SQL: " + query);
        }
        catch(NullPointerException nullPointerException){
            System.out.println("SQL: " + query);
        }

        ArrayList<String> result = new ArrayList<>();

        ResultSet rs = statement.executeQuery(query);

        while(rs.next()){
            result.add(rs.getString("codiceCliente"));
        }

        DAOMySQLSettings.closeStatement(statement);

        if (result.size() == 1) {
            App.getInstance().getCodiceCliente(result.get(0));
            return result.get(0);
        }

            // 1 - more than one client is correlated to the given card
            // OR
            // 2 - client not found (db error, something went wrong)
        else return "ERROR";
    }

    public String getMassimaliFromCodiceCarta(String codiceCarta) throws SQLException, DAOException {
        // select() function is codiceCarta-based
        List<Carte> card = CarteDAOMySQL.getInstance().select(new Carte(-1, codiceCarta));
        if( card.size() != 1)
            return "ERROR";
        else {
            App.getInstance().getMassimali(card.get(0).getMassimaleRimanente().toString() + " / " + card.get(0).getMassimaleRimanente().toString() + " €");
            return card.get(0).getMassimaleRimanente().toString() + " / " + card.get(0).getMassimaleRimanente().toString() + " €";
        }
    }

    public String getPuntiFedeltaFromCodiceCliente(String codiceCliente) throws SQLException, DAOException {
        // select() function is codiceCliente-based
        List<Clienti> customer = ClientiDAOMySQL.getInstance().select(new Clienti("","",codiceCliente));
        if( customer.size() != 1)
            return "ERROR";
        else {
            App.getInstance().getPuntiFedelta(customer.get(0).getPuntiFedelta().toString());
            return customer.get(0).getPuntiFedelta().toString();
        }
    }

}

