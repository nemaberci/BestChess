package hu.aberci.entities.data;

import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.Tile;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

public class TileImpl implements Tile {

    @Getter
    ObjectProperty<Piece> pieceProperty;

    @Getter
    IntegerProperty xProperty, yProperty;

    public TileImpl(Piece startingPiece, int x, int y) {

        xProperty = new SimpleIntegerProperty(x);
        yProperty = new SimpleIntegerProperty(y);

        pieceProperty = new SimpleObjectProperty<>(startingPiece);

    }

    public TileImpl(Tile tile) {

        pieceProperty = new SimpleObjectProperty<>();
        pieceProperty.set(
                tile.getPieceProperty().get()
        );

        xProperty = new SimpleIntegerProperty();
        xProperty.set(
                tile.getXProperty().get()
        );

        yProperty = new SimpleIntegerProperty();
        xProperty.set(
                tile.getXProperty().get()
        );

    }

}
