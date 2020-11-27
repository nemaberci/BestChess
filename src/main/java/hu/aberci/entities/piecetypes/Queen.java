package hu.aberci.entities.piecetypes;

import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.Tile;
import hu.aberci.util.interfaces.MoveGenerator;

import java.util.Set;

/**
 * MoveGenerator implementation for the queen.
 * */
public class Queen implements MoveGenerator {

    /**
     * The queen can move anywhere where a rook or a bishop could move from this position. For more info see {@link MoveGenerator}.
     * */
    @Override
    public Set<Tile> moveGenerator(BoardState boardState, Piece piece) {

        Set<Tile> moves = new Rook().moveGenerator(boardState, piece);
        moves.addAll(new Bishop().moveGenerator(boardState, piece));
        return moves;

    }
}
