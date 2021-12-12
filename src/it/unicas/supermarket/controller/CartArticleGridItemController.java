package it.unicas.supermarket.controller;

import it.unicas.supermarket.App;
import it.unicas.supermarket.model.Articoli;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.mysql.ArticoliDAOMySQL;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

import static it.unicas.supermarket.controller.MarketSectionLayoutController.getNomeArticoloFromBarcode;
import static it.unicas.supermarket.controller.MarketSectionLayoutController.getPrezzoArticoloFromBarcode;

public class CartArticleGridItemController {

    @FXML private Label cartArticleNameLabel;
    @FXML private Label cartArticlePriceLabel;
    @FXML private ImageView cartArticleImage;
    @FXML private Label cartQuantityLabel;

    private Articoli articolo;
    private ArticleSelectionListener articleSelectionListener;


    public void loadItem(String barcode, Integer quantity, ArticleSelectionListener articleSelectionListener) throws DAOException {
        System.out.println("load");
        List<Articoli> result = ArticoliDAOMySQL.getInstance().select(new Articoli("", barcode));
        if (result.size()!=1){
            throw new DAOException("Trovato più di un articolo nella loadITem");
        }
        this.articolo = result.get(0);
        this.articleSelectionListener = articleSelectionListener;

        cartArticleNameLabel.setText(getNomeArticoloFromBarcode(barcode));
        cartQuantityLabel.setText(String.valueOf(quantity));
        cartArticlePriceLabel.setText(getPrezzoArticoloFromBarcode(barcode)+" €");
        Image image = new Image("file:" + articolo.getImageURL());
        cartArticleImage.setImage(image);
    }

}
