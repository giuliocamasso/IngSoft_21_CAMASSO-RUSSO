package it.unicas.supermarket.controller;

import it.unicas.supermarket.ArticleSelectionListener;
import it.unicas.supermarket.MainSample;
import it.unicas.supermarket.model.Fruit;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ArticleGridItemController {
    @FXML
    private Label articleNameLabel;

    @FXML
    private Label articlePriceLabel;

    @FXML
    private ImageView articleImage;

    @FXML
    private void click(MouseEvent mouseEvent) {
        articleSelectionListener.onClickListener(fruit);
    }

    private Fruit fruit;
    private ArticleSelectionListener articleSelectionListener;

    public void setData(Fruit fruit, ArticleSelectionListener articleSelectionListener) {
        this.fruit = fruit;
        this.articleSelectionListener = articleSelectionListener;
        articleNameLabel.setText(fruit.getName());
        articlePriceLabel.setText(MainSample.CURRENCY + fruit.getPrice());
        Image image = new Image("file:"+fruit.getImgSrc());
        articleImage.setImage(image);
    }
}
