package it.unicas.supermarket.controller;
import it.unicas.supermarket.App;
import it.unicas.supermarket.ArticleSelectionListener;
import it.unicas.supermarket.Main;
import it.unicas.supermarket.model.Articoli;
import it.unicas.supermarket.model.Fruit;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.mysql.ArticoliDAOMySQL;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MarketSectionLayoutController implements Initializable {

    @FXML
    private Label articleDescription1Label;

    @FXML
    private Label articleDescription2Label;

    @FXML
    private Label articleProducerLabel;

    @FXML
    private Label articleNameLabel;

    @FXML
    private Label articlePriceLabel;

    @FXML
    private ImageView articleImageView;

    @FXML
    private ScrollPane articleScrollPane;

    @FXML
    private GridPane articleGridPane;

    // Section Buttons
    @FXML
    private Button macelleriaButton;
    @FXML
    private Button pescheriaButton;
    @FXML
    private Button ortofruttaButton;
    @FXML
    private Button alimentariButton;
    @FXML
    private Button fornoButton;
    @FXML
    private Button bevandeButton;
    @FXML
    private Button surgelatiButton;
    @FXML
    private Button snacksButton;
    @FXML
    private Button babyButton;
    @FXML
    private Button cartoleriaButton;
    @FXML
    private Button petButton;
    @FXML
    private Button benessereButton;
    @FXML
    private Button casalinghiButton;


    private List<Articoli> articoli = new ArrayList<>();
    private Image image;
    private ArticleSelectionListener articleSelectionListener;

    private List<Articoli> getData() throws DAOException {
        // loading articles from catalogue
        List<Articoli> catalogo = ArticoliDAOMySQL.getInstance().selectAll();
        // updating url
        for (Articoli articoli : catalogo)
            articoli.setImageURL(Articoli.getURLfromCode(articoli.getBarcode()));

        return catalogo;
    }

    private void setChosenArticle(Articoli articolo) {
        articleNameLabel.setText(articolo.getNome());
        articlePriceLabel.setText(String.valueOf(articolo.getPrezzo()));
        image = new Image("file:" + articolo.getImageURL());
        articleImageView.setImage(image);
        articleProducerLabel.setText(articolo.getProduttore());
        articleDescription1Label.setText(articolo.getDescrizioneProdotto());
        articleDescription2Label.setText(articolo.getDescrizioneQuantita());
        /*
        articleDetails.setStyle("-fx-background-color: #" + fruit.getColor() + ";\n" +
                "    -fx-background-radius: 30;");
        */
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            articoli.addAll(getData());
        } catch (DAOException e) {
            e.printStackTrace();
        }
        if (articoli.size() > 0) {
            setChosenArticle(articoli.get(0));
            articleSelectionListener = this::setChosenArticle;
        }
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < articoli.size(); i++) {
                FXMLLoader itemLoader = new FXMLLoader();
                itemLoader.setLocation(Main.class.getResource("view/ArticleGridItem.fxml"));
                AnchorPane anchorPane = itemLoader.load();
                ArticleGridItemController articleGridItemController = itemLoader.getController();

                articleGridItemController.setData(articoli.get(i), articleSelectionListener);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                articleGridPane.add(anchorPane, column++, row); //(child,column,row)

                //set grid width
                articleGridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                articleGridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                articleGridPane.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                articleGridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                articleGridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                articleGridPane.setMaxHeight(Region.USE_PREF_SIZE);

                // margini all'interno del grid pane
                GridPane.setMargin(anchorPane, new Insets(10, 10, 10 ,10));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleChart() {
        System.out.println("Going to Chart section...");
        App.getInstance().initOrderSummaryLayout();
    }

    public void loadSectionArticles(String section){
        System.out.println("Loading" + section);
    }

    // section buttons
    @FXML
    public void handleButchery(){
        loadSectionArticles("Macelleria");
    }

    @FXML
    public void handleFishmonger(){
        loadSectionArticles("Pescheria");
    }

    @FXML
    public void handleFruitAndVegetables(){
        loadSectionArticles("Ortofrutta");
    }

    @FXML
    public void handleGrocery(){
        loadSectionArticles("Alimentari");
    }

    @FXML
    public void handleBackery(){
        loadSectionArticles("Forno");
    }

    @FXML
    public void handleDrinks(){
        loadSectionArticles("Bevande");
    }

    @FXML
    public void handleFrozen(){
        loadSectionArticles("Surgelati");
    }

    @FXML
    public void handleSnacks(){
        loadSectionArticles("Snacks");
    }

    @FXML
    public void handleBaby(){
        loadSectionArticles("Baby");
    }

    @FXML
    public void handleStationery(){
        loadSectionArticles("Cartoleria");
    }

    @FXML
    public void handlePet(){
        loadSectionArticles("Pet");
    }

    @FXML
    public void handleWellness(){
        loadSectionArticles("Benessere");
    }

    @FXML
    public void handleHousehold(){
        loadSectionArticles("Casalinghi");
    }





}
