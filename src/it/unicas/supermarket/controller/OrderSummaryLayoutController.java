package it.unicas.supermarket.controller;
import it.unicas.supermarket.App;
import it.unicas.supermarket.Main;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller della scena associata al carrello e ai dettagli pagamento per finalizzare l'ordine
 */
public class OrderSummaryLayoutController implements Initializable {

    // sezione carrello
    @FXML private ScrollPane cartArticleScrollPane;
    @FXML private GridPane cartArticleGridPane;
    @FXML private Label totalCostLabel;

    static float totalImport = 0f;

    // sezione dettagli pagamento
    @FXML private Label codiceCartaLabel;
    @FXML private Label puntiFedeltaLabel;
    @FXML private Label codiceClienteLabel;
    @FXML private Label massimaliLabel;

    @FXML private Label paymentCheckLabel;

    /**
     * Inizializza i prodotti nel carrello, il totale, e il possibile esito di pagamento
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        App.getInstance().setOrderSummaryVisited(true);

        // riempio il carrello con i prodotti correnti
        try {
            fillCartGridPane(App.getInstance().getCartMap());
        } catch (IOException | DAOException e) {
            e.printStackTrace();
        }

        // inizializzazione della label e della variabile associata al costo totale dell'ordine
        try {
            updateTotalCartCost();
        } catch (DAOException e) {
            e.printStackTrace();
        }

        // aggiornamento dell'esito di pagamento
        try {
            paymentCheck();
        } catch (DAOException e) {
            e.printStackTrace();
        }

        // i dati di pagamento del cliente vengono mostrati
        paymentDetails();
    }

    // setter grid layout
    public void setGridLayout(AnchorPane anchorPane){
        // grid width
        cartArticleGridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
        cartArticleGridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        cartArticleGridPane.setMaxWidth(Region.USE_PREF_SIZE);

        // grid height
        cartArticleGridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
        cartArticleGridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        cartArticleGridPane.setMaxHeight(Region.USE_PREF_SIZE);

        // inset-margins
        GridPane.setMargin(anchorPane, new Insets(10, 10, 10, 10));
    }

    /**
     * Il metodo riempie la Grid Pane nel riepilogo dell'ordine "Carrello"
     * @param cartMap HashMap del carrello
     */
    public void fillCartGridPane(HashMap<String, Integer> cartMap) throws IOException, DAOException {

        int column = 0;
        int row = 1;

        // carico i singoli articoli nella grid pane
        for (int index=0; index<cartMap.size(); index++) {

            FXMLLoader itemLoader = new FXMLLoader();
            itemLoader.setLocation(Main.class.getResource("view/CartArticleGridItem.fxml"));
            AnchorPane anchorPane = itemLoader.load();

            CartArticleGridItemController cartArticleGridItemController = itemLoader.getController();
            cartArticleGridItemController.loadItem(App.getInstance().getCartListArticles().get(index), App.getInstance().getCartListQuantity().get(index));

            cartArticleGridPane.add(anchorPane, column, row++);
            setGridLayout(anchorPane);
        }
    }

    /**
     * <p>
     *      Il metodo associato al bottone "Torna ai reparti" riporta alla sezione dei reparti <br>
     *      per aggiungere nuovi prodotti al carrello
     * </p>
     */
    @FXML
    private void handleMarket() {
        App.getInstance().backToSectionsFromPayment();
    }

    /**
     * <p>
     *     Il metodo porta a termine il pagamento mostrando lo scontrino dell'ordine effettuato <br>
     *     Se il carrello e' vuoto non posso pagare per nessun prodotto <br>
     *     Se il carrello non e' vuoto e ho abbastanza massimale, posso pagare con "Paga Ora" <br>
     *     Se il carrello non e' vuoto ma non ho abbastanza massimale, non posso pagare
     * </p>
     */
    @FXML
    private void handlePayment() throws DAOException {
        // il pagamento è abilitato solo se l'importo è maggiore di zero e se il massimale rimanente è sufficiente
        if(totalImport > 0f && totalImport <= App.getInstance().getMassimaleRimanente()) {
            // aggiornamneto massimale e punti fedeltà
            App.getInstance().setMassimaleRimanente(App.getInstance().getMassimaleRimanente()-totalImport);
            App.getInstance().updatePuntiFedelta(App.getInstance().computeFidelity(totalImport));

            // i dati del cliente vengono salvati se necessiterà di effettuare un'altra spesa dopo aver visto
            // lo scontrino, senza bisogno di fare l'eject della carta
            Util.updateClienteAfterPayment(
                    App.getInstance().getCodiceCarta(),
                    App.getInstance().getCodiceCliente(),
                    App.getInstance().getMassimaleRimanente(),
                    App.getInstance().getPuntiFedelta()
            );

            // mostro lo scontrino
            App.getInstance().showReceipt();

            // invio l'ordine effettuato al database
            try {
                Util.sendOrderToDB(totalImport);
                App.getInstance().getCartMap().clear();
                App.getInstance().getLoginLayoutController().setCardAccepted(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Quando chiamato, il metodo aggiorna l'importo totale e la relativa Label al variare dei prodotti nel carrello
     */
    public void updateTotalCartCost() throws DAOException {

        List<Integer> quantityList = App.getInstance().getCartListQuantity();
        List<String> barcodeList = App.getInstance().getCartListArticles();

        float sum = 0f;

        // calcolo l'importo totale
        for (int i=0; i<App.getInstance().getCartMap().size(); i++){
            Integer quantity = quantityList.get(i);
            Float price = Util.getPrezzoArticoloFromBarcode(barcodeList.get(i));
            sum += quantity*price;
        }

        // aggiorno la variabile di importo e la relativa Label
        totalImport = sum;
        totalCostLabel.setText("€ "+ String.format("%.2f",totalImport));
    }

    /**
     * <p>
     *     Il metodo mostra lo status di pagamento possibile in base ai prodotti nel carrello e al massimale <br>
     *     Carrello vuoto: non ci sono prodotti nel carrello <br>
     *     Massimale Sufficiente + Saldo Previsto: se ho abbastanza massimale, posso vedere il futuro saldo <br>
     *     Massimale Insufficiente: se non ho abbastanza massimale, il cliente viene informato
     * </p>
     */
    public void paymentCheck() throws DAOException {
        // se l'importo è zero, il carrello è vuoto
        if (totalImport == 0f){
            paymentCheckLabel.setText("Carrello vuoto");
            paymentCheckLabel.setStyle("-fx-text-fill: rgb(255,255,0)");
        }
        // se ho abbastanza massimale, posso pagare e vedere il futuro massimale dopo l'eventuale pagamento
        else if (totalImport <= App.getInstance().getMassimaleRimanente()){
            paymentCheckLabel.setText("Massimale Sufficiente\n" + "Saldo previsto: " + String.format("%.2f", (App.getInstance().getMassimaleRimanente()-totalImport)) + " €");
            paymentCheckLabel.setStyle("-fx-text-fill: rgb(0,255,0)");
        }
        // se non ho abbastanza massimale, un avviso è mostrato
        else {
            paymentCheckLabel.setText("Massimale Insufficiente");
            paymentCheckLabel.setStyle("-fx-text-fill: rgb(255,0,0)");
        }
    }

    /**
     * Il metodo mostra i dettagli del cliente e della carta nella sezione "Dettagli Pagamento"
     */
    private void paymentDetails() {
        codiceClienteLabel.setText(App.getInstance().getCodiceCliente());
        massimaliLabel.setText(App.getInstance().getMassimaliString());
        puntiFedeltaLabel.setText(String.valueOf(App.getInstance().getPuntiFedelta()));
        codiceCartaLabel.setText(App.getInstance().getCodiceCarta());
    }

    /**
     * Il metodo e' chiamato per settare gli elementi del carrello e dei dettagli pagamento
     */
    public void syncProducts() throws DAOException, IOException {
        // pulisco il carrello
        clearGridItems();
        // riempio il carrello con i nuovi articoli
        fillCartGridPane(App.getInstance().getCartMap());
        // aggiorno le Label di costo totale
        updateTotalCartCost();
        // aggiorno i dettagli pagamento
        paymentDetails();
        // verifico la fattibilità del mio ordine
        paymentCheck();
    }

    /**
     * Il metodo elimina tutti gli articoli presenti nella grid pane del carrello
     */
    private void clearGridItems() {
        cartArticleGridPane.getChildren().clear();
    }

    /**
     * Il metodo porta alla rimozione della carta dell'utente
     */
    @FXML
    public void handleLogout(){
        App.getInstance().logout();
    }

}

