package hu.aberci.util;

import hu.aberci.entities.interfaces.PlayerColor;
import hu.aberci.entities.interfaces.Tile;
import hu.aberci.util.interfaces.PossibleMoves;

import java.util.HashSet;
import java.util.Set;

public class PossibleMovesImpls {

    public static PossibleMoves possiblePawnMoves = (boardState, piece) -> {

        int pawnStartingX = PlayerColor.WHITE.equals(piece.getPlayerColorProperty().get()) ? 1 : 6;

        boolean canMoveTwice = piece.getTileProperty().get().getXProperty().get() == pawnStartingX;

        Set<Tile> moves = new HashSet<>();

        int pieceX = piece.getTileProperty().get().getXProperty().get();
        int pieceY = piece.getTileProperty().get().getYProperty().get();

        Tile wantToMoveTo = boardState.getTilesProperty().get()
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

        return moves;

    };

    public static PossibleMoves possibleRookMoves = (boardState, piece) -> {

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

    };

    public static PossibleMoves possibleBishopMoves = (boardState, piece) -> {

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

    };

    public static PossibleMoves possibleKnightMoves = (boardState, piece) -> {

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

    };

    public static PossibleMoves possibleKingMoves = (boardState, piece) -> {

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

    };

    public static PossibleMoves possibleQueenMoves = (boardState, piece) -> {

        Set<Tile> moves = possibleRookMoves.possibleMoves(boardState, piece);
        moves.addAll(possibleBishopMoves.possibleMoves(boardState, piece));
        return moves;

    };

}
