package it.unicas.supermarket.controller;
import it.unicas.supermarket.App;
import it.unicas.supermarket.Main;
import it.unicas.supermarket.model.Articoli;
import it.unicas.supermarket.model.Fruit;
import it.unicas.supermarket.model.dao.DAOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class OrderSummaryLayoutController implements Initializable {

    // cart section
    @FXML private ScrollPane cartArticleScrollPane;
    @FXML private GridPane cartArticleGridPane;

    private final List<Articoli> cartGridPaneArticles = new ArrayList<>();

    // payment section
    @FXML private Label codiceCartaLabel;
    @FXML private Label puntiFedeltaLabel;
    @FXML private Label codiceClienteLabel;
    @FXML private Label massimaliLabel;

    private ArticleSelectionListener articleSelectionListener;

    @FXML
    private void handleMarket() {
        App.getInstance().initMarketSectionLayout();
    }

    @FXML
    private void handlePayment() {
        System.out.println("Going to the Receipt section ...");
        App.getInstance().showReceipt();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadImages();

        try {
            fillCartGridPane(App.getInstance().getCartMap());
        } catch (IOException | DAOException e) {
            e.printStackTrace();
        }

        paymentDetails();
    }


    public void fillCartGridPane(HashMap<String, Integer> cartMap) throws IOException, DAOException {

        int column = 0;
        int row = 1;
/*
        ArrayList<Float> resultItem = new ArrayList<>();

        float articleQuantity = 0f;
        float cartImport = 0f;

        List<Integer> quantityList = App.getInstance().getCartListQuantity();
        List<String> barcodeList = App.getInstance().getCartListArticles();

        for (int i=0; i<App.getInstance().getCartMap().size(); i++){
            Integer quantity = quantityList.get(i);
            Float price = getPrezzoArticoloFromBarcode(barcodeList.get(i));
            articleQuantity += quantity.floatValue();
            cartImport += quantity*price;
        }

        resultItem.add(articleQuantity);
        resultItem.add(cartImport);
        */

        for (int index=0; index<cartMap.size(); index++) {

            FXMLLoader itemLoader = new FXMLLoader();
            itemLoader.setLocation(Main.class.getResource("view/CartArticleGridItem.fxml"));
            AnchorPane anchorPane = itemLoader.load();

            CartArticleGridItemController cartArticleGridItemController = itemLoader.getController();
            cartArticleGridItemController.loadItem(App.getInstance().getCartListArticles().get(index),
                    App.getInstance().getCartListQuantity().get(index), articleSelectionListener);

            row++;

            cartArticleGridPane.add(anchorPane, column++, row); //(child,column,row)
            setGridLayout(anchorPane);
        }
    }



    private void loadImages(){
        for (Articoli articoli : cartGridPaneArticles)
            articoli.setImageURL(Articoli.getURLfromCode(articoli.getBarcode()));
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

    private void paymentDetails() {
        codiceClienteLabel.setText(App.getInstance().getCodiceCliente());
        massimaliLabel.setText(App.getInstance().getMassimali());
        puntiFedeltaLabel.setText(App.getInstance().getPuntiFedelta());
        codiceCartaLabel.setText(App.getInstance().getCodiceCarta());
    }
}

