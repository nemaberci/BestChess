package hu.aberci.views;

import hu.aberci.entities.interfaces.Tile;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import lombok.Getter;

public class TileView extends StackPane {

    @Getter
    ObjectProperty<Tile> tileProperty;

    ChessBoardView parent;

    public TileView(ChessBoardView gridPane, Tile tile) {

        tileProperty = new SimpleObjectProperty<>(tile);
        parent = gridPane;

        setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        System.out.println("I WAS CLICKED, I AM AT " + tileProperty.get().getXProperty().get() + ", " + tileProperty.get().getYProperty().get());
                    }
                }
        );

    }

}
