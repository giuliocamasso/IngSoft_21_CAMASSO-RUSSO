package it.unicas.supermarket.controller;

import it.unicas.supermarket.App;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ReceiptController {

    private boolean backToLogin = false;

    private Stage dialogStage;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isBackClicked() {
        return backToLogin;
    }

    @FXML
    public void handleLogin(){
        App.getInstance().initLoginLayout();
        backToLogin = true;
        dialogStage.close();
    }

}
