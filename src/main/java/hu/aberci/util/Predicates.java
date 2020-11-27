package hu.aberci.util;

import hu.aberci.entities.data.BoardStateImpl;
import hu.aberci.entities.data.MoveImpl;
import hu.aberci.entities.interfaces.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Predicates used for determining if a move is valid in a position.
 * All predicates that filter moves
 * ({@link Predicates#pieceOnTileIsNotOfPlayerColor}, {@link Predicates#isTargetTileReachableFromPiece}, {@link Predicates#isPlayerNotInCheckAfterMove})
 * are made so that they return true if the move is valid and false otherwise.
 * */
public class Predicates {

    /**
     * No piece can move to a tile that has a friendly piece on it. This Predicate checks if a given move
     * would move the piece to a tile with a friendly piece on it. Returns {@code true} if it does not
     * contain a friendly piece and {@code false} if it does.
     * */
    public static Predicate<Move> pieceOnTileIsNotOfPlayerColor = (move) -> {
        if (move.getTargetTile().getPieceProperty().get() == null) {
            return true;
        }
        return move.getTargetTile().getPieceProperty().get().getPlayerColorProperty().get() != move.getPiece().getPlayerColorProperty().get();
    };

    /**
     * Every piece (except for the knight) can only move to connected tiles. If it can not
     * move to a tile then it can not move to the tile behind it. This Predicate checks if the target Tile
     * of a move can be reached from the sourceTile. Returns {@code true} if is and {@code false} if it is not.
     * */
    public static Predicate <Move> isTargetTileReachableFromPiece = (move) -> {

        int targetTileX = move.getTargetTile().getXProperty().get();
        int targetTileY = move.getTargetTile().getYProperty().get();
        int sourceTileX = move.getPiece().getTileProperty().get().getXProperty().get();
        int sourceTileY = move.getPiece().getTileProperty().get().getYProperty().get();

        while (sourceTileX != targetTileX || sourceTileY != targetTileY) {

            if (sourceTileX < targetTileX) {
                sourceTileX++;
            }
            if (sourceTileX > targetTileX) {
                sourceTileX--;
            }
            if (sourceTileY < targetTileY) {
                sourceTileY++;
            }
            if (sourceTileY > targetTileY) {
                sourceTileY--;
            }

            if (sourceTileX == targetTileX && sourceTileY == targetTileY) {
                break;
            }

            if (move.getBoardState().getTilesProperty().get().get(sourceTileX).get(sourceTileY).getPieceProperty().get() != null) {

                return false;

            }

        }

        return true;

    };

    /**
     * Checks if the player whose turn it currently is is in check or not. This is not to be used
     * to filter moves. Returns {@code true} if the player is in check and {@code false} if they are not.
     * */
    public static Predicate<BoardState> isPlayerInCheck = boardState -> {

        Tile tileOfKing = null;

        for (Piece piece: boardState.getPiecesProperty().get().get(boardState.getPlayerTurnProperty().get())) {

            if (piece.getPieceTypeProperty().get() == PieceType.KING) {

                tileOfKing = piece.getTileProperty().get();

            }

        }

        PlayerColor opponentPlayerColor = (boardState.getPlayerTurnProperty().get() == PlayerColor.BLACK ? PlayerColor.WHITE : PlayerColor.BLACK);

        for (Piece opponentPiece: boardState.getPiecesProperty().get().get(opponentPlayerColor)) {

            Set<Tile> possibleTiles = opponentPiece.getPieceTypeProperty().get()
                    .moveGenerator.moveGenerator(boardState, opponentPiece);

            Stream<Move> possibleMoves = possibleTiles.stream().map(
                    tile -> new MoveImpl(boardState, tile, opponentPiece, false)
            );

            possibleMoves = possibleMoves
                    .filter(pieceOnTileIsNotOfPlayerColor);
            if (!PieceType.KNIGHT.equals(opponentPiece.getPieceTypeProperty().get())) {
                    possibleMoves = possibleMoves.filter(isTargetTileReachableFromPiece);
            }

            Set<Tile> reachableTiles = possibleMoves.map(
                    Move::getTargetTile
            ).collect(Collectors.toSet());

            if (reachableTiles.contains(
                    tileOfKing
            )) {
                return true;
            }

        }

        return false;

    };

    /**
     * A move is only legal if after making it, the player who made is is not in check. We check this
     * by creating a new BoardState, make the move and check if the player is in check after it.
     * Returns {@code true} if the move is legal and {@code false} otherwise.
     * */
    public static Predicate<Move> isPlayerNotInCheckAfterMove = (move) -> {

        // We create new boardstate

        BoardState newBoardState = new BoardStateImpl(move.getBoardState());

        int targetTileX = move.getTargetTile().getXProperty().get();
        int targetTileY = move.getTargetTile().getYProperty().get();
        int sourceTileX = move.getPiece().getTileProperty().get().getXProperty().get();
        int sourceTileY = move.getPiece().getTileProperty().get().getYProperty().get();

        Piece sourcePieceInNewBoard = null;

        for (Piece piece: newBoardState.getPiecesProperty().get().get(move.getPiece().getPlayerColorProperty().get())) {

            if (piece.getTileProperty().get().getXProperty().get() == sourceTileX && piece.getTileProperty().get().getYProperty().get() == sourceTileY) {

                sourcePieceInNewBoard = piece;
                break;

            }

        }

        if (newBoardState.getTilesProperty().get(targetTileX).get(targetTileY).getPieceProperty().get() != null) {

            newBoardState.getPiecesProperty().get(
                    PlayerColor.WHITE.equals(sourcePieceInNewBoard.getPlayerColorProperty().get()) ? PlayerColor.BLACK : PlayerColor.WHITE
            ).remove(
                    newBoardState.getTilesProperty().get(targetTileX).get(targetTileY).getPieceProperty().get()
            );

        }

        newBoardState.getTilesProperty().get().get(sourceTileX).get(sourceTileY).getPieceProperty().set(null);
        newBoardState.getTilesProperty().get().get(targetTileX).get(targetTileY).getPieceProperty().set(sourcePieceInNewBoard);

        sourcePieceInNewBoard.getTileProperty().set(
                newBoardState.getTilesProperty().get(targetTileX).get(targetTileY)
        );

        // we check if opponent has check now

        return !isPlayerInCheck.test(newBoardState);

    };

}
