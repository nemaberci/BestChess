package hu.aberci.views;

import hu.aberci.entities.interfaces.Tile;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;
import lombok.Getter;

public class TileView extends Button {

    @Getter
    ObjectProperty<Tile> tileProperty;

    ChessBoardView parent;

    public TileView(ChessBoardView gridPane, Tile tile) {

        tileProperty.set(tile);
        parent = gridPane;

    }

}
