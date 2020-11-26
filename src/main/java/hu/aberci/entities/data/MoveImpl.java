package hu.aberci.entities.data;

import hu.aberci.entities.interfaces.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

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

    public MoveImpl(BoardState b, Tile t, Piece p, boolean isCapture) {

        boardState = b;
        targetTile = t;
        piece = p;
        sourceTile = p.getTileProperty().get();
        capture = isCapture;
        promotingTo = null;

    }

    public MoveImpl(BoardState b, Tile sourceTile1, Tile targetTile1, Piece p, boolean isCapture) {

        targetTile = targetTile1;
        sourceTile = sourceTile1;
        piece = p;
        boardState = b;
        capture = isCapture;
        promotingTo = null;

    }

    public MoveImpl(BoardState b, Tile t, Piece p, boolean isCapture, PieceType promotion) {

        this(b, t, p, isCapture);

        promotingTo = promotion;

    }

}
