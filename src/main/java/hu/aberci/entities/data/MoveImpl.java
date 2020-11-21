package hu.aberci.entities.data;

import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Move;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.Tile;
import lombok.Getter;

public class MoveImpl implements Move {

    @Getter
    BoardState boardState;

    @Getter
    Tile targetTile;

    @Getter
    Piece piece;

    public MoveImpl(BoardState b, Tile t, Piece p) {

        boardState = b;
        targetTile = t;
        piece = p;

    }

}
