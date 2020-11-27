package hu.aberci.entities.piecetypes;

import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.Tile;
import hu.aberci.util.interfaces.MoveGenerator;

import java.util.HashSet;
import java.util.Set;

/**
 * MoveGenerator implementation for the knight.
 * */
public class Knight implements MoveGenerator {

    /**
     * A knight can move to any square that is 2 tiles in one direction and 1 tile in another (that is not backwards). For more info see {@link MoveGenerator}.
     * */
    @Override
    public Set<Tile> moveGenerator(BoardState boardState, Piece piece) {
        int pieceX = piece.getTileProperty().get().getXProperty().get();
        int pieceY = piece.getTileProperty().get().getYProperty().get();

        Set<Tile> moves = new HashSet<>();

        boolean canMoveForwardOnce = pieceX < 7;
        boolean canMoveForwardTwice = pieceX < 6;
        boolean canMoveBackwardOnce = pieceX > 0;
        boolean canMoveBackwardTwice = pieceX > 1;
        boolean canMoveLeftOnce = pieceY > 0;
        boolean canMoveLeftTwice = pieceY > 1;
        boolean canMoveRightOnce = pieceY < 7;
        boolean canMoveRightTwice = pieceY < 6;

        if (canMoveForwardTwice && canMoveLeftOnce) {
            moves.add(
                    boardState.getTilesProperty().get()
                            .get(pieceX + 2)
                            .get(pieceY - 1)
            );
        }

        if (canMoveForwardTwice && canMoveRightOnce) {
            moves.add(
                    boardState.getTilesProperty().get()
                            .get(pieceX + 2)
                            .get(pieceY + 1)
            );
        }

        if (canMoveForwardOnce && canMoveLeftTwice) {
            moves.add(
                    boardState.getTilesProperty().get()
                            .get(pieceX + 1)
                            .get(pieceY - 2)
            );
        }

        if (canMoveForwardOnce && canMoveRightTwice) {
            moves.add(
                    boardState.getTilesProperty().get()
                            .get(pieceX + 1)
                            .get(pieceY + 2)
            );
        }

        if (canMoveBackwardTwice && canMoveLeftOnce) {
            moves.add(
                    boardState.getTilesProperty().get()
                            .get(pieceX - 2)
                            .get(pieceY - 1)
            );
        }

        if (canMoveBackwardTwice && canMoveRightOnce) {
            moves.add(
                    boardState.getTilesProperty().get()
                            .get(pieceX - 2)
                            .get(pieceY + 1)
            );
        }

        if (canMoveBackwardOnce && canMoveLeftTwice) {
            moves.add(
                    boardState.getTilesProperty().get()
                            .get(pieceX - 1)
                            .get(pieceY - 2)
            );
        }

        if (canMoveBackwardOnce && canMoveRightTwice) {
            moves.add(
                    boardState.getTilesProperty().get()
                            .get(pieceX - 1)
                            .get(pieceY + 2)
            );
        }

        return moves;
    }
}
