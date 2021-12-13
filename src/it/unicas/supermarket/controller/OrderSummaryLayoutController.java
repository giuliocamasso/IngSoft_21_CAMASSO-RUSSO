package it.unicas.supermarket.controller;
import it.unicas.supermarket.App;
import it.unicas.supermarket.Main;
import it.unicas.supermarket.model.Articoli;
import it.unicas.supermarket.model.Fruit;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.mysql.ArticoliDAOMySQL;
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

import static it.unicas.supermarket.controller.MarketSectionLayoutController.getPrezzoArticoloFromBarcode;

public class OrderSummaryLayoutController implements Initializable {

    // cart section
    @FXML private ScrollPane cartArticleScrollPane;
    @FXML private GridPane cartArticleGridPane;

    @FXML private static Label totalCostLabel;

    static float totalImport = 0f;

    // payment section
    @FXML private Label codiceCartaLabel;
    @FXML private Label puntiFedeltaLabel;
    @FXML private Label codiceClienteLabel;
    @FXML private Label massimaliLabel;

    private ArticleSelectionListener articleSelectionListener;

    public float getTotalImport() { return this.totalImport; }

    public static void setTotalImport(float tot) {
        totalImport = tot;
        totalCostLabel.setText("€ "+totalImport);
    }

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

        try {
            fillCartGridPane(App.getInstance().getCartMap());
        } catch (IOException | DAOException e) {
            e.printStackTrace();
        }

        try {
            updateTotalCartCost();
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
            cartArticleGridItemController.loadItem(App.getInstance().getCartListArticles().get(index),
                    App.getInstance().getCartListQuantity().get(index), articleSelectionListener);

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

    public static void updateTotalCartCost() throws DAOException {

        List<Integer> quantityList = App.getInstance().getCartListQuantity();
        List<String> barcodeList = App.getInstance().getCartListArticles();

        Float sum = 0f;

        for (int i=0; i<App.getInstance().getCartMap().size(); i++){
            Integer quantity = quantityList.get(i);
            Float price = getPrezzoArticoloFromBarcode(barcodeList.get(i));
            sum += quantity*price;
        }

        setTotalImport(sum);
    }

    public static int handleDecrement(String barcode) throws DAOException {
        System.out.println("Decrement Cart Pressed");

        int oldCartQuantity = App.getInstance().getCartListQuantityFromKey(barcode);
        int newCartQuantity = oldCartQuantity--;
        System.out.println("new "+newCartQuantity);
        System.out.println("old "+oldCartQuantity);
        System.out.println("max "+getScorteFromBarcode(barcode));
        System.out.println("ID "+barcode);

        // chosen quantity must be > 0
        if(newCartQuantity >= 0) {
            App.getInstance().getCartMap().replace(barcode, oldCartQuantity, newCartQuantity);
        }
        return 1;
    }

    public static int getScorteFromBarcode(String barcode) throws DAOException {
        // select() function is barcode-based
        List<Articoli> article = ArticoliDAOMySQL.getInstance().select(new Articoli("", barcode));
        if( article.size() != 1)
            return -1;
        else
            return article.get(0).getScorteMagazzino();
    }

    private void paymentDetails() {
        codiceClienteLabel.setText(App.getInstance().getCodiceCliente());
        massimaliLabel.setText(App.getInstance().getMassimali());
        puntiFedeltaLabel.setText(App.getInstance().getPuntiFedelta());
        codiceCartaLabel.setText(App.getInstance().getCodiceCarta());
    }
}

