package it.unicas.supermarket.controller;

import it.unicas.supermarket.App;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ReceiptController {

    private Stage dialogStage;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void handleLogin(){
        App.getInstance().ejectCardAfterPayment();
        dialogStage.close();
    }

}
