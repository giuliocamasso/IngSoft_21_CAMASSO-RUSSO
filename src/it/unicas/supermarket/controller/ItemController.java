package it.unicas.supermarket.controller;

import it.unicas.supermarket.IndianListener;
import it.unicas.supermarket.MainSample;
import it.unicas.supermarket.model.Fruit;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ItemController {
    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLable;

    @FXML
    private ImageView img;

    @FXML
    private void click(MouseEvent mouseEvent) {
        indianListener.onClickListener(fruit);
    }

    private Fruit fruit;
    private IndianListener indianListener;

    public void setData(Fruit fruit, IndianListener myListener) {
        this.fruit = fruit;
        this.indianListener = myListener;
        nameLabel.setText(fruit.getName());
        priceLable.setText(MainSample.CURRENCY + fruit.getPrice());
        Image image = new Image("file:"+fruit.getImgSrc());
        img.setImage(image);
    }
}
