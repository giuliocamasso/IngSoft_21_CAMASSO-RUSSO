package it.unicas.supermarket.controller;

import it.unicas.supermarket.model.Articoli;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ArticleGridItemController {
    @FXML private Label articleNameLabel;
    @FXML private Label articlePriceLabel;
    @FXML private ImageView articleImage;

    @FXML private void click(MouseEvent mouseEvent) {
        articleSelectionListener.onClickListener(articolo);
    }

    private Articoli articolo;
    private ArticleSelectionListener articleSelectionListener;

    public void loadItem(Articoli articolo, ArticleSelectionListener articleSelectionListener) {
        System.out.println("load");
        this.articolo = articolo;
        this.articleSelectionListener = articleSelectionListener;
        articleNameLabel.setText(articolo.getNome());
        articlePriceLabel.setText(articolo.getPrezzo() +" €");
        Image image = new Image("file:"+articolo.getImageURL());
        articleImage.setImage(image);
    }
}
