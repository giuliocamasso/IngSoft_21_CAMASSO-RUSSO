package it.unicas.supermarket.controller;

import it.unicas.supermarket.model.Articoli;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Il pannello principale della scena dei reparti, e' costituito da un grid pane inserito all'interno di uno scroll pane<br>
 * Ciascun item della grid pane (il quadrato contenente icona nome e prezzo dell'articolo) ha un controller associato
 * Il controller consente di caricare gli item all'interno della griglia,
 * e di visualizzare i dettagli degli stessi sul pannello laterale sinistro con un click (con l'ArticleSelectionlistener)
 */
public class ArticleGridItemController {
    // l'articolo associato all'item
    private Articoli articolo;

    // grafica
    @FXML private Label articleNameLabel;
    @FXML private Label articlePriceLabel;
    @FXML private ImageView articleImage;

    // listener
    private ArticleSelectionListener articleSelectionListener;

    // On Mouse Pressed action
    @FXML private void click(MouseEvent mouseEvent) {
        articleSelectionListener.onClickListener(articolo);
    }

    /**
     * Il metodo e' chiamato per riempire la grid pane della sezione reparti<br>
     * In seguito a una query (filtraggi per reparto o nome) deve essere mostrato un certo elenco di articoli,
     * e il metodo loadItem viene chiamato per ciascuno di essi
     * @param articolo L'articolo associato al grid item
     * @param articleSelectionListener Il listener che consente di visualizzare i dettagli
     *                                dell'articolo sul pannello laterale sinistro cliccando su di essi
     */
    public void loadItem(Articoli articolo, ArticleSelectionListener articleSelectionListener) {

        this.articolo = articolo;
        this.articleSelectionListener = articleSelectionListener;

        // label settings
        articleNameLabel.setText(articolo.getNome());
        articlePriceLabel.setText( String.format("%.2f",articolo.getPrezzo()) +" €");

        // caricamento immagine
        Image image = new Image("file:"+articolo.getImageURL());
        articleImage.setImage(image);
    }
}
