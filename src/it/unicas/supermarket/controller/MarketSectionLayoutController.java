package it.unicas.supermarket.controller;
import it.unicas.supermarket.App;
import javafx.fxml.FXML;

public class MarketSectionLayoutController {

    @FXML
    public void handleChart() {
        System.out.println("Going to Chart section...");
        App.getInstance().initOrderSummaryLayout();
    }
}
