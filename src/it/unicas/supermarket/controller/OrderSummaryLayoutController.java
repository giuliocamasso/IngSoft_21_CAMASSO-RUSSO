package it.unicas.supermarket.controller;
import it.unicas.supermarket.App;
import it.unicas.supermarket.Main;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class OrderSummaryLayoutController implements Initializable {

    // cart section
    @FXML private ScrollPane cartArticleScrollPane;
    @FXML private GridPane cartArticleGridPane;

    @FXML private Label totalCostLabel;

    static float totalImport = 0f;

    // payment section
    @FXML private Label codiceCartaLabel;
    @FXML private Label puntiFedeltaLabel;
    @FXML private Label codiceClienteLabel;
    @FXML private Label massimaliLabel;

    @FXML private Label paymentCheckLabel;

    private ArticleSelectionListener articleSelectionListener;

    public float getTotalImport() { return totalImport; }

    @FXML
    private void handleMarket() {
        App.getInstance().backToSectionsFromPayment();
    }

    @FXML
    private void handlePayment() {
        System.out.println("Going to the Receipt section ...");

        if(totalImport > 0f && totalImport <= App.getInstance().getMassimaleRimanente()) {
            App.getInstance().setMassimaleRimanente(App.getInstance().getMassimaleRimanente()-totalImport);
            System.out.println("Massimale Rimanente nuovo: " + (App.getInstance().getMassimaleRimanente()-totalImport));
            App.getInstance().showReceipt();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            fillCartGridPane(App.getInstance().getCartMap());
        } catch (IOException | DAOException e) {
            e.printStackTrace();
        }

        try
        {
            updateTotalCartCost();
            totalCostLabel.setText("€ "+totalImport);
        } catch (DAOException e) {
            e.printStackTrace();
        }

        try {
            paymentCheck();
        } catch (DAOException e) {
            e.printStackTrace();
        }

        paymentDetails();
    }

    public void fillCartGridPane(HashMap<String, Integer> cartMap) throws IOException, DAOException {

        int column = 0;
        int row = 1;

        for (int index=0; index<cartMap.size(); index++) {

            FXMLLoader itemLoader = new FXMLLoader();
            itemLoader.setLocation(Main.class.getResource("view/CartArticleGridItem.fxml"));
            AnchorPane anchorPane = itemLoader.load();

            CartArticleGridItemController cartArticleGridItemController = itemLoader.getController();
            cartArticleGridItemController.loadItem(App.getInstance().getCartListArticles().get(index), App.getInstance().getCartListQuantity().get(index));

            cartArticleGridPane.add(anchorPane, column, row++); //(child,column,row)
            setGridLayout(anchorPane);
        }
    }

    public void setGridLayout(AnchorPane anchorPane){
        // grid width
        cartArticleGridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
        cartArticleGridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        cartArticleGridPane.setMaxWidth(Region.USE_PREF_SIZE);

        // grid height
        cartArticleGridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
        cartArticleGridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        cartArticleGridPane.setMaxHeight(Region.USE_PREF_SIZE);

        // inset-margins
        GridPane.setMargin(anchorPane, new Insets(10, 10, 10, 10));
    }

    public void updateTotalCartCost() throws DAOException {

        List<Integer> quantityList = App.getInstance().getCartListQuantity();
        List<String> barcodeList = App.getInstance().getCartListArticles();

        totalImport = 0f;

        for (int i=0; i<App.getInstance().getCartMap().size(); i++){
            Integer quantity = quantityList.get(i);
            Float price = Util.getPrezzoArticoloFromBarcode(barcodeList.get(i));
            totalImport += quantity*price;
        }

        totalCostLabel.setText("€ "+totalImport);
        System.out.println("Sum from updateTotale " + totalImport);

    }

    public void paymentCheck() throws DAOException {
        if (totalImport == 0f){
            paymentCheckLabel.setText("Carrello vuoto");
            paymentCheckLabel.setStyle("-fx-text-fill: rgb(255,255,0)");
        }
        else if (totalImport <= App.getInstance().getMassimaleRimanente()){
            paymentCheckLabel.setText("Massimale Sufficiente");
            paymentCheckLabel.setStyle("-fx-text-fill: rgb(0,255,0)");
        }
        else {
            paymentCheckLabel.setText("Massimale Insufficiente");
            paymentCheckLabel.setStyle("-fx-text-fill: rgb(255,0,0)");
        }
    }

    private void paymentDetails() {
        codiceClienteLabel.setText(App.getInstance().getCodiceCliente());
        massimaliLabel.setText(App.getInstance().getMassimaliString());
        puntiFedeltaLabel.setText(String.valueOf(App.getInstance().getPuntiFedelta()));
        codiceCartaLabel.setText(App.getInstance().getCodiceCarta());
    }
}

