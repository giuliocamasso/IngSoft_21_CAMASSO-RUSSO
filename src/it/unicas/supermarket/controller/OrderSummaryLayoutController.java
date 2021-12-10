package it.unicas.supermarket.controller;
import it.unicas.supermarket.App;
import it.unicas.supermarket.ArticleSelectionListener;
import it.unicas.supermarket.Main;
import it.unicas.supermarket.model.Fruit;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrderSummaryLayoutController implements Initializable {

    @FXML
    private VBox chartArticleDetails;

    @FXML
    private Label chartArticleNameLabel;

    @FXML
    private Label chartArticlePriceLabel;

    @FXML
    private ImageView chartArticleImage;

    @FXML
    private ScrollPane chartArticleScrollPane;

    @FXML
    private GridPane chartArticleGridPane;

    private List<Fruit> fruits = new ArrayList<>();
    private Image image;
    private ArticleSelectionListener articleSelectionListener;

    @FXML
    private void handleMarket(){
        App.getInstance().initMarketSectionLayout();
    }

    @FXML
    private void handlePayment() {
        System.out.println("Going to the Receipt section ...");
        App.getInstance().showReceipt();
    }

    private List<Fruit> getData() {
        List<Fruit> fruits = new ArrayList<>();
        Fruit fruit;
        fruit = new Fruit();
        fruit.setName("Kiwi");
        fruit.setPrice(2.99);
        fruit.setImgSrc("resources/images/fruits/kiwi.png");
        fruit.setColor("6A7324");
        fruits.add(fruit);

        fruit = new Fruit();
        fruit.setName("Coconut");
        fruit.setPrice(3.99);
        fruit.setImgSrc("resources/images/fruits/coconut.png");
        fruit.setColor("A7745B");
        fruits.add(fruit);

        fruit = new Fruit();
        fruit.setName("Peach");
        fruit.setPrice(1.50);

        fruit.setImgSrc("resources/images/fruits/peach.png");
        fruit.setColor("F16C31");
        fruits.add(fruit);

        fruit = new Fruit();
        fruit.setName("Grapes");
        fruit.setPrice(0.99);
        fruit.setImgSrc("resources/images/fruits/grapes.png");
        fruit.setColor("291D36");
        fruits.add(fruit);

        fruit = new Fruit();
        fruit.setName("Watermelon");
        fruit.setPrice(4.99);
        fruit.setImgSrc("resources/images/fruits/watermelon.png");
        fruit.setColor("22371D");
        fruits.add(fruit);

        fruit = new Fruit();
        fruit.setName("Orange");
        fruit.setPrice(2.99);
        fruit.setImgSrc("resources/images/fruits/orange.png");
        fruit.setColor("FB5D03");
        fruits.add(fruit);

        fruit = new Fruit();
        fruit.setName("StrawBerry");
        fruit.setPrice(0.99);
        fruit.setImgSrc("resources/images/fruits/strawberry.png");
        fruit.setColor("80080C");
        fruits.add(fruit);

        fruit = new Fruit();
        fruit.setName("Mango");
        fruit.setPrice(0.99);
        fruit.setImgSrc("resources/images/fruits/mango.png");
        fruit.setColor("FFB605");
        fruits.add(fruit);

        fruit = new Fruit();
        fruit.setName("Cherry");
        fruit.setPrice(0.99);
        fruit.setImgSrc("resources/images/fruits/cherry.png");
        fruit.setColor("5F060E");
        fruits.add(fruit);

        fruit = new Fruit();
        fruit.setName("Banana");
        fruit.setPrice(1.99);
        fruit.setImgSrc("resources/images/fruits/banana.png");
        fruit.setColor("E7C00F");
        fruits.add(fruit);

        return fruits;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fruits.addAll(getData());
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < fruits.size(); i++) {
                FXMLLoader itemLoader = new FXMLLoader();
                itemLoader.setLocation(Main.class.getResource("view/ChartArticleGridItem.fxml"));
                AnchorPane anchorPane = itemLoader.load();
                ChartArticleGridItemController chartArticleGridItemController = itemLoader.getController();

                chartArticleGridItemController.setData(fruits.get(i), articleSelectionListener);

                chartArticleGridPane.add(anchorPane, column, row++); //(child,column,row)

                //set grid width
                chartArticleGridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                chartArticleGridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                chartArticleGridPane.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                chartArticleGridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                chartArticleGridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                chartArticleGridPane.setMaxHeight(Region.USE_PREF_SIZE);

                // margini all'interno del grid pane
                GridPane.setMargin(anchorPane, new Insets(10, 10, 10 ,10));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
