package it.unicas.supermarket;

import it.unicas.supermarket.controller.LoginLayoutController;
import it.unicas.supermarket.controller.MarketSectionLayoutController;
import it.unicas.supermarket.controller.OrderSummaryLayoutController;
import it.unicas.supermarket.controller.ReceiptController;
import it.unicas.supermarket.model.dao.DAOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.*;

/**
 * Il singleton che gestisce le scene e la logica dell'applicazione
 */
public class App {

    private static App app = null;

    private Stage mainStage;

    // scene
    private Scene marketSectionScene;
    private Scene paymentSectionScene;
    private Scene loginSectionScene;

    // handler usato per abilitare il blur
    private AnchorPane orderSummaryLayoutPane;

    // controller
    private LoginLayoutController loginLayoutController;
    private OrderSummaryLayoutController orderSummaryLayoutController;
    private MarketSectionLayoutController marketSectionLayoutController;

    // attributi utente corrente
    public String codiceCarta;
    public String codiceCliente;
    public float massimaleMensile;
    public float massimaleRimanente;
    public int puntiFedelta;

    public String reparto = "None";

    // flag per la gestione delle scene
    boolean orderSummaryVisited = false;
    boolean marketSectionsVisited = false;

    /**
     * Il carrello e' una LinkedHashMap avente come keys (String)
     * il barcode degli articoli e come values(Integer) le relative quantita'<br>
     * E' stata usata una LinkedHashMap perche' consente di memorizzare l'ordine
     * di inserimento dei prodotti nel carrello
     */
    public final LinkedHashMap<String, Integer> cartMap = new LinkedHashMap<>();

    // design-pattern Singleton
    private App() {}

    public static App getInstance(){
        if (app == null){
            app = new App();
        }
        return app;
    }

    // getter dei controller
    public LoginLayoutController getLoginLayoutController()                 { return loginLayoutController;                 }
    public OrderSummaryLayoutController getOrderSummaryLayoutController()   { return orderSummaryLayoutController;          }
    public MarketSectionLayoutController getMarketSectionLayoutController() { return marketSectionLayoutController;         }

    // getter e setter delle variabili associate al cliente corrente
    public void setCodiceCarta(String codiceCarta)                          { this.codiceCarta = codiceCarta;               }
    public String getCodiceCarta()                                          { return codiceCarta;                           }

    public String getCodiceCliente()                                        { return codiceCliente;                         }
    public void setCodiceCliente(String codiceCliente)                      { this.codiceCliente = codiceCliente;           }

    public float getMassimaleMensile()                                      { return massimaleMensile;                      }
    public void setMassimaleMensile(float massimaleMensile)                 { this.massimaleMensile = massimaleMensile;     }

    public float getMassimaleRimanente()                                    { return massimaleRimanente;                    }
    public void setMassimaleRimanente(float massimaleRimanente)             { this.massimaleRimanente = massimaleRimanente; }

    public int getPuntiFedelta()                                            { return puntiFedelta;                          }
    public void setPuntiFedelta(int puntiFedelta)                           { this.puntiFedelta += puntiFedelta;            }

    // getter e setter del reparto selezionato
    public String getReparto()                                              { return reparto;                               }
    public void setReparto(String reparto)                                  { this.reparto = reparto;                       }

    // getter del carrello
    public HashMap<String, Integer> getCartMap()                            { return cartMap;                                }

    // getter e setter dei flag
    public boolean isOrderSummaryVisited()                                  { return orderSummaryVisited;                    }
    public void setOrderSummaryVisited(boolean orderSummaryVisited)         { this.orderSummaryVisited = orderSummaryVisited;}
    public boolean getMarketSectionsVisited()                               { return marketSectionsVisited;                  }

    /**
     * Il metodo launch() e' chiamato dallo start() di Main
     * @param primaryStage lo stage principale dell'applicazione
     */
    public void launch(Stage primaryStage){
        // main stage settings
        mainStage = primaryStage;

        mainStage.setTitle("GG Supermarket");
        mainStage.initStyle(StageStyle.UNIFIED);
        mainStage.setResizable(false);

        // icona dell'applicazione
        mainStage.getIcons().add(new Image("file:resources/images/shopping-cart.png"));

        // carico la scena del login
        initLoginLayout();

        mainStage.show();
    }

    /**
     * Prepara la scena del login all'avvio dell'applicazione
     */
    public void initLoginLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/LoginLayout.fxml"));

            AnchorPane loginLayout = loader.load();

            // inizializzo la nuova scena
            Scene loginScene = new Scene(loginLayout);

            // imposto la scena sul mainStage e aggiorno l'handler della scena di App
            mainStage.setScene(loginScene);
            this.loginSectionScene = loginScene;

            // gestione chiusura finestra
            mainStage.setOnCloseRequest(event -> {
                event.consume();
                handleExit();
            });

            // aggiorno l'handler del controller
            this.loginLayoutController = loader.getController();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Chiamato cliccando sulla 'x' della finestra
     */
    public void handleExit() {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Uscita");
        alert.setHeaderText("Vuoi uscire dal supermercato?");
        alert.setContentText("Grazie per averci scelto.");

        ButtonType buttonYes = new ButtonType("Si");
        ButtonType buttonBackToMarketApp = new ButtonType("Torna al supermercato", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonYes, buttonBackToMarketApp);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonYes){
            System.exit(0);
        }

    }
    /**
     * Chiamato alla prima apertura della schermata reparti <br>
     * Prepara la scena mostrando tutti gli articoli disponibili nel supermercato
     */
    public void initMarketSectionLayout() {

        // alle chiamate successive viene solo aggiornata e settata la scena
        marketSectionsVisited = true;

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MarketSectionLayout.fxml"));

            AnchorPane marketSectionLayout = loader.load();

            this.marketSectionScene  = new Scene(marketSectionLayout);
            mainStage.setScene(marketSectionScene);

            mainStage.setOnCloseRequest(event -> {
                event.consume();
                handleExit();
            });

            this.marketSectionLayoutController = loader.getController();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Inizializza la scena di riepilogo del carrello e dei dettagli di pagamento
     */
    public void initOrderSummaryLayout() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/OrderSummaryLayout.fxml"));

            this.orderSummaryLayoutPane = loader.load();

            // inizializzo la nuova scena
            Scene orderSummaryScene = new Scene(orderSummaryLayoutPane);

            // imposto la scena sul mainStage e aggiorno l'handler della scena di App
            mainStage.setScene(orderSummaryScene);
            this.paymentSectionScene = orderSummaryScene;

            // gestione chiusura finestra
            mainStage.setOnCloseRequest(event -> {
                event.consume();
                handleExit();
            });

            // aggiorno l'handler del controller
            this.orderSummaryLayoutController = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Il metodo è chiamato per mostrare lo scontrino dopo la finalizzazione di un ordine
     */
    public void showReceipt() {
        try {

            setBlur(true);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("view/Receipt.fxml"));
            Stage dialogStage = new Stage();

            dialogStage.setTitle("Receipt");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.initOwner(mainStage);

            dialogStage.setScene(new Scene(loader.load()));

            ReceiptController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Metodo di utilita' in fase di debug, usato per la stampa del carrello
     */
    public void printCart() throws DAOException {
        int size = this.cartMap.size();
        System.out.println("--- Ordine ---");

        List<String> articles = getCartListArticles();
        List<Integer> quantities = getCartListQuantity();

        for (int i = 0; i<size; i++){
            String articleName = Util.getNomeArticoloFromBarcode(articles.get(i));
            Float price = Util.getPrezzoArticoloFromBarcode(articles.get(i));
            Integer quantity = quantities.get(i);

            System.out.println(articleName + " " +quantity + "x " + price + " € = " +quantity*price + " €");
        }
    }

    /**
     * Il metodo e' chiamato per aggiornare la sezione dei reparti
     * tornando indietro dalla sezione di pagamento<br>
     * Aggiorna la scena dei reparti sincronizzando gli articoli nel carrello
     * e setta la scena sul main stage
     */
    public void backToSectionsFromPayment() {
        try {
            marketSectionLayoutController.syncCartPane();
        }
        catch (DAOException e) {
            e.printStackTrace();
        }
        mainStage.setScene(marketSectionScene);
    }

    /**
     * Il metodo resetta la scena quando dalla sezione pagamento si ritorna ai reparti e di nuovo alla sezione pagamento
     */
    public void backToPaymentFromSections() throws DAOException, IOException {
        orderSummaryLayoutController.syncProducts();
        mainStage.setScene(paymentSectionScene);
    }

    /**
     * Il metodo chiamato quando si rimuove una carta dal lettore<br>
     * Si svuota il carrello, si resetta il form di benvenuto e si setta la scena di login aggiornata
     */
    public void ejectCard(){
        loginLayoutController.resetForm();

        cartMap.clear();
        marketSectionLayoutController.clearShowedCart();

        mainStage.setScene(loginSectionScene);
    }

    /**
     * Chiamato dal bottone di logout nella sezione dei reparti<Br>
     * Riporta al form del login
     */
    public void logout(){
        ejectCard();
        mainStage.setScene(loginSectionScene);
    }

    /**
     * Il metodo e' chiamato dopo aver effettuato un pagamento<br>
     * Reindirizza al login mostrando i dati dell'acquirente aggiornati
     * Consente di continuare con gli acquisti, oppure di rimuovere la carta e iniziare una spesa con un nuovo utente
     */
    public void ejectCardAfterPayment(){
        // aggiornamento label
        loginLayoutController.getMassimaliLabel().setText(getMassimaliString());
        loginLayoutController.getPuntiFedeltaLabel().setText(String.valueOf(puntiFedelta));
        loginLayoutController.getMessageLabel().setText("Grazie per averci scelto!");
        // aggiornamento del form
        loginLayoutController.getConfirmButton().setText("Conferma");
        loginLayoutController.getCodiceCartaTextField().setEditable(false);
        loginLayoutController.getPinPasswordField().setEditable(false);

        mainStage.setScene(loginSectionScene);
    }

    /**
     * Il metodo e' chiamato dopo la finalizzazione di un ordine e aggiorna la fedelta' del cliente
     * secondo questa politica: 1 punto fedelta' ogni 10 euro di spesa<br>
     * es. 5 euro -> 0 fedelta'<br>
     * es. 19 euro -> 1 fedelta'<br>
     * es. 21 euro -> 2 fedelta'
     * @param totalImport l'importo totale dell'ordine effettuto
     * @return  Restituisce la fedelta' del cliente aggiornata dopo il pagamento
     */
    public int computeFidelity(float totalImport)                       { return (int)(totalImport/10); }

    /**
     * Il metodo mostra informazioni sugli autori del progetto
     */
    public void aboutUs() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("GG Supermarket Info");
        alert.setHeaderText("About");
        alert.setContentText("Autori:\nCamasso Giulio, Russo Giulio\n\nGitHub:\ngiuliocamasso/IngSoft_21_CAMASSO-RUSSO\n");

        ButtonType buttonBackToMarketApp = new ButtonType("Torna al supermercato", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonBackToMarketApp);

        alert.showAndWait();
    }

    /**
     * Il metodo e' chiamato quando un nuovo utente si logga nella stessa sessione<br>
     * Resetta la scena dei reparti e la setta sul main stage
     */
    public void loadNewUserMarketSection() {
        marketSectionLayoutController.clearShowedCart();
        marketSectionLayoutController.resetMarketSection();

        mainStage.setScene(marketSectionScene);
    }

    /**
     * Il metodo e' chiamato sia quando viene mostrato lo scontrino che quando si torna al login cliccando il bottone in esso contenuto<br>
     * Applica/rimuove un filtro di blurring gaussiano sulla scena in background(la sezione dei pagamenti) per enfatizzare lo scontrino in sovraimpressione
     * @param enable applica/rimuove il filtro sulla scena in background
     */
    public void setBlur(boolean enable) {

        if(enable)
            orderSummaryLayoutPane.setEffect(new GaussianBlur(10));
        else
            orderSummaryLayoutPane.setEffect(new GaussianBlur(-10));
    }

    // metodi di utilità

    /**
     * @return Restituisce la lista contenente le chiavi della mappa Carrello
     */
    public List<String> getCartListArticles()                           { return new ArrayList<>(this.cartMap.keySet()); }

    /**
     * @return Restituisce la lista contenente le quantita' della mappa Carrello
     */
    public List<Integer> getCartListQuantity()                          { return new ArrayList<>(this.cartMap.values()); }

    /**
     * @return Resituisce la stringa contenente il massimale rimanente / massimale mensile
     */
    public String getMassimaliString(){
        return  String.format("%.2f",massimaleRimanente) + " / " +  String.format("%.2f",massimaleMensile) + " €";
    }
}
