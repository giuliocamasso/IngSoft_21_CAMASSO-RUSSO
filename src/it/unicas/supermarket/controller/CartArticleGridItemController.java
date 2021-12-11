package it.unicas.supermarket.controller;

import it.unicas.supermarket.ArticleSelectionListener;
import it.unicas.supermarket.model.Fruit;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CartArticleGridItemController {
    @FXML
    private Label cartArticleNameLabel;

    @FXML
    private Label cartArticlePriceLabel;

    @FXML
    private ImageView cartArticleImage;

    private Fruit fruit;
    private ArticleSelectionListener articleSelectionListener;

    public void setData(Fruit fruit, ArticleSelectionListener articleSelectionListener) {
        this.fruit = fruit;
        this.articleSelectionListener = articleSelectionListener;
        cartArticleNameLabel.setText(fruit.getName());
        cartArticlePriceLabel.setText(fruit.getPrice());
        Image image = new Image("file:"+fruit.getImgSrc());
        cartArticleImage.setImage(image);
    }

}
