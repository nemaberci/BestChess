package hu.aberci.entities.piecetypes;

import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.Tile;
import hu.aberci.util.interfaces.MoveGenerator;

import java.util.HashSet;
import java.util.Set;

/**
 * MoveGenerator implementation for the king.
 * */
public class King implements MoveGenerator {

    /**
     * A King can move in any direction but only one tile. For more info see {@link MoveGenerator}.
     * */
    @Override
    public Set<Tile> moveGenerator(BoardState boardState, Piece piece) {
        int pieceX = piece.getTileProperty().get().getXProperty().get();
        int pieceY = piece.getTileProperty().get().getYProperty().get();

        Set<Tile> moves = new HashSet<>();

        boolean canMoveForwardOnce = pieceX < 7;
        boolean canMoveBackwardOnce = pieceX > 0;
        boolean canMoveLeftOnce = pieceY > 0;
        boolean canMoveRightOnce = pieceY < 7;

        if (canMoveForwardOnce) {

            moves.add(
                    boardState.getTilesProperty().get()
                            .get(pieceX + 1)
                            .get(pieceY)
            );

            if (canMoveLeftOnce) {

                moves.add(
                        boardState.getTilesProperty().get()
                                .get(pieceX + 1)
                                .get(pieceY - 1)
                );

            }

            if (canMoveRightOnce) {

                moves.add(
                        boardState.getTilesProperty().get()
                                .get(pieceX + 1)
                                .get(pieceY + 1)
                );

            }

        }

        if (canMoveBackwardOnce) {

            moves.add(
                    boardState.getTilesProperty().get()
                            .get(pieceX - 1)
                            .get(pieceY)
            );

            if (canMoveLeftOnce) {

                moves.add(
                        boardState.getTilesProperty().get()
                                .get(pieceX - 1)
                                .get(pieceY - 1)
                );

            }

            if (canMoveRightOnce) {

                moves.add(
                        boardState.getTilesProperty().get()
                                .get(pieceX - 1)
                                .get(pieceY + 1)
                );

            }

        }

        if (canMoveLeftOnce) {

            moves.add(
                    boardState.getTilesProperty().get()
                            .get(pieceX)
                            .get(pieceY - 1)
            );

        }

        if (canMoveRightOnce) {

            moves.add(
                    boardState.getTilesProperty().get()
                            .get(pieceX)
                            .get(pieceY + 1)
            );

        }

        return moves;
    }
}
