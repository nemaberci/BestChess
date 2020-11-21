package hu.aberci.util;

import hu.aberci.entities.data.BoardStateImpl;
import hu.aberci.entities.data.MoveImpl;
import hu.aberci.entities.data.TileImpl;
import hu.aberci.entities.interfaces.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Predicates {

    public static Predicate<Move> pieceOnTileIsNotOfPlayerColor = (move) -> move.getTargetTile().getPieceProperty().get().getPlayerColorProperty().get() != move.getPiece().getPlayerColorProperty().get();

    private static boolean isTileInMoves(Set<Tile> moves, int x, int y) {

        for (Tile tile: moves) {

            if (tile.getXProperty().get() == x && tile.getYProperty().get() == y) {
                return true;
            }

        }

        return false;

    }

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

            Set<Move> possibleMoves = possibleTiles.stream().map(
                    tile -> new MoveImpl(boardState, tile, opponentPiece)
            ).collect(Collectors.toSet());

            possibleMoves = possibleMoves.stream()
                    .filter(pieceOnTileIsNotOfPlayerColor)
                    .filter(isTargetTileReachableFromPiece)
                    .collect(Collectors.toSet());

            Set<Tile> reachableTiles = possibleMoves.stream().map(
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

        newBoardState.getTilesProperty().get().get(targetTileX).get(targetTileY).getPieceProperty().set(sourcePieceInNewBoard);
        newBoardState.getTilesProperty().get().get(sourceTileX).get(sourceTileY).getPieceProperty().set(null);

        // we check if opponent has check now

        return !isPlayerInCheck.test(newBoardState);

    };

}
