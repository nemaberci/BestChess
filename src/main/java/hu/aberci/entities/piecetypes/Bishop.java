package hu.aberci.entities.piecetypes;

import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.Tile;
import hu.aberci.util.interfaces.MoveGenerator;

import java.util.HashSet;
import java.util.Set;

/**
 * MoveGenerator implementation for the bishop
 * */
public class Bishop implements MoveGenerator {

    /**
     * A bishop can only move diagonally. For move info see {@link MoveGenerator}.
     * */
    @Override
    public Set<Tile> moveGenerator(BoardState boardState, Piece piece) {
        int pieceX = piece.getTileProperty().get().getXProperty().get();
        int pieceY = piece.getTileProperty().get().getYProperty().get();

        Set<Tile> moves = new HashSet<>();

        for (int i = -Math.min(pieceX, pieceY); i <= Math.min(7-pieceX, 7-pieceY); i++) {

            moves.add(
                    boardState.getTilesProperty().get()
                            .get(pieceX + i)
                            .get(pieceY + i)
            );

        }

        for (int i = -Math.min(pieceX, 7-pieceY); i <= Math.min(7-pieceX, pieceY); i++) {

            moves.add(
                    boardState.getTilesProperty().get()
                            .get(pieceX + i)
                            .get(pieceY - i)
            );

        }

        return moves;
    }
}
