package it.unicas.supermarket.controller;

import it.unicas.supermarket.model.Articoli;
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


    public void setData(Articoli article, ArticleSelectionListener articleSelectionListener) {

        cartArticleNameLabel.setText(article.getNome());
        cartArticlePriceLabel.setText(article.getPrezzo()+" €");
        Image image = new Image("file:"+article.getImageURL());
        cartArticleImage.setImage(image);

    }

}
