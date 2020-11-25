package hu.aberci.util;

import hu.aberci.entities.data.BoardStateImpl;
import hu.aberci.entities.data.MoveImpl;
import hu.aberci.entities.data.TileImpl;
import hu.aberci.entities.interfaces.*;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Predicates {

    public static Predicate<Move> pieceOnTileIsNotOfPlayerColor = (move) -> {
        if (move.getTargetTile().getPieceProperty().get() == null) {
            return true;
        }
        return move.getTargetTile().getPieceProperty().get().getPlayerColorProperty().get() != move.getPiece().getPlayerColorProperty().get();
    };

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
                    .possibleMoves.possibleMoves(boardState, opponentPiece);

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
