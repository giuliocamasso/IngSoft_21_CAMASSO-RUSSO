package it.unicas.supermarket.controller;
import it.unicas.supermarket.model.dao.DAOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller dell'articolo da inserire all'interno dello scontrino
 */
public class ReceiptArticleGridItemController {

    @FXML private Label receiptArticleLabel;
    @FXML private Label receiptCostLabel;

    /**
     * Il metodo carica il singolo articolo sullo scontrino ad ordine finalizzato
     * @param nome Nome dell'articolo da caricare sullo scontrino
     * @param quantita Quantita dell'articolo da caricare sullo scontrino
     * @param prezzo Prezzo dell'articolo da caricare sullo scontrino
     */
    public void loadItem(String nome, Integer quantita, Float prezzo) throws DAOException {
        receiptArticleLabel.setText(nome);
        receiptCostLabel.setText(quantita + "x   " + prezzo);
    }
}
