package hu.aberci.util;

import hu.aberci.entities.data.MoveImpl;
import hu.aberci.entities.interfaces.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {

    public static Set<Tile> getAllLegalMoves(BoardState boardState, Piece piece) {

        Set<Tile> possibleTiles = piece.getPieceTypeProperty().get().possibleMoves.possibleMoves(boardState, piece);
        Stream<MoveImpl> possibleMoves = possibleTiles.stream()
                .map(tile -> new MoveImpl(boardState, tile, piece))
                .filter(Predicates.pieceOnTileIsNotOfPlayerColor)
                .filter(Predicates.isPlayerNotInCheckAfterMove);

        if (piece.getPieceTypeProperty().get() != PieceType.KNIGHT) {

            possibleMoves = possibleMoves.filter(Predicates.isTargetTileReachableFromPiece);

        }

        possibleTiles = possibleMoves.map(Move::getTargetTile).collect(Collectors.toSet());

        return  possibleTiles;

    }

}
