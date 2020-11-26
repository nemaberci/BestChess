package hu.aberci.entities.data;

import hu.aberci.entities.interfaces.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Data class responsible for storing Moves.
 * No special logic is implemented in this class, for more info see: {@link Move}
 * */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class MoveImpl implements Move {

    BoardState boardState;

    Tile targetTile;

    Tile sourceTile;

    Piece piece;

    boolean capture;

    PieceType promotingTo;

    /**
     * Constructor that creates a new move implementation without using a different sourceTile.
     * The sourceTile is the Piece's current tile.
     * This constructor is used only for moved that are not promotions.
     *
     * @param b The BoardState where this move occured.
     * @param p The Piece that is moved
     * @param t The Tile that it moved to
     * @param isCapture Determines if this move is a capture
     * */
    public MoveImpl(BoardState b, Tile t, Piece p, boolean isCapture) {

        boardState = b;
        targetTile = t;
        piece = p;
        sourceTile = p.getTileProperty().get();
        capture = isCapture;
        promotingTo = null;

    }

    /**
     * Constructor that creates a new move implementation using a designated sourceTile.
     * This constructor is used only for moved that are not promotions.
     *
     * @param b The BoardState where this move occured.
     * @param p The Piece that is moved
     * @param source The Tile that it moved from
     * @param target The Tile that it moved to
     * @param isCapture Determines if this move is a capture
     * */
    public MoveImpl(BoardState b, Tile source, Tile target, Piece p, boolean isCapture) {

        targetTile = target;
        sourceTile = source;
        piece = p;
        boardState = b;
        capture = isCapture;
        promotingTo = null;

    }

    /**
     * Constructor used for creating Promotion moves. Uses the other constructor without a designated
     * sourceTile.
     *
     * @param b The BoardState where this move occured.
     * @param p The Piece that is moved
     * @param t The Tile that it moved from
     * @param isCapture Determines if this move is a capture
     * @param promotion The PieceType that the Piece is promoting to.
     * */
    public MoveImpl(BoardState b, Tile t, Piece p, boolean isCapture, PieceType promotion) {

        this(b, t, p, isCapture);

        promotingTo = promotion;

    }

}
