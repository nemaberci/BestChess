package hu.aberci.entities.data;

import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.Tile;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

/**
 * Data class used for storing Tiles.
 * No special logic is implemented in this class, for more info see: {@link Tile}
 * */
public class TileImpl implements Tile {

    @Getter
    ObjectProperty<Piece> pieceProperty;

    @Getter
    IntegerProperty xProperty, yProperty;

    /**
     * Creates a new Tile
     *
     * @param x The X coordinate of the Tile on the board.
     * @param y The Y coordinate of the Tile on the board.
     * @param startingPiece The Piece on the Tile at creation.
     * */
    public TileImpl(Piece startingPiece, int x, int y) {

        xProperty = new SimpleIntegerProperty(x);
        yProperty = new SimpleIntegerProperty(y);

        pieceProperty = new SimpleObjectProperty<>(startingPiece);

    }

    /**
     * Copy constructor for Tile. Not used.
     * */
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
