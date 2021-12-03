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

public class MarketSectionLayoutController implements Initializable {

    @FXML
    private VBox articleDetails;

    @FXML
    private Label articleNameLabel;

    @FXML
    private Label articlePriceLabel;

    @FXML
    private ImageView articleImage;

    @FXML
    private ScrollPane articleScrollPane;

    @FXML
    private GridPane articleGridPane;

    private List<Fruit> fruits = new ArrayList<>();
    private Image image;
    private ArticleSelectionListener articleSelectionListener;

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

    private void setChosenFruit(Fruit fruit) {
        articleNameLabel.setText(fruit.getName());
        articlePriceLabel.setText(fruit.getPrice());
        image = new Image("file:"+fruit.getImgSrc());
        articleImage.setImage(image);
        /*
        articleDetails.setStyle("-fx-background-color: #" + fruit.getColor() + ";\n" +
                "    -fx-background-radius: 30;");
        */
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fruits.addAll(getData());
        if (fruits.size() > 0) {
            setChosenFruit(fruits.get(0));
            articleSelectionListener = fruit -> setChosenFruit(fruit);
        }
        int column = 0;
        int row = 0;

        try {
            for (int i = 0; i < fruits.size(); i++) {
                FXMLLoader itemLoader = new FXMLLoader();
                itemLoader.setLocation(Main.class.getResource("view/ArticleGridItem.fxml"));
                AnchorPane anchorPane = itemLoader.load();
                ArticleGridItemController articleGridItemController = itemLoader.getController();

                articleGridItemController.setData(fruits.get(i), articleSelectionListener);

                if (column == 4) {
                    column = 0;
                    row++;
                }

                articleGridPane.add(anchorPane, column++, row); //(child,column,row)

                //set grid width
                articleGridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                articleGridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                articleGridPane.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                articleGridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                articleGridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                articleGridPane.setMaxHeight(Region.USE_PREF_SIZE);

                // margini all'interno del grid pane
                GridPane.setMargin(anchorPane, new Insets(0));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleChart() {
        System.out.println("Going to Chart section...");
        App.getInstance().initOrderSummaryLayout();
    }
}
