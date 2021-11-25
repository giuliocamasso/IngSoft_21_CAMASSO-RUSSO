package it.unicas.supermarket.view;

import it.unicas.supermarket.MainApp;
import javafx.fxml.FXML;


public class MarketSectionLayoutController {

    // Reference to the main application
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleExit() {

        mainApp.handleExit();

    }

    @FXML
    private void handleValidate() {
        mainApp.handleValidate();

    }

}