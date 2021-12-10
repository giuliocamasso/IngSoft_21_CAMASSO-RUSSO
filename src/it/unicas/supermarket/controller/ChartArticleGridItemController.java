package it.unicas.supermarket.controller;

import it.unicas.supermarket.model.Fruit;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChartArticleGridItemController {
    @FXML
    private Label chartArticleNameLabel;

    @FXML
    private Label chartArticlePriceLabel;

    @FXML
    private ImageView chartArticleImage;

    private Fruit fruit;
    private ArticleSelectionListener articleSelectionListener;

    public void setData(Fruit fruit, ArticleSelectionListener articleSelectionListener) {
        this.fruit = fruit;
        this.articleSelectionListener = articleSelectionListener;
        chartArticleNameLabel.setText(fruit.getName());
        chartArticlePriceLabel.setText(fruit.getPrice());
        Image image = new Image("file:"+fruit.getImgSrc());
        chartArticleImage.setImage(image);
    }

}
