package it.unicas.supermarket.controller;

import it.unicas.supermarket.App;
import it.unicas.supermarket.Main;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.Util;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ReceiptController {

    private Stage dialogStage;

    @FXML private GridPane receiptArticleGridPane;
    @FXML private Label receiptTotalImportLabel;

    @FXML
    private TableColumn<List<String>, String> productsColumn;

    @FXML
    private void initialize() {
        try {
            fillReceiptGridPane(App.getInstance().getCartMap());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DAOException e) {
            e.printStackTrace();
        }

        receiptTotalImportLabel.setText(String.format("%.2f",OrderSummaryLayoutController.totalImport));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void handleLogin(){
        App.getInstance().ejectCardAfterPayment();
        dialogStage.close();
    }

    public void fillReceiptGridPane(HashMap<String, Integer> cartMap) throws IOException, DAOException {

        int column = 0;
        int row = 1;

        for (int index=0; index<cartMap.size(); index++) {
            if (App.getInstance().getCartListQuantity().get(index) > 0) {
                FXMLLoader itemLoader = new FXMLLoader();
                itemLoader.setLocation(Main.class.getResource("view/ReceiptArticleGridItem.fxml"));
                AnchorPane anchorPane = itemLoader.load();

                ReceiptArticleGridItemController receiptArticleGridItemController = itemLoader.getController();
                receiptArticleGridItemController.loadItem(
                        Util.getNomeArticoloFromBarcode(App.getInstance().getCartListArticles().get(index)),
                        App.getInstance().getCartListQuantity().get(index),
                        Util.getPrezzoArticoloFromBarcode(App.getInstance().getCartListArticles().get(index))
                );

                receiptArticleGridPane.add(anchorPane, column, row++); //(child,column,row)
                setGridLayout(anchorPane);
            }
        }
    }

    public void setGridLayout(AnchorPane anchorPane){
        // grid width
        receiptArticleGridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
        receiptArticleGridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        receiptArticleGridPane.setMaxWidth(Region.USE_PREF_SIZE);

        // grid height
        receiptArticleGridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
        receiptArticleGridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        receiptArticleGridPane.setMaxHeight(Region.USE_PREF_SIZE);

        // inset-margins
        GridPane.setMargin(anchorPane, new Insets(10, 10, 10, 10));
    }


}
