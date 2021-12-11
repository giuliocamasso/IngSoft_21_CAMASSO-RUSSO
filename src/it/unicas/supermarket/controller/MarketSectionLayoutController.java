package it.unicas.supermarket.controller;
import it.unicas.supermarket.App;
import it.unicas.supermarket.Main;
import it.unicas.supermarket.model.Articoli;
import it.unicas.supermarket.model.Clienti;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.mysql.ArticoliDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.ClientiDAOMySQL;
import it.unicas.supermarket.model.dao.mysql.DAOMySQLSettings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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

public class MarketSectionLayoutController implements Initializable {

    // Section Buttons
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

    // left-panel
    @FXML private Label articleNameLabel;
    @FXML private Label articlePriceLabel;
    @FXML private ImageView articleImageView;
    @FXML private Label articleProducerLabel;
    @FXML private Label articleDescription1Label;
    @FXML private Label articleDescription2Label;
    @FXML private Label quantityLabel;
    @FXML private Button incrementButton;
    @FXML private Button decrementButton;
    @FXML private Button addToCartButton;

    // grid-scrollable pane
    @FXML private ScrollPane articleScrollPane;
    @FXML private GridPane articleGridPane;

    private final List<Articoli> gridPaneArticles = new ArrayList<>();
    private ArticleSelectionListener articleSelectionListener;
    private Articoli chosenArticle;
    private Integer chosenArticleQuantity;
    private Integer chosenArticleStorage;

    // right-panel
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

    @FXML private Label rimanentiLabel;

    @FXML private Button pagaOraButton;



    // logger for tracking queries
    private static final Logger logger =  Logger.getLogger(LoginLayoutController.class.getName());

    // by default, this loads and shows all the available articles
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        App.getInstance().setReparto("All");

        try {
            gridPaneArticles.addAll(getArticles("Initialize"));
            loadImages();
        }
        catch (DAOException | SQLException | IOException e) {
            e.printStackTrace();
        }

        if (gridPaneArticles.size() > 0) {
            setChosenArticle(gridPaneArticles.get(0));
            articleSelectionListener = this::setChosenArticle;
        }

        try {
            fillGridPane(gridPaneArticles);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //clear right-pane
        for (Integer i = 1; i < 6; i++)
            clearRow(i);
    }

    private List<Articoli> getArticles(String section) throws DAOException, SQLException, IOException {
        // loading articles from db
        List<Articoli> articoli;

        if (section.equals("Initialize"))
            articoli = ArticoliDAOMySQL.getInstance().selectAll();
        else
            articoli = filterBySection(section);

        return articoli;
    }

    private void setChosenArticle(Articoli articolo) {
        // updates the left-panel
        this.chosenArticle = articolo;
        this.chosenArticleQuantity = 0;
        try {
            this.chosenArticleStorage = checkStock(this.chosenArticle.getBarcode());
            System.out.println("Articoli disponibili: " + this.chosenArticleStorage);
        }
        catch (DAOException e) {
            e.printStackTrace();
        }

        updateArticlePane();
    }

    @FXML
    public void handleCart() {
        System.out.println("Going to Cart section...");
        App.getInstance().initOrderSummaryLayout();
    }

    public void loadSectionArticles(String section){

        System.out.println("Loading" + section);

        clearGridItems();
        gridPaneArticles.clear();

        try {
            loadFilteredItems(section);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // update graphics
        updateSectionBar(section);

        App.getInstance().setReparto(section);

    }

    // section buttons
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

    public void loadFilteredItems(String section) throws IOException {
        clearGridItems();

        try {
           gridPaneArticles.addAll(getArticles(section));
           loadImages();
       }
       catch (DAOException | SQLException | IOException e) {
           e.printStackTrace();
       }

       if (gridPaneArticles.size() > 0) {
           setChosenArticle(gridPaneArticles.get(0));
           articleSelectionListener = this::setChosenArticle;
       }

       try {
           fillGridPane(gridPaneArticles);
       } catch (IOException e) {
           e.printStackTrace();
       }
        loadImages();
   }

    public void clearGridItems(){
        articleGridPane.getChildren().clear();
    }

    public ArrayList<Articoli> filterBySection(String section) throws SQLException {
        Statement statement = DAOMySQLSettings.getStatement();
        String query = "select * from articoli where (reparto = '" + section +"');" ;
        try{
            logger.info("SQL: " + query);
        }
        catch(NullPointerException nullPointerException){
            System.out.println("SQL: " + query);
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

    public void fillGridPane(List<Articoli> articoli) throws IOException {
        loadImages();

        int column = 0;
        int row = 1;
        for (Articoli articolo : articoli) {

            FXMLLoader itemLoader = new FXMLLoader();
            itemLoader.setLocation(Main.class.getResource("view/ArticleGridItem.fxml"));
            AnchorPane anchorPane = itemLoader.load();

            ArticleGridItemController articleGridItemController = itemLoader.getController();
            articleGridItemController.loadItem(articolo, articleSelectionListener);

            // filling gridPane
            if (column == 3) {
                column = 0;
                row++;
            }

            articleGridPane.add(anchorPane, column++, row); //(child,column,row)
            setGridLayout(anchorPane);
        }
    }

    private void loadImages(){
        for (Articoli articoli : gridPaneArticles)
            articoli.setImageURL(Articoli.getURLfromCode(articoli.getBarcode()));
    }

    void updateSectionBar(String section){
        String prevStyle = "";

        String newStyle = "    -fx-border-width: 1;\n" +
                          "    -fx-border-color: rgb(240, 109, 139);" +
                          "    -fx-effect: dropshadow( three-pass-box , rgba(240, 109, 139, 0.6), 5, 0.0 , 0 , 1 );";

        String newFontStyle = "-fx-font-weight: bold";

        // restoring prev selected one
        switch (App.getInstance().getReparto()) {
            case "Macelleria":
                macelleriaButton.setStyle(prevStyle);
                macelleriaLabel.setStyle(prevStyle);
                break;
            case "Pescheria":
                pescheriaButton.setStyle(prevStyle);
                pescheriaLabel.setStyle(prevStyle);
                break;
            case "Ortofrutta":
                ortofruttaButton.setStyle(prevStyle);
                ortofruttaLabel.setStyle(prevStyle);
                break;
            case "Alimentari":
                alimentariButton.setStyle(prevStyle);
                alimentariLabel.setStyle(prevStyle);
                break;
            case "Forno":
                fornoButton.setStyle(prevStyle);
                fornoLabel.setStyle(prevStyle);
                break;
            case "Bevande":
                bevandeButton.setStyle(prevStyle);
                bevandeLabel.setStyle(prevStyle);
                break;
            case "Surgelati":
                surgelatiButton.setStyle(prevStyle);
                surgelatiLabel.setStyle(prevStyle);
                break;
            case "Snacks":
                snacksButton.setStyle(prevStyle);
                snacksLabel.setStyle(prevStyle);
                break;
            case "Baby":
                babyButton.setStyle(prevStyle);
                babyLabel.setStyle(prevStyle);
                break;
            case "Cartoleria":
                cartoleriaButton.setStyle(prevStyle);
                cartoleriaLabel.setStyle(prevStyle);
                break;
            case "Pet":
                petButton.setStyle(prevStyle);
                petLabel.setStyle(prevStyle);
                break;
            case "Benessere":
                benessereButton.setStyle(prevStyle);
                benessereLabel.setStyle(prevStyle);
                break;
            case "Casalinghi":
                casalinghiButton.setStyle(prevStyle);
                casalinghiLabel.setStyle(prevStyle);
                break;
            case "INIT"   :             // NB. it does nothing of first interaction!
        }

        switch (section) {
            case "Macelleria" -> {
                macelleriaButton.setStyle(newStyle);
                macelleriaLabel.setStyle(newFontStyle);
            }
            case "Pescheria" -> {
                pescheriaButton.setStyle(newStyle);
                pescheriaLabel.setStyle(newFontStyle);
            }
            case "Ortofrutta" -> {
                ortofruttaButton.setStyle(newStyle);
                ortofruttaLabel.setStyle(newFontStyle);
            }
            case "Alimentari" -> {
                alimentariButton.setStyle(newStyle);
                alimentariLabel.setStyle(newFontStyle);
            }
            case "Forno" -> {
                fornoButton.setStyle(newStyle);
                fornoLabel.setStyle(newFontStyle);
            }
            case "Bevande" -> {
                bevandeButton.setStyle(newStyle);
                bevandeLabel.setStyle(newFontStyle);
            }
            case "Surgelati" -> {
                surgelatiButton.setStyle(newStyle);
                surgelatiLabel.setStyle(newFontStyle);
            }
            case "Snacks" -> {
                snacksButton.setStyle(newStyle);
                snacksLabel.setStyle(newFontStyle);
            }
            case "Baby" -> {
                babyButton.setStyle(newStyle);
                babyLabel.setStyle(newFontStyle);
            }
            case "Cartoleria" -> {
                cartoleriaButton.setStyle(newStyle);
                cartoleriaLabel.setStyle(newFontStyle);
            }
            case "Pet" -> {
                petButton.setStyle(newStyle);
                petLabel.setStyle(newFontStyle);
            }
            case "Benessere" -> {
                benessereButton.setStyle(newStyle);
                benessereLabel.setStyle(newFontStyle);
            }
            case "Casalinghi" -> {
                casalinghiButton.setStyle(newStyle);
                casalinghiLabel.setStyle(newFontStyle);
            }
        }
    }

    void updateArticlePane(){

        articleNameLabel.setText(this.chosenArticle.getNome());
        articlePriceLabel.setText(this.chosenArticle.getPrezzo()+" €");

        Image image = new Image("file:" + this.chosenArticle.getImageURL());
        articleImageView.setImage(image);

        articleProducerLabel.setText(this.chosenArticle.getProduttore());
        articleDescription1Label.setText(this.chosenArticle.getDescrizioneProdotto());
        articleDescription2Label.setText(this.chosenArticle.getDescrizioneQuantita());

        // also set quantityLabel to zero
        quantityLabel.setText(String.valueOf(chosenArticleQuantity));
        /*
        articleDetails.setStyle("-fx-background-color: #" + fruit.getColor() + ";\n" +
                "    -fx-background-radius: 30;");
        */
    }

    // Add to cart buttons section
    @FXML public void handleIncrement() {
        System.out.println("Increment Pressed");

        Integer cartQuantity = 0;
        String cartMapKey = this.chosenArticle.getBarcode();

        // chosen quantity + already in the cart quantity must be < than the available articles
        if(App.getInstance().getCartMap().containsKey(cartMapKey))
            cartQuantity = App.getInstance().getCartMap().get(cartMapKey);

        if(this.chosenArticleQuantity + cartQuantity < this.chosenArticleStorage) {
            this.chosenArticleQuantity++;
            quantityLabel.setText(String.valueOf(chosenArticleQuantity));
        }
    }

    @FXML public void handleDecrement(){
        System.out.println("Decrement Pressed");
        if(this.chosenArticleQuantity > 0) {
            this.chosenArticleQuantity--;
            quantityLabel.setText(String.valueOf(chosenArticleQuantity));
        }
    }

    @FXML public void handleAddToCart() throws DAOException {
        System.out.println("AddToCart Pressed with quantity: " + this.chosenArticleQuantity);

        // nothing to do in this case
        if(this.chosenArticleQuantity == 0)
            return;

        String cartMapKey = this.chosenArticle.getBarcode();

        // the article is already in the cart
        if(App.getInstance().getCartMap().containsKey(cartMapKey)) {
            Integer oldQuantity = App.getInstance().getCartMap().get(cartMapKey);
            System.out.println("oldQuantity: " + oldQuantity);
            App.getInstance().getCartMap().replace(cartMapKey, oldQuantity + this.chosenArticleQuantity);
            System.out.println("newQuantity: " + App.getInstance().getCartMap().get(cartMapKey));
        }
        else{
            App.getInstance().getCartMap().put(cartMapKey, this.chosenArticleQuantity);
        }

        System.out.println(App.getInstance().getCartMap());
        System.out.println(App.getInstance().getCartListArticles());
        System.out.println(App.getInstance().getCartListQuantity());

        // resets the current article quantity
        this.chosenArticleQuantity = 0;
        quantityLabel.setText(String.valueOf(0));

        updateCart();
    }

    Integer checkStock(String codiceArticolo) throws DAOException {
        List<Articoli> result = ArticoliDAOMySQL.getInstance().select(new Articoli("", codiceArticolo));
        if (result.size() != 1)
            throw new DAOException("checkStock() error. Article not found.");
        Integer storage = result.get(0).getScorteMagazzino();

        if(storage < 1) {
            //articolo non disponibile
            System.out.println("Articolo non disponibile.");
            return 0;
        }
        else return storage;
    }


    void updateCart() throws DAOException {

        //Integer cartSize = App.getInstance().getCartMap().keySet().size();

        //articolo1Label.setText(this.chosenArticle.getNome());
        //prezzo1Label.setText(this.chosenArticle.getPrezzo() + " €");
        //quantita1Label.setText(String.valueOf(App.getInstance().getCartMap().get(this.chosenArticle.getBarcode())));


        updateCartPane();

        System.out.println(App.getInstance().getCartMap());


        System.out.println(App.getInstance().getCartListArticles());
        System.out.println(App.getInstance().getCartListQuantity());

    }

    private void updateCartPane() throws DAOException {
        Integer cartSize = App.getInstance().getCartMap().size();
        System.out.println(cartSize);

        if (cartSize < 6) {
            for (Integer i = 1; i < cartSize + 1; i++)
                updateRow(i);

            for (Integer i = cartSize + 1; i < 6; i++)
                clearRow(i);
        }
        else
            shiftRows();

    }

    private void updateRow(Integer row) throws DAOException {
        Integer index = row-1;
        String barcode = App.getInstance().getCartListArticles().get(index);
        String vBoxStyle =  "-fx-border-width: 0 0 1 0;\n-fx-border-color: rgb(240, 109, 139);";
        String hBoxStyle =  "-fx-border-radius: 50;\n-fx-border-width: 1;\n-fx-border-color: rgb(240, 109, 139);";


        switch (row) {
            case 1 -> {
                articolo1Label.setText(getNomeArticoloFromBarcode(barcode));
                prezzo1Label.setText(getPrezzoArticoloFromBarcode(barcode));
                quantita1Label.setText(String.valueOf(App.getInstance().getCartListQuantity().get(index)));
                articolo1VBox.setStyle(vBoxStyle);
                articolo1InfoHBox.setStyle(hBoxStyle);
            }
            case 2 -> {
                articolo2Label.setText(getNomeArticoloFromBarcode(barcode));
                prezzo2Label.setText(getPrezzoArticoloFromBarcode(barcode));
                quantita2Label.setText(String.valueOf(App.getInstance().getCartListQuantity().get(index)));
                articolo2VBox.setStyle(vBoxStyle);
                articolo2InfoHBox.setStyle(hBoxStyle);
            }
            case 3 -> {
                articolo3Label.setText(getNomeArticoloFromBarcode(barcode));
                prezzo3Label.setText(getPrezzoArticoloFromBarcode(barcode));
                quantita3Label.setText(String.valueOf(App.getInstance().getCartListQuantity().get(index)));
                articolo3VBox.setStyle(vBoxStyle);
                articolo3InfoHBox.setStyle(hBoxStyle);
            }
            case 4 -> {
                articolo4Label.setText(getNomeArticoloFromBarcode(barcode));
                prezzo4Label.setText(getPrezzoArticoloFromBarcode(barcode));
                quantita4Label.setText(String.valueOf(App.getInstance().getCartListQuantity().get(index)));
                articolo4VBox.setStyle(vBoxStyle);
                articolo4InfoHBox.setStyle(hBoxStyle);
            }
            case 5 -> {
                articolo5Label.setText(getNomeArticoloFromBarcode(barcode));
                prezzo5Label.setText(getPrezzoArticoloFromBarcode(barcode));
                quantita5Label.setText(String.valueOf(App.getInstance().getCartListQuantity().get(index)));
                articolo5VBox.setStyle(vBoxStyle);
                articolo5InfoHBox.setStyle(hBoxStyle);
            }
            default -> throw new IllegalStateException("Unexpected value: " + row);
        }
    }

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

        clearRow(5);

    }

    private void clearRow(Integer row){
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

            default -> throw new IllegalStateException("Unexpected value: " + row);
        }
    }



    public String getNomeArticoloFromBarcode(String barcode) throws DAOException {
        // select() function is barcode-based
        List<Articoli> article = ArticoliDAOMySQL.getInstance().select(new Articoli("", barcode));
        if( article.size() != 1)
            return "ERROR";
        else
            return article.get(0).getNome();
    }

    public String getPrezzoArticoloFromBarcode(String barcode) throws DAOException {
        // select() function is barcode-based
        List<Articoli> article = ArticoliDAOMySQL.getInstance().select(new Articoli("", barcode));
        if( article.size() != 1)
            return "ERROR";
        else
            return article.get(0).getPrezzo()+" €";
    }

}
