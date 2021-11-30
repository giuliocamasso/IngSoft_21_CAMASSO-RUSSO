package it.unicas.supermarket.controller;

import it.unicas.supermarket.App;
import javafx.fxml.FXML;

public class OrderSummaryLayoutController {

    @FXML
    private void handleMarket(){
        App.getInstance().initMarketSectionLayout();
    }

    @FXML
    private void handleShowPayment() {
        System.out.println("Going to the Receipt section ...");
        App.getInstance().showReceipt();
    }
}
