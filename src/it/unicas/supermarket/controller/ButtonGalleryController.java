package it.unicas.supermarket.controller;
import it.unicas.supermarket.App;
import it.unicas.supermarket.Main;
import javafx.fxml.FXML;


public class ButtonGalleryController {

    @FXML
    private void handleExit() {

       App.getInstance().handleExit();

    }

    @FXML
    private void handleValidate() {
        //App.getInstance().handleValidate();

    }

}