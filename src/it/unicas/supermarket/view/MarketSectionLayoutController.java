package it.unicas.supermarket.view;
import it.unicas.supermarket.App;
import javafx.fxml.FXML;

public class MarketSectionLayoutController {

    @FXML
    public void handleChart() {
        System.out.println("Going to chart section...");
        App.getInstance().initOrderSummaryLayout();
    }
}
