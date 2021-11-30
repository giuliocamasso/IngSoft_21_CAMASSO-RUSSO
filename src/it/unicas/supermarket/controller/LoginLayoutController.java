package it.unicas.supermarket.controller;

import it.unicas.supermarket.App;
import it.unicas.supermarket.Main;
import javafx.fxml.FXML;

/**
 * [ ! MUST UPDATE THIS______________________________________________________________________________]
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 *
 * @author GC-GR
 */
public class LoginLayoutController {

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {

        App.getInstance().handleExit();

    }

    @FXML
    private void handleConfirm() {
        App.getInstance().handleValidate();

    }
}