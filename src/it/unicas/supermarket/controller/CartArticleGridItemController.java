package it.unicas.supermarket.controller;
import it.unicas.supermarket.App;
import it.unicas.supermarket.model.Articoli;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.Util;
import it.unicas.supermarket.model.dao.mysql.ArticoliDAOMySQL;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

/**
 * Controller del singolo articolo presente nel Carrello
 */
public class CartArticleGridItemController {

    @FXML private Label cartArticleNameLabel;
    @FXML private Label cartArticlePriceLabel;
    @FXML private ImageView cartArticleImage;
    @FXML private Label cartQuantityLabel;

    private int scorteRimanenti;
    private int quantitaSelezionata;

    private Articoli articolo;

    /**
     * Il metodo carica il singolo articolo sul Carrello
     * @param barcode Codice dell'articolo da caricare nel Carrello
     * @param quantita Quantita' dell'articolo da caricare nel Carrello
     */
    public void loadItem(String barcode, Integer quantita) throws DAOException {

        // ottengo l'oggetto di tipo Articolo a partire dal barcode
        List<Articoli> result = ArticoliDAOMySQL.getInstance().select(new Articoli("", barcode));
        if (result.size()!=1){
            throw new DAOException("Trovato più di un articolo nella loadITem");
        }
        this.articolo = result.get(0);

        // aggiorno quantità e consequenziali scorte
        quantitaSelezionata = quantita;
        scorteRimanenti = Util.getScorteFromBarcode(this.articolo.getBarcode()) - quantitaSelezionata;

        // aggiorno le Label e l'immagine dell'articolo
        cartArticleNameLabel.setText(Util.getNomeArticoloFromBarcode(barcode));
        cartQuantityLabel.setText(String.valueOf(quantitaSelezionata));
        cartArticlePriceLabel.setText(Util.getPrezzoArticoloFromBarcode(barcode)+" €");
        Image image = new Image("file:" + Articoli.getURLfromCode(barcode));
        cartArticleImage.setImage(image);
    }

    /**
     * Il metodo incrementa la quantità dell'articolo selezionato e contestualmente aggiorna l'importo totale e il saldo previsto
     */
    @FXML public void handleCartIncrement() throws DAOException {
        // se non ho scorte, non posso incrementare
        if(scorteRimanenti<1)
            return;

        // incremento la quantità selezionata
        quantitaSelezionata++;
        // decremento le scorte rimanenti
        scorteRimanenti--;
        // aggiorno la struttura del carrello
        App.getInstance().getCartMap().replace(this.articolo.getBarcode(), quantitaSelezionata);
        // aggiorno la Label del singolo articolo
        cartQuantityLabel.setText(String.valueOf(quantitaSelezionata));
        // aggiorno il costo totale dell'ordine
        App.getInstance().getOrderSummaryLayoutController().updateTotalCartCost();
        // verifico la fattibilità del mio ordine
        App.getInstance().getOrderSummaryLayoutController().paymentCheck();
    }

    /**
     * Il metodo decrementa la quantità dell'articolo selezionato e contestualmente aggiorna l'importo totale e il saldo previsto
     */
    @FXML public void handleCartDecrement() throws DAOException {
        // se la quantità è nulla, non posso decrementare
        if(quantitaSelezionata < 1)
            return;

        // incremento la quantità selezionata
        quantitaSelezionata--;
        // decremento le scorte rimanenti
        scorteRimanenti++;
        // aggiorno la struttura del carrello
        App.getInstance().getCartMap().replace(this.articolo.getBarcode(), quantitaSelezionata);
        // aggiorno la Label del singolo articolo
        cartQuantityLabel.setText(String.valueOf(quantitaSelezionata));
        // aggiorno il costo totale dell'ordine
        App.getInstance().getOrderSummaryLayoutController().updateTotalCartCost();
        // verifico la fattibilità del mio ordine
        App.getInstance().getOrderSummaryLayoutController().paymentCheck();
    }

}
