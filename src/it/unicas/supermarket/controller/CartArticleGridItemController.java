package it.unicas.supermarket.controller;

import it.unicas.supermarket.App;
import it.unicas.supermarket.model.Articoli;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.Util;
import it.unicas.supermarket.model.dao.mysql.ArticoliDAOMySQL;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class CartArticleGridItemController {

    @FXML private Label cartArticleNameLabel;
    @FXML private Label cartArticlePriceLabel;
    @FXML private ImageView cartArticleImage;
    @FXML private Label cartQuantityLabel;

    private int scorteRimanenti;
    private int quantita;

    private Articoli articolo;

    public void loadItem(String barcode, Integer quantity) throws DAOException {

        System.out.println("load");
        List<Articoli> result = ArticoliDAOMySQL.getInstance().select(new Articoli("", barcode));
        if (result.size()!=1){
            throw new DAOException("Trovato più di un articolo nella loadITem");
        }
        this.articolo = result.get(0);

        quantita = quantity;
        scorteRimanenti = Util.getScorteFromBarcode(this.articolo.getBarcode()) - quantita;

        cartArticleNameLabel.setText(Util.getNomeArticoloFromBarcode(barcode));
        cartQuantityLabel.setText(String.valueOf(quantita));
        cartArticlePriceLabel.setText(Util.getPrezzoArticoloFromBarcode(barcode)+" €");
        Image image = new Image("file:" + Articoli.getURLfromCode(barcode));
        cartArticleImage.setImage(image);
    }

    // Add to cart buttons section
    @FXML public void handleCartIncrement() throws DAOException {

        if(scorteRimanenti<1)
            return;

        scorteRimanenti--;
        quantita++;
        App.getInstance().getCartMap().replace(this.articolo.getBarcode(), quantita);
        cartQuantityLabel.setText(String.valueOf(quantita));

        App.getInstance().getOrderSummaryLayoutController().updateTotalCartCost();
        App.getInstance().getOrderSummaryLayoutController().paymentCheck();
    }

    @FXML public void handleCartDecrement() throws DAOException {
        if(quantita < 1)
            return;

        scorteRimanenti++;
        quantita--;
        App.getInstance().getCartMap().replace(this.articolo.getBarcode(), quantita);
        cartQuantityLabel.setText(String.valueOf(quantita));
        App.getInstance().getOrderSummaryLayoutController().updateTotalCartCost();
        App.getInstance().getOrderSummaryLayoutController().paymentCheck();
    }

}
