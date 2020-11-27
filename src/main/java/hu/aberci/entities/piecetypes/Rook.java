package hu.aberci.entities.piecetypes;

import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.Tile;
import hu.aberci.util.interfaces.MoveGenerator;

import java.util.HashSet;
import java.util.Set;

/**
 * MoveGenerator implementation for the rook.
 * */
public class Rook implements MoveGenerator {

    /**
     * A rook can move anywhere in a straight line. For more info see {@link MoveGenerator}.
     * */
    @Override
    public Set<Tile> moveGenerator(BoardState boardState, Piece piece) {
        Set<Tile> moves = new HashSet<>();

        int pieceX = piece.getTileProperty().get().getXProperty().get();
        int pieceY = piece.getTileProperty().get().getYProperty().get();

        for (int x = 0; x < 8; x++) {

            if (x == pieceX) {
                continue;
            }

            moves.add(
                    boardState.getTilesProperty().get()
                            .get(x)
                            .get(pieceY)
            );

        }

        for (int y = 0; y < 8; y++) {

            if (y == pieceY) {
                continue;
            }

            moves.add(
                    boardState.getTilesProperty().get()
                            .get(pieceX)
                            .get(y)
            );

        }

        return moves;
    }
}
