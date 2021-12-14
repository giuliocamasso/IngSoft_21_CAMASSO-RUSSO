package it.unicas.supermarket.controller;

import it.unicas.supermarket.App;
import it.unicas.supermarket.model.Articoli;
import it.unicas.supermarket.model.dao.DAOException;
import it.unicas.supermarket.model.dao.Util;
import it.unicas.supermarket.model.dao.mysql.ArticoliDAOMySQL;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.util.List;

public class ReceiptArticleGridItemController {

    @FXML private Label receiptArticleLabel;
    @FXML private Label receiptCostLabel;

    public void loadItem(String nome, Integer quantita, Float prezzo) throws DAOException {
        System.out.println("load");

        receiptArticleLabel.setText(nome);
        receiptCostLabel.setText(String.valueOf(quantita) + "x   " + String.valueOf(prezzo));
    }
}
