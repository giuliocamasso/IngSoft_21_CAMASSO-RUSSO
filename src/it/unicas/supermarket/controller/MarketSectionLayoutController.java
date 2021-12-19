package it.unicas.supermarket.controller;
import it.unicas.supermarket.App;
import it.unicas.supermarket.Main;
import it.unicas.supermarket.model.Articoli;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.Util;
import it.unicas.supermarket.model.dao.mysql.ArticoliDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.DAOMySQLSettings;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Il controller che gestisce la schermata di navigazione tra i reparti del supermercato
 */
public class MarketSectionLayoutController implements Initializable {

    // articoli mostrati sulla grid pane
    private final List<Articoli> gridPaneArticles = new ArrayList<>();
    // listener per mostrare i dettagli dell'articolo selezionato sul pannello 'Dettagli'
    private ArticleSelectionListener articleSelectionListener;

    // articolo selezionato
    private Articoli chosenArticle;

    // variabili per l'aggiunta al carrello
    private Integer chosenArticleQuantity;      // quantita' corrente da aggiungere al carrello
    private Integer chosenArticleStorage;       // scorte articolo selezionato

    // logger per tracciamento query
    private static final Logger logger = Logger.getLogger(LoginLayoutController.class.getName());

    // variabili gestione preview carrello - (pannello laterale destro)
    private Integer cartSize = 0;
    private final ArrayList<String> showedBarcodes = new ArrayList<>();

    // barra superiore dei reparti
    @FXML private Button macelleriaButton;
    @FXML private Label macelleriaLabel;

    @FXML private Button pescheriaButton;
    @FXML private Label pescheriaLabel;

    @FXML private Button ortofruttaButton;
    @FXML private Label ortofruttaLabel;

    @FXML private Button alimentariButton;
    @FXML private Label alimentariLabel;

    @FXML private Button fornoButton;
    @FXML private Label fornoLabel;

    @FXML private Button bevandeButton;
    @FXML private Label bevandeLabel;

    @FXML private Button surgelatiButton;
    @FXML private Label surgelatiLabel;

    @FXML private Button snacksButton;
    @FXML private Label snacksLabel;

    @FXML private Button babyButton;
    @FXML private Label babyLabel;

    @FXML private Button cartoleriaButton;
    @FXML private Label cartoleriaLabel;

    @FXML private Button petButton;
    @FXML private Label petLabel;

    @FXML private Button benessereButton;
    @FXML private Label benessereLabel;

    @FXML private Button casalinghiButton;
    @FXML private Label casalinghiLabel;

    @FXML private Button logoutButton;
    @FXML private Label logoutLabel;

    @FXML private Button aboutUsButton;
    @FXML private Label aboutUsLabel;

    // barra di ricerca
    @FXML private TextField searchTextField;
    @FXML private Button searchButton;

    // Dettagli articolo - pannello laterale sinistro
    @FXML private Label articleNameLabel;               // nome articolo
    @FXML private Label articlePriceLabel;              // prezzo articolo
    @FXML private ImageView articleImageView;           // immagine dell'articolo
    @FXML private Label articleProducerLabel;           // produttore
    @FXML private Label articleDescription1Label;       // descrizione estesa
    @FXML private Label articleDescription2Label;       // descrizione quantita' articolo
    @FXML private Label quantityLabel;

    // bottoni aggiunta al carrello
    @FXML private Button incrementButton;
    @FXML private Button decrementButton;
    @FXML private Button addToCartButton;

    // grid-scrollable pane
    @FXML private ScrollPane articleScrollPane;
    @FXML private GridPane articleGridPane;

    // Carrello - pannello laterale destro
    @FXML private VBox articolo1VBox;
    @FXML private HBox articolo1InfoHBox;
    @FXML private Label articolo1Label;
    @FXML private Label quantita1Label;
    @FXML private Label prezzo1Label;

    @FXML private VBox articolo2VBox;
    @FXML private HBox articolo2InfoHBox;
    @FXML private Label articolo2Label;
    @FXML private Label quantita2Label;
    @FXML private Label prezzo2Label;

    @FXML private VBox articolo3VBox;
    @FXML private HBox articolo3InfoHBox;
    @FXML private Label articolo3Label;
    @FXML private Label quantita3Label;
    @FXML private Label prezzo3Label;

    @FXML private VBox articolo4VBox;
    @FXML private HBox articolo4InfoHBox;
    @FXML private Label articolo4Label;
    @FXML private Label quantita4Label;
    @FXML private Label prezzo4Label;

    @FXML private VBox articolo5VBox;
    @FXML private HBox articolo5InfoHBox;
    @FXML private Label articolo5Label;
    @FXML private Label quantita5Label;
    @FXML private Label prezzo5Label;

    @FXML private VBox articolo6VBox;
    @FXML private HBox articolo6InfoHBox;
    @FXML private Label articolo6Label;
    @FXML private Label quantita6Label;
    @FXML private Label prezzo6Label;

    @FXML private VBox articolo7VBox;
    @FXML private HBox articolo7InfoHBox;
    @FXML private Label articolo7Label;
    @FXML private Label quantita7Label;
    @FXML private Label prezzo7Label;

    @FXML private Label rimanentiLabel;
    @FXML private Label totaleCarrelloLabel;
    @FXML private Label quantitaTotaleCarrelloLabel;

    // bottone navigazione alla sezione pagamenti
    @FXML private Button pagaOraButton;

    /**
     * Metodo di inizializzazione del controller (implements Initializable)
     * Per default vengono mostrati TUTTI gli articoli presenti nel supermercato
     */
    @Override public void initialize(URL location, ResourceBundle resources) {
        resetMarketSection();
    }

    /**
     * Il metodo restituisce gli articoli presenti nel database secondo la metrica 'filter'<br>
     * Puo' cercare gli articoli di un determinato reparto, oppure effettuare una ricerca per nome
     * @param filter Il nome del reparto(eventuale) di cui si vogliono ottenere gli articoli,
     *               oppure la radice del nome da cercare
     * @param isSection Indica se il il 'filter' passato in ingresso e' un reparto oppure la radice di un articolo da ricercare
     * @return Restituisce la lista degli articoli filtrati
     */
    private List<Articoli> getArticles(String filter, boolean isSection) throws DAOException, SQLException, IOException {

        List<Articoli> articoli;
        // se filter indica un reparto, leggo gli articoli di tale reparto
        if(isSection)
            articoli = filterBySection(filter);

        // carico tutti gli articoli presenti nel supermercato
        else if (filter.equals("Initialize"))
            articoli = ArticoliDAOMySQL.getInstance().selectAll();

        // altrimenti ricerco per nome
        else {
            articoli = filterByName(filter);
            if(articoli.size()==0)
                searchTextField.setText("Nessun articolo trovato per \" " + filter + "\"!");

            // deseleziono l'eventuale reparto selezionato ( grafica )
            updateSectionBar("None");
        }

        return articoli;
    }

    /**
     * Il metodo esegue una ricerca tra gli articoli nel db per nome<br>
     * NB. Nel caso di passaggio di stringa vuota, si restiuiscono tutti gli articoli del supermercato
     * @param name La radice dell'articolo da ricercare
     * @return Restituisce gli eventuali articoli cercati
     */
    private List<Articoli> filterByName(String name) throws DAOException {
        return ArticoliDAOMySQL.getInstance().select(new Articoli(name,""));
    }

    /**
     * Il metodo cerca gli articoli filtrando per reparto<br>
     * Di fatto specializza la select() di ArticoliDAOMySQL ricercando per reparti
     * @param section Il reparto di cui voglio leggere gli articoli
     * @return Restituisce gli articoli del reparto selezionato
     */
    public ArrayList<Articoli> filterBySection(String section) throws SQLException {

        Statement statement = DAOMySQLSettings.getStatement();

        String query = "select * from articoli where (reparto = '" + section +"');" ;

        if (Util.isQueryPrintingEnabled()){
            try {
                logger.info("SQL: " + query);
            } catch (NullPointerException nullPointerException) {
                System.out.println("SQL: " + query);
            }
        }

        ArrayList<Articoli> filteredArticles = new ArrayList<>();

        ResultSet rs = statement.executeQuery(query);
        while(rs.next()){
            try {
                filteredArticles.add(new Articoli(
                        rs.getString("nome"),
                        rs.getFloat("prezzo"),
                        rs.getInt("scorteMagazzino"),
                        rs.getString("barcode"),
                        rs.getString("reparto"),
                        rs.getString("produttore"),
                        rs.getString("descrizioneProdotto"),
                        rs.getString("descrizioneQuantita"),
                        rs.getInt("idArticolo"))
                );
            }
            catch (DAOException e){
                e.printStackTrace();
            }

        }
        DAOMySQLSettings.closeStatement(statement);

        return filteredArticles;
    }

    /**
     * Il metodo carica e mostra gli articoli filtrati per nome o sezione
     * @param filter Indica il reparto o il nome degli articoli da caricare
     * @param isSection Indica se il filtro passato e' un reparto (true) oppure un nome(false)
     */
    public void loadFilteredItems(String filter, boolean isSection) throws IOException {
        // pulisco la griglia
        clearGridItems();

        // aggiungo gli articoli selezionati alla griglia, e ne carico le immagini
        try {
            gridPaneArticles.addAll(getArticles(filter, isSection));
            loadImages();
        }
        catch (DAOException | SQLException | IOException e) {
            e.printStackTrace();
        }

        // se viene caricato almeno un articolo, imposto il primo come articolo selezionato e ne mostro i dettagli
        if (gridPaneArticles.size() > 0) {
            setChosenArticle(gridPaneArticles.get(0));
            //NB. Aggiorno il l'articolo corrente con quello selezionato cliccando
            // articleSelectionListener = articolo -> setChosenArticle(articolo);
            articleSelectionListener = this::setChosenArticle;
        }

        // aggiorno la grafica della griglia
        try {
            fillGridPane(gridPaneArticles);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // carico e immagini degli articoli della griglia
        loadImages();
    }

    /**
     * Il metodo aggiorna la grafica del grid pane, mostrando gli articoli passati
     * @param articoli La lista degli articoli con cui riempire la griglia
     */
    public void fillGridPane(List<Articoli> articoli) throws IOException {
        // carico le immagini
        loadImages();

        int column = 0;
        int row = 1;
        // per ciascun articolo da mostrare, carico fxml e controller
        for (Articoli articolo : articoli) {

            FXMLLoader itemLoader = new FXMLLoader();
            itemLoader.setLocation(Main.class.getResource("view/ArticleGridItem.fxml"));
            AnchorPane anchorPane = itemLoader.load();

            ArticleGridItemController articleGridItemController = itemLoader.getController();
            articleGridItemController.loadItem(articolo, articleSelectionListener);

            // ogni riga contiene 3 articoli
            if (column == 3) {
                column = 0;
                row++;
            }

            articleGridPane.add(anchorPane, column++, row);     //(child,column,row)
            setGridLayout(anchorPane);
        }
    }

    /**
     * Il metodo imposta l'URL dell'immagine di ciascun articolo<br>
     * NB. l'URL dell'immagine degli articoli non e' inserita nel database,
     * pertanto ogni volta che voglio mostrae un articolo devo prima leggerlo dal db,
     * e poi assegnargli l'URL dell'immagine da mostrare
     */
    private void loadImages(){
        for (Articoli articoli : gridPaneArticles)
            articoli.setImageURL(Articoli.getURLfromCode(articoli.getBarcode()));
    }

    /**
     * Il metodo definisce il layout della grid-pane che mostra gli articoli
     * @param anchorPane Il pannello di cui si vogliono definire le proprieta'
     */
    public void setGridLayout(AnchorPane anchorPane){
        // grid width
        articleGridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
        articleGridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        articleGridPane.setMaxWidth(Region.USE_PREF_SIZE);

        // grid height
        articleGridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
        articleGridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        articleGridPane.setMaxHeight(Region.USE_PREF_SIZE);

        // inset-margins
        GridPane.setMargin(anchorPane, new Insets(10, 10, 10, 10));
    }

    /**
     * Il metodo svuota il contenuto del grid pane
     */
    public void clearGridItems(){
        articleGridPane.getChildren().clear();
    }

    /**
     * Il metodo gestisce la grafica della barra dei reparti, disegnando il contorno del reparto selezionato
     * e mettendone il nome in grassetto
     * @param section Il reparto da selezionare
     */
    void updateSectionBar(String section){

        // css da applicare
        String prevStyle = "";
        String newFontStyle = "-fx-font-weight: bold";
        // Section Buttons
        String selectionStyle = "-fx-border-width: 1;" +
                                "-fx-border-color: rgb(79, 172, 254);" +
                                "-fx-effect: dropshadow( three-pass-box , rgb(79,172,254), 5, 0.0 , 0 , 1 );";

        // Prima di aggiornare il reparto appena selezionato, resetto la grafica del precedente
        switch (App.getInstance().getReparto()) {
            case "Macelleria" -> {
                macelleriaButton.setStyle(prevStyle);
                macelleriaLabel.setStyle(prevStyle);
            }
            case "Pescheria" -> {
                pescheriaButton.setStyle(prevStyle);
                pescheriaLabel.setStyle(prevStyle);
            }
            case "Ortofrutta" -> {
                ortofruttaButton.setStyle(prevStyle);
                ortofruttaLabel.setStyle(prevStyle);
            }
            case "Alimentari" -> {
                alimentariButton.setStyle(prevStyle);
                alimentariLabel.setStyle(prevStyle);
            }
            case "Forno" -> {
                fornoButton.setStyle(prevStyle);
                fornoLabel.setStyle(prevStyle);
            }
            case "Bevande" -> {
                bevandeButton.setStyle(prevStyle);
                bevandeLabel.setStyle(prevStyle);
            }
            case "Surgelati" -> {
                surgelatiButton.setStyle(prevStyle);
                surgelatiLabel.setStyle(prevStyle);
            }
            case "Snacks" -> {
                snacksButton.setStyle(prevStyle);
                snacksLabel.setStyle(prevStyle);
            }
            case "Baby" -> {
                babyButton.setStyle(prevStyle);
                babyLabel.setStyle(prevStyle);
            }
            case "Cartoleria" -> {
                cartoleriaButton.setStyle(prevStyle);
                cartoleriaLabel.setStyle(prevStyle);
            }
            case "Pet" -> {
                petButton.setStyle(prevStyle);
                petLabel.setStyle(prevStyle);
            }
            case "Benessere" -> {
                benessereButton.setStyle(prevStyle);
                benessereLabel.setStyle(prevStyle);
            }
            case "Casalinghi" -> {
                casalinghiButton.setStyle(prevStyle);
                casalinghiLabel.setStyle(prevStyle);
            }
            case "None" -> {
                macelleriaButton.setStyle(prevStyle);
                macelleriaLabel.setStyle(prevStyle);
                pescheriaButton.setStyle(prevStyle);
                pescheriaLabel.setStyle(prevStyle);
                ortofruttaButton.setStyle(prevStyle);
                ortofruttaLabel.setStyle(prevStyle);
                alimentariButton.setStyle(prevStyle);
                alimentariLabel.setStyle(prevStyle);
                fornoButton.setStyle(prevStyle);
                fornoLabel.setStyle(prevStyle);
                bevandeButton.setStyle(prevStyle);
                bevandeLabel.setStyle(prevStyle);
                surgelatiButton.setStyle(prevStyle);
                surgelatiLabel.setStyle(prevStyle);
                snacksButton.setStyle(prevStyle);
                snacksLabel.setStyle(prevStyle);
                babyButton.setStyle(prevStyle);
                babyLabel.setStyle(prevStyle);
                cartoleriaButton.setStyle(prevStyle);
                cartoleriaLabel.setStyle(prevStyle);
                petButton.setStyle(prevStyle);
                petLabel.setStyle(prevStyle);
                benessereButton.setStyle(prevStyle);
                benessereLabel.setStyle(prevStyle);
                casalinghiButton.setStyle(prevStyle);
                casalinghiLabel.setStyle(prevStyle);
            }
        }

        // aggiorno la grafica del reparto appena selezionato
        switch (section) {
            case "Macelleria" -> {
                macelleriaButton.setStyle(selectionStyle);
                macelleriaLabel.setStyle(newFontStyle);
            }
            case "Pescheria" -> {
                pescheriaButton.setStyle(selectionStyle);
                pescheriaLabel.setStyle(newFontStyle);
            }
            case "Ortofrutta" -> {
                ortofruttaButton.setStyle(selectionStyle);
                ortofruttaLabel.setStyle(newFontStyle);
            }
            case "Alimentari" -> {
                alimentariButton.setStyle(selectionStyle);
                alimentariLabel.setStyle(newFontStyle);
            }
            case "Forno" -> {
                fornoButton.setStyle(selectionStyle);
                fornoLabel.setStyle(newFontStyle);
            }
            case "Bevande" -> {
                bevandeButton.setStyle(selectionStyle);
                bevandeLabel.setStyle(newFontStyle);
            }
            case "Surgelati" -> {
                surgelatiButton.setStyle(selectionStyle);
                surgelatiLabel.setStyle(newFontStyle);
            }
            case "Snacks" -> {
                snacksButton.setStyle(selectionStyle);
                snacksLabel.setStyle(newFontStyle);
            }
            case "Baby" -> {
                babyButton.setStyle(selectionStyle);
                babyLabel.setStyle(newFontStyle);
            }
            case "Cartoleria" -> {
                cartoleriaButton.setStyle(selectionStyle);
                cartoleriaLabel.setStyle(newFontStyle);
            }
            case "Pet" -> {
                petButton.setStyle(selectionStyle);
                petLabel.setStyle(newFontStyle);
            }
            case "Benessere" -> {
                benessereButton.setStyle(selectionStyle);
                benessereLabel.setStyle(newFontStyle);
            }
            case "Casalinghi" -> {
                casalinghiButton.setStyle(selectionStyle);
                casalinghiLabel.setStyle(newFontStyle);
            }
            // caso nessun reparto selezionato
            case "None" -> {}
        }
    }

    /**
     * Il metodo gestisce la grid-pane della schermata, mostrando gli articoli del reparto(eventuale) selezionato
     * @param section Il reparto di cui si volgiono mostrare gli articoli
     */
    public void loadSectionArticles(String section){
        // svuoto la griglia
        clearGridItems();
        gridPaneArticles.clear();

        // carico gli articoli da mostrare
        try {
            loadFilteredItems(section, true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // aggiorno la grafica della barra dei reparti
        updateSectionBar(section);

        // infine, salvo il reparto selezionato in App
        App.getInstance().setReparto(section);

    }

    /**
     * Il metodo imposta l'articolo come 'selezionato', salvandone i dati nel controller e mostrandone i dettagli nel pannello Dettagli
     * @param articolo L'articolo selezionato da impostare
     */
    private void setChosenArticle(Articoli articolo) {
        // aggiorno il controller
        this.chosenArticle = articolo;
        this.chosenArticleQuantity = 0;

        // controllo la quantita' presente in magazzino
        try {
            this.chosenArticleStorage = checkStock(this.chosenArticle.getBarcode());
        }
        catch (DAOException e) {
            e.printStackTrace();
        }

        // aggiorno il pannello Dettagli
        updateArticlePane();
    }

    /**
     * Il metodo gestisce la grafica del pannello 'Dettagli articolo'
     */
    void updateArticlePane(){

        // nome e prezzo
        articleNameLabel.setText(this.chosenArticle.getNome());
        articlePriceLabel.setText(this.chosenArticle.getPrezzo()+" €");

        // immagine
        Image image = new Image("file:" + this.chosenArticle.getImageURL());
        articleImageView.setImage(image);

        // descrizioni
        articleProducerLabel.setText(this.chosenArticle.getProduttore());
        articleDescription1Label.setText(this.chosenArticle.getDescrizioneProdotto());
        articleDescription2Label.setText(this.chosenArticle.getDescrizioneQuantita());

        // di default la quantita' corrente selezionata e' zero
        quantityLabel.setText(String.valueOf(chosenArticleQuantity));
    }

    /**
     * Il metodo gestisce il bottone '+' del selettore quantita' nel pannello dei dettagli articolo
     */
    @FXML public void handleIncrement() {

        Integer cartQuantity = 0;

        // verifico la quantita' gia' aggiunta al carrello
        String cartMapKey = this.chosenArticle.getBarcode();
        if(App.getInstance().getCartMap().containsKey(cartMapKey))
            cartQuantity = App.getInstance().getCartMap().get(cartMapKey);

        // controllo se le scorte sono sufficienti
        if(this.chosenArticleQuantity + cartQuantity < this.chosenArticleStorage) {
            this.chosenArticleQuantity++;
            quantityLabel.setText(String.valueOf(chosenArticleQuantity));
        }
    }

    /**
     * Il metodo gestisce il bottone '-' del selettore quantita' nel pannello dei dettagli articolo
     */
    @FXML public void handleDecrement(){
        // posso diminuire solo se la quantita' corrente e' >= 1
        if(this.chosenArticleQuantity > 0) {
            this.chosenArticleQuantity--;
            quantityLabel.setText(String.valueOf(chosenArticleQuantity));
        }
    }

    /**
     * Il metodo gestisce l'aggiunta al carrello dell'articolo con la relativa quantita' selezionata
     */
    @FXML public void handleAddToCart() throws DAOException {

        if(this.chosenArticleQuantity == 0)
            return;

        String cartMapKey = this.chosenArticle.getBarcode();

        // caso articolo gia' nel carrello -> aggiorno la quantita'
        if(App.getInstance().getCartMap().containsKey(cartMapKey)) {
            Integer oldQuantity = App.getInstance().getCartMap().get(cartMapKey);
            App.getInstance().getCartMap().replace(cartMapKey, oldQuantity + this.chosenArticleQuantity);
        }
        // altrimento lo inserisco nella mappa
        else{
            App.getInstance().getCartMap().put(cartMapKey, this.chosenArticleQuantity);
        }

        // resetto la quantita' corrente a 0 aggiornando anche la label
        this.chosenArticleQuantity = 0;
        quantityLabel.setText(String.valueOf(0));

        // infine aggiorno la grafica della preview del carrello
        updateCart();
    }

    /**
     * Il metodo porta al riepilogo dell'ordine corrente
     */
    @FXML
    public void handleCart() throws DAOException, IOException {
        if (App.getInstance().cartMap.size() != 0) {
            if (!App.getInstance().isOrderSummaryVisited())
                App.getInstance().initOrderSummaryLayout();
            else
                App.getInstance().backToPaymentFromSections();
        }
    }

    /**
     * il metodo restituisce la quantita' dell'articolo selezionato presente in magazzino<br>
     * NB la quantita nel magazzino viene aggiornata solo dopo aver finalizzato con successo il pagamento di un ordine
     * @param codiceArticolo il codice dell'articolo di cui si vuole ottenere la scorta in magazzino
     * @return Restituisce le scorte disponibili del dato articolo
     */
    Integer checkStock(String codiceArticolo) throws DAOException {

        List<Articoli> result = ArticoliDAOMySQL.getInstance().select(new Articoli("", codiceArticolo));

        if (result.size() != 1)
            throw new DAOException("checkStock() error. Article not found.");

        Integer storage = result.get(0).getScorteMagazzino();

        // articolo non disponibile
        if(storage < 1) {
            return 0;
        }
        else return storage;
    }

    /**
     * Il metodo aggiorna la grafica del pannello laterale destro, contenente la preview del carrello<br>
     * Sono mostrati sinteticamente gli ultimi articoli inseriti in ordine cronologico
     */
    public void updateCart() throws DAOException {

        Integer cartSize = App.getInstance().getCartMap().size();

        // nuovo articolo aggiunto al carrello
        if (!cartSize.equals(this.cartSize)){

            // aggiorno la dimensione della preview del carrello
            this.cartSize = cartSize;

            // ci sono 7 slot disponibili: se ho ancora spazio...
            if (cartSize < 8) {
                this.showedBarcodes.add(this.chosenArticle.getBarcode());
                updateRow(cartSize);
            }
            // sto gia' mostrando 7 articoli diversi
            else{
                // aggiorno la lista degli articoli mostrati nella preview del carrello
                updateShowedArticles(this.chosenArticle.getBarcode());
                // shift verso il basso degli articoli
                shiftRows();
                // aggiornamento della rifa superiore (ultimo articolo inserito)
                updateRow(7);
            }
        }

        // aggiorno quantita di articoli già aggiunti al carrello
        else{ //cartSize.equals(this.cartSize)

            String newArticleBarcode = this.chosenArticle.getBarcode();

            // caso articolo gia' mostrato nella preview: aggiorno la relativa riga
            if(this.showedBarcodes.contains(newArticleBarcode))
                updateRow(showedBarcodes.indexOf(newArticleBarcode)+1);

            // caso articolo non mostrato nella preview: shift verso il basso,
            // e lo mostro in testa con la quantita' aggiornata
            else{
                updateShowedArticles(this.chosenArticle.getBarcode());
                shiftRows();
                updateRow(7);
            }
        }

        // infine aggiorno le label della preview del carrello ossia:
        // - totale pezzi(in alto)
        // - importo totale
        // - numero articoli presenti nel carrello ma NON mostrati nella preview (in basso)
        updateCartLabels();
    }

    /**
     * Il metodo aggiorna le labels del pannello laterale destro (numero pezzi,
     * importo totale corrente e numero articoli non visualizzati nella preview)
     */
    public void updateCartLabels() throws DAOException {

        // cartSums.get(0) = quantità totali
        // cartSums.get(1) = importo totale
        ArrayList<Float> cartSums = getCartSums();

        // top
        if (cartSums.get(0) > 0)
            quantitaTotaleCarrelloLabel.setText(cartSums.get(0).intValue() + " pezzi");
        else
            quantitaTotaleCarrelloLabel.setText("");
        if (cartSums.get(1) > 0)
            totaleCarrelloLabel.setText(String.format("%.2f",cartSums.get(1)) + " €");
        else
            totaleCarrelloLabel.setText("");

        // bottom
        if(cartSize > 7)
            rimanentiLabel.setText("Altri " + (cartSize-7) + " elementi nel carrello...");
        else
            rimanentiLabel.setText("-------");
    }

    /**
     * Il metodo aggiorna la lista contenente i codici degli articoli mostrati nella preview del carrello<br>
     * Elimina il piu' vecchio con uno shift e inserisce in coda l'ultimo aggiunto
     * @param newArticleBarcode Il codice del nuovo articolo da inserire nella lista
     */
    private void updateShowedArticles(String newArticleBarcode) {
        ArrayList<String> tmpList = new ArrayList<>();

        // nb. copio solo gli ultimi size-1 elementi
        for(int i=1; i<this.showedBarcodes.size(); i++)
            tmpList.add(this.showedBarcodes.get(i));

        // rialloco i size-1 barcode
        this.showedBarcodes.clear();
        this.showedBarcodes.addAll(tmpList);
        // e aggiungo l'ultimo inserito
        this.showedBarcodes.add(newArticleBarcode);
    }

    /**
     * Il metodo aggiorna una riga del pannello laterale destro
     * @param row La riga da aggiornare
     */
    private void updateRow(Integer row){

        // leggo la quantita' dal carrello
        String quantita = App.getInstance().getCartMap().get(this.chosenArticle.getBarcode())+"x ";

        // css
        // underline della vBox
        String vBoxStyle = "-fx-border-width: 0 0 1 0;\n-fx-border-color: rgb(79,172,254);";
        // ovalBox per quantita' e prezzo
        String hBoxStyle = "-fx-border-radius: 50;\n-fx-border-width: 1;\n-fx-border-color: rgb(79,172,254);";

        switch (row) {
            case 1 -> {
                articolo1Label.setText(this.chosenArticle.getNome());
                prezzo1Label.setText(String.format("%.2f",this.chosenArticle.getPrezzo()) + " €");
                quantita1Label.setText(quantita);
                if(this.cartSize == 1) {
                    articolo1VBox.setStyle(vBoxStyle);
                    articolo1InfoHBox.setStyle(hBoxStyle);
                }
            }
            case 2 -> {
                articolo2Label.setText(this.chosenArticle.getNome());
                prezzo2Label.setText(String.format("%.2f",this.chosenArticle.getPrezzo()) + " €");
                quantita2Label.setText(quantita);
                if(this.cartSize == 2) {
                    articolo2VBox.setStyle(vBoxStyle);
                    articolo2InfoHBox.setStyle(hBoxStyle);
                }
            }
            case 3 -> {
                articolo3Label.setText(this.chosenArticle.getNome());
                prezzo3Label.setText(String.format("%.2f",this.chosenArticle.getPrezzo()) + " €");
                quantita3Label.setText(quantita);
                if(this.cartSize == 3) {
                    articolo3VBox.setStyle(vBoxStyle);
                    articolo3InfoHBox.setStyle(hBoxStyle);
                }
            }
            case 4 -> {
                articolo4Label.setText(this.chosenArticle.getNome());
                prezzo4Label.setText(String.format("%.2f",this.chosenArticle.getPrezzo()) + " €");
                quantita4Label.setText(quantita);
                if(this.cartSize == 4) {
                    articolo4VBox.setStyle(vBoxStyle);
                    articolo4InfoHBox.setStyle(hBoxStyle);
                }
            }
            case 5 -> {
                articolo5Label.setText(this.chosenArticle.getNome());
                prezzo5Label.setText(String.format("%.2f",this.chosenArticle.getPrezzo()) + " €");
                quantita5Label.setText(quantita);

                // nb. the clearAll function also resets the style
                if(this.cartSize == 5) {
                    articolo5VBox.setStyle(vBoxStyle);
                    articolo5InfoHBox.setStyle(hBoxStyle);
                }
            }

            case 6 -> {
                articolo6Label.setText(this.chosenArticle.getNome());
                prezzo6Label.setText(String.format("%.2f",this.chosenArticle.getPrezzo()) + " €");
                quantita6Label.setText(quantita);

                // nb. the clearAll function also resets the style
                if(this.cartSize == 6) {
                    articolo6VBox.setStyle(vBoxStyle);
                    articolo6InfoHBox.setStyle(hBoxStyle);
                }
            }

            case 7 -> {
                articolo7Label.setText(this.chosenArticle.getNome());
                prezzo7Label.setText(String.format("%.2f",this.chosenArticle.getPrezzo()) + " €");
                quantita7Label.setText(quantita);

                // nb. the clearAll function also resets the style
                if(this.cartSize >= 7) {
                    articolo7VBox.setStyle(vBoxStyle);
                    articolo7InfoHBox.setStyle(hBoxStyle);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + row);
        }
    }

    /**
     * Il metodo aggiorna la grafica del pannello laterale destro eseguendo uno shift verso il basso<br>
     * La riga in testa (settimo articolo) viene svuotata, predisponendo l'inserimento di un nuovo articolo
     */
    private void shiftRows(){

        articolo1Label.setText(articolo2Label.getText());
        prezzo1Label.setText(prezzo2Label.getText());
        quantita1Label.setText(quantita2Label.getText());

        articolo2Label.setText(articolo3Label.getText());
        prezzo2Label.setText(prezzo3Label.getText());
        quantita2Label.setText(quantita3Label.getText());

        articolo3Label.setText(articolo4Label.getText());
        prezzo3Label.setText(prezzo4Label.getText());
        quantita3Label.setText(quantita4Label.getText());

        articolo4Label.setText(articolo5Label.getText());
        prezzo4Label.setText(prezzo5Label.getText());
        quantita4Label.setText(quantita5Label.getText());

        articolo5Label.setText(articolo6Label.getText());
        prezzo5Label.setText(prezzo6Label.getText());
        quantita5Label.setText(quantita6Label.getText());

        articolo6Label.setText(articolo7Label.getText());
        prezzo6Label.setText(prezzo7Label.getText());
        quantita6Label.setText(quantita7Label.getText());

        clearRow(7);

    }

    /**
     * Il metodo svuota una riga del pannello laterale destro (contenuto e grafica)
     * @param row La riga da eliminare (NB. 1-7)
     */
    private void clearRow(Integer row){
        // empty style
        String borderStyle = "-fx-border-width: 0";

        switch (row) {
            case 1 -> {
                articolo1Label.setText("");
                prezzo1Label.setText("");
                quantita1Label.setText("");
                articolo1VBox.setStyle(borderStyle);
                articolo1InfoHBox.setStyle(borderStyle);
            }
            case 2 -> {
                articolo2Label.setText("");
                prezzo2Label.setText("");
                quantita2Label.setText("");
                articolo2VBox.setStyle("-fx-border-width: 0 0 0 0;");
                articolo2InfoHBox.setStyle(borderStyle);
            }
            case 3 -> {
                articolo3Label.setText("");
                prezzo3Label.setText("");
                quantita3Label.setText("");
                articolo3VBox.setStyle(borderStyle);
                articolo3InfoHBox.setStyle(borderStyle);
            }
            case 4 -> {
                articolo4Label.setText("");
                prezzo4Label.setText("");
                quantita4Label.setText("");
                articolo4VBox.setStyle(borderStyle);
                articolo4InfoHBox.setStyle(borderStyle);
            }
            case 5 -> {
                articolo5Label.setText("");
                prezzo5Label.setText("");
                quantita5Label.setText("");
                articolo5VBox.setStyle(borderStyle);
                articolo5InfoHBox.setStyle(borderStyle);
            }
            case 6 -> {
                articolo6Label.setText("");
                prezzo6Label.setText("");
                quantita6Label.setText("");
                articolo6VBox.setStyle(borderStyle);
                articolo6InfoHBox.setStyle(borderStyle);
            }
            case 7 -> {
                articolo7Label.setText("");
                prezzo7Label.setText("");
                quantita7Label.setText("");
                articolo7VBox.setStyle(borderStyle);
                articolo7InfoHBox.setStyle(borderStyle);
            }

            default -> throw new IllegalStateException("Unexpected value: " + row);
        }
    }

    /**
     * Il metodo calcola l'importo totale dell'ordine corrente, e il numero complessivo di pezzi nel carrello<br>
     * NB il numero totale di articoli si ricava facilmente dalla size() del carrello
     * @return restituisce un ArrayList, salvando in posizione 0 il numero di pezzi e in posizione 1 l'importo totale
     */
    private ArrayList<Float> getCartSums() throws DAOException {

        // la struttura da restituire
        ArrayList<Float> resultItem = new ArrayList<>();

        float articleQuantity = 0f;
        float cartImport = 0f;

        List<Integer> quantityList = App.getInstance().getCartListQuantity();
        List<String> barcodeList = App.getInstance().getCartListArticles();

        for (int i=0; i<App.getInstance().getCartMap().size(); i++){
            Integer quantity = quantityList.get(i);
            Float price = Util.getPrezzoArticoloFromBarcode(barcodeList.get(i));
            articleQuantity += quantity.floatValue();
            cartImport += quantity*price;
        }

        resultItem.add(articleQuantity);
        resultItem.add(cartImport);

        return resultItem;
    }

    /**
     * Il metodo e' usato per aggiornare gli articoli mostrati nella preview del carrello tornando dalla sezione pagamenti<br>
     * Es. Nella sezione pagamenti modifico la quantita' di uno o piu' articoli presenti nel carrello -> conseguentemente devo aggiornare
     * le stesse quantita anche nella scena della navigazione tra i reparti, nella preview del carrello
     */
    public void syncCartPane() throws DAOException {

        // aggiorno le label #pezzi, importo totale e #articoli non mostrati nella preview del carrello
        updateCartLabels();

        int cartSize = this.showedBarcodes.size();
        String quantita;

        // essendo solo 7 quelli mostrati nella preview, li aggiorno direttamente tutti (se presenti)
        quantita = App.getInstance().getCartMap().get(showedBarcodes.get(0)) + "x ";
        quantita1Label.setText(quantita);
        if(cartSize==1) return;

        quantita = App.getInstance().getCartMap().get(showedBarcodes.get(1)) + "x ";
        quantita2Label.setText(quantita);
        if(cartSize==2) return;

        quantita = App.getInstance().getCartMap().get(showedBarcodes.get(2)) + "x ";
        quantita3Label.setText(quantita);
        if(cartSize==3) return;

        quantita = App.getInstance().getCartMap().get(showedBarcodes.get(3)) + "x ";
        quantita4Label.setText(quantita);
        if(cartSize==4) return;

        quantita = App.getInstance().getCartMap().get(showedBarcodes.get(4)) + "x ";
        quantita5Label.setText(quantita);
        if(cartSize==5) return;

        quantita = App.getInstance().getCartMap().get(showedBarcodes.get(5)) + "x ";
        quantita6Label.setText(quantita);
        if(cartSize==6) return;

        quantita = App.getInstance().getCartMap().get(showedBarcodes.get(6)) + "x ";
        quantita7Label.setText(quantita);
    }

    /**
     * Il metodo che gestisce la ricerca per nome<br>
     * Svuota la griglia, ottiene gli oggetti da visualizzare con una query e le mostra sul grid pane
     */
    @FXML public void handleSearch(){

        // svuoto la griglia
        clearGridItems();
        gridPaneArticles.clear();

        // ricerco per nome
        try {
            loadFilteredItems(searchTextField.getText(), false);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        // aggiorno la barra dei reparti e la variabile in App
        updateSectionBar("None");
        App.getInstance().setReparto("None");
    }

    /**
     * Il metodo riporta alla sezione di login ed esegue un logout, svuotando il form e predisponendo un nuovo login<br>
     * E' chiamato cliccando il bottone logout in alto a sinistra nella barra dei reparti
     */
    @FXML void handleLogout(){
        App.getInstance().logout();
    }

    /**
     * Il metodo mostra i nomi degli autori e il link al repository GitHub<br>
     * E' chiamato dall'omonimo button nella barra dei reparti, in alto a destra
     */
    @FXML void handleAboutUs(){
        App.getInstance().aboutUs();
    }

    /**
     * Il metodo svuota la preview (logica) del carrello, svuotando la lista 'showedBarcodes'
     */
    public void clearShowedCart(){
        cartSize = 0;
        showedBarcodes.clear();
    }

    /**
     * Il metodo resetta completamente la scena dei reparti<br>
     * Svuotando la griglia e la preview del carrello
     * Ricarica tutti gli articoli e li mostra
     * Resetta la grafica del pannello laterale destro e setta come articolo selezionato il primo mostrato sulla griglia
     */
    public void resetMarketSection() {

        // reset della griglia e del pannello sinistro
        if(gridPaneArticles.size() != 0)
            gridPaneArticles.clear();

        try {
            gridPaneArticles.addAll(getArticles("Initialize", false));
            loadImages();
        }
        catch (DAOException | SQLException | IOException e) {
            e.printStackTrace();
        }

        if (gridPaneArticles.size() > 0) {
            setChosenArticle(gridPaneArticles.get(0));
            // articleSelectionListener = articolo -> setChosenArticle(articolo);
            articleSelectionListener = this::setChosenArticle;
        }

        try {
            fillGridPane(gridPaneArticles);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // reset pannello destro
        try {
            updateCartLabels();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        for (int i = 1; i < 8; i++)
            clearRow(i);

        // reset barra dei reparti
        App.getInstance().setReparto("None");
        updateSectionBar(App.getInstance().getReparto());
    }

    // Gestione dei bottoni associati ai vari reparti
    @FXML public void handleButchery()              { loadSectionArticles("Macelleria"); }
    @FXML public void handleFishmonger()            { loadSectionArticles("Pescheria");  }
    @FXML public void handleFruitAndVegetables()    { loadSectionArticles("Ortofrutta"); }
    @FXML public void handleGrocery()               { loadSectionArticles("Alimentari"); }
    @FXML public void handleBackery()               { loadSectionArticles("Forno");      }
    @FXML public void handleDrinks()                { loadSectionArticles("Bevande");    }
    @FXML public void handleFrozen()                { loadSectionArticles("Surgelati");  }
    @FXML public void handleSnacks()                { loadSectionArticles("Snacks");     }
    @FXML public void handleBaby()                  { loadSectionArticles("Baby");       }
    @FXML public void handleStationery()            { loadSectionArticles("Cartoleria"); }
    @FXML public void handlePet()                   { loadSectionArticles("Pet");        }
    @FXML public void handleWellness()              { loadSectionArticles("Benessere");  }
    @FXML public void handleHousehold()             { loadSectionArticles("Casalinghi"); }
}
