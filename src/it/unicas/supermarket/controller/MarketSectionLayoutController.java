package it.unicas.supermarket.controller;
import it.unicas.supermarket.App;
import it.unicas.supermarket.ArticleSelectionListener;
import it.unicas.supermarket.Main;
import it.unicas.supermarket.model.Articoli;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.mysql.ArticoliDAOMySQL;
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
    // left-panel
    @FXML private Label articleNameLabel;
    @FXML private Label articlePriceLabel;
    @FXML private ImageView articleImageView;
    @FXML private Label articleProducerLabel;
    @FXML private Label articleDescription1Label;
    @FXML private Label articleDescription2Label;

    // grid-scrollable pane
    @FXML private ScrollPane articleScrollPane;
    @FXML private GridPane articleGridPane;

    private final List<Articoli> gridPaneArticles = new ArrayList<>();
    private ArticleSelectionListener articleSelectionListener;

    // Section Buttons
    @FXML private Button macelleriaButton;
    @FXML private Button pescheriaButton;
    @FXML private Button ortofruttaButton;
    @FXML private Button alimentariButton;
    @FXML private Button fornoButton;
    @FXML private Button bevandeButton;
    @FXML private Button surgelatiButton;
    @FXML private Button snacksButton;
    @FXML private Button babyButton;
    @FXML private Button cartoleriaButton;
    @FXML private Button petButton;
    @FXML private Button benessereButton;
    @FXML private Button casalinghiButton;

    private static final Logger logger =  Logger.getLogger(LoginLayoutController.class.getName());

    // by default, this loads and shows all the available articles
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        articleNameLabel.setText(articolo.getNome());
        articlePriceLabel.setText(articolo.getPrezzo()+" €");
        Image image = new Image("file:" + articolo.getImageURL());
        articleImageView.setImage(image);
        articleProducerLabel.setText(articolo.getProduttore());
        articleDescription1Label.setText(articolo.getDescrizioneProdotto());
        articleDescription2Label.setText(articolo.getDescrizioneQuantita());
        /*
        articleDetails.setStyle("-fx-background-color: #" + fruit.getColor() + ";\n" +
                "    -fx-background-radius: 30;");
        */
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
        } catch (IOException e) {
            e.printStackTrace();
        }


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
}
