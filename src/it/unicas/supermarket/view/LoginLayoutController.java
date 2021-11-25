package it.unicas.supermarket.view;
import java.util.Optional;

import it.unicas.supermarket.MainApp;
import it.unicas.supermarket.model.dao.mysql.DAOMySQLSettings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * [ ! MUST UPDATE THIS______________________________________________________________________________]
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 *
 * @author GC-GR
 */
public class LoginLayoutController {

    // Reference to the main application
    private MainApp mainApp;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Creates an empty address book.
     */

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {

        mainApp.handleExit();

    }

    @FXML
    private void handleValidate() {
        mainApp.handleValidate();

    }

}