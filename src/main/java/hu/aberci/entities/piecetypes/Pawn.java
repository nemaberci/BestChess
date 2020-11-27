package hu.aberci.entities.piecetypes;

import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.PlayerColor;
import hu.aberci.entities.interfaces.Tile;
import hu.aberci.util.interfaces.MoveGenerator;

import java.util.HashSet;
import java.util.Set;

/**
 * MoveGenerator implementation for the pawn.
 * */
public class Pawn implements MoveGenerator {

    /**
     * A pawn can move forward once, can take diagonally forward and can move twice if it is in its starting tile. For more info see {@link MoveGenerator}.
     * */
    @Override
    public Set<Tile> moveGenerator(BoardState boardState, Piece piece) {

            int pawnStartingX = PlayerColor.WHITE.equals(piece.getPlayerColorProperty().get()) ? 1 : 6;

            boolean canMoveOnce = piece.getTileProperty().get().getXProperty().get() != 7 && piece.getTileProperty().get().getXProperty().get() != 0;
            boolean canMoveTwice = piece.getTileProperty().get().getXProperty().get() == pawnStartingX;

            Set<Tile> moves = new HashSet<>();

            int pieceX = piece.getTileProperty().get().getXProperty().get();
            int pieceY = piece.getTileProperty().get().getYProperty().get();
            Tile wantToMoveTo;

            if (canMoveOnce) {

                wantToMoveTo = boardState.getTilesProperty().get()
                        .get((PlayerColor.WHITE.equals(piece.getPlayerColorProperty().get()) ? pieceX + 1 : pieceX - 1))
                        .get(pieceY);

                if (wantToMoveTo.getPieceProperty().get() == null) {

                    moves.add(
                            wantToMoveTo
                    );

                }

                if (canMoveTwice && wantToMoveTo.getPieceProperty().get() == null) {

                    wantToMoveTo = boardState.getTilesProperty().get()
                            .get((PlayerColor.WHITE.equals(piece.getPlayerColorProperty().get()) ? pieceX + 2 : pieceX - 2))
                            .get(pieceY);

                    if (wantToMoveTo.getPieceProperty().get() == null) {

                        moves.add(
                                wantToMoveTo
                        );

                    }

                }

                boolean canTakeToTheLeft = false;
                if (pieceY != 0) {
                    canTakeToTheLeft = boardState.getTilesProperty().get()
                            .get((PlayerColor.WHITE.equals(piece.getPlayerColorProperty().get()) ? pieceX + 1 : pieceX - 1))
                            .get(pieceY - 1)
                            .getPieceProperty()
                            .get() != null;
                }

                boolean canTakeToTheRight = false;
                if (pieceY != 7) {
                    canTakeToTheRight = boardState.getTilesProperty().get()
                            .get((PlayerColor.WHITE.equals(piece.getPlayerColorProperty().get()) ? pieceX + 1 : pieceX - 1))
                            .get(pieceY + 1)
                            .getPieceProperty()
                            .get() != null;
                }

                if (canTakeToTheLeft) {

                    moves.add(
                            boardState.getTilesProperty().get()
                                    .get((PlayerColor.WHITE.equals(piece.getPlayerColorProperty().get()) ? pieceX + 1 : pieceX - 1))
                                    .get(pieceY - 1)
                    );

                }

                if (canTakeToTheRight) {

                    moves.add(
                            boardState.getTilesProperty().get()
                                    .get((PlayerColor.WHITE.equals(piece.getPlayerColorProperty().get()) ? pieceX + 1 : pieceX - 1))
                                    .get(pieceY + 1)
                    );

                }

            }

            return moves;
    }

}
