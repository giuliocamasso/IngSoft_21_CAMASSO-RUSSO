package it.unicas.supermarket.view;
import it.unicas.supermarket.model.Cliente;
import it.unicas.supermarket.model.dao.mysql.ClienteDAOMySQL;
import it.unicas.supermarket.model.dao.DAOException;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import it.unicas.supermarket.MainApp;
import javafx.util.Callback;
import java.util.List;


public class Clienti_test_main_viewController {
    @FXML
    private TableView<Cliente> clientiTableView;
    @FXML
    private TableColumn<Cliente, String> nomeColumn;
    @FXML
    private TableColumn<Cliente, String> cognomeColumn;

    @FXML
    private Label nomeLabel;
    @FXML
    private Label cognomeLabel;
    @FXML
    private Label telefonoLabel;
    @FXML
    private Label fedeltaLabel;

    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public Clienti_test_main_viewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the Colleghi table with the two columns.
        nomeColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Cliente, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Cliente, String> p) {
                        // p.getValue() returns the Person instance for a particular TableView row
                        return p.getValue().nomeProperty();
                    }
                });

        //nomeColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        cognomeColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());


        // Listen for selection changes and show the Colleghi details when changed.
        clientiTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showClientiDetails(newValue));
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        // clientiTableView.setItems(mainApp.getClienteData());
    }

    /**
     * Fills all text fields to show details about the colleghi.
     * If the specified colleghi is null, all text fields are cleared.
     *
     * @param cliente the cliente or null
     */
    private void showClientiDetails(Cliente cliente) {
        if (cliente != null) {
            // Fill the labels with info from the colleghi object.
            nomeLabel.setText(cliente.getNome());
            cognomeLabel.setText(cliente.getCognome());
            telefonoLabel.setText(cliente.getTelefono());
            fedeltaLabel.setText(cliente.getpunti_fedelta().toString());
        } else {
            // Colleghi is null, remove all the text.
            nomeLabel.setText("");
            cognomeLabel.setText("");
            telefonoLabel.setText("");
            fedeltaLabel.setText("");
        }
    }

}
