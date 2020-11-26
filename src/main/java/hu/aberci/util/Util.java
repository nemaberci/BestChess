package hu.aberci.util;

import hu.aberci.entities.data.MoveImpl;
import hu.aberci.entities.interfaces.*;
import javafx.scene.image.Image;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {

    public static Set<Tile> getAllLegalMoves(BoardState boardState, Piece piece) {

        Set<Tile> possibleTiles = piece.getPieceTypeProperty().get().possibleMoves.possibleMoves(boardState, piece);
        Stream<MoveImpl> possibleMoves = possibleTiles.stream()
                .map(tile -> new MoveImpl(boardState, tile, piece, false))
                .filter(Predicates.pieceOnTileIsNotOfPlayerColor)
                .filter(Predicates.isPlayerNotInCheckAfterMove);

        /*System.out.println("Moves after first filter: " +
                possibleTiles.stream()
                .map(tile -> new MoveImpl(boardState, tile, piece, false))
                .filter(Predicates.pieceOnTileIsNotOfPlayerColor).collect(Collectors.toSet()).size()
        );

        System.out.println("Moves after second filter: " + possibleTiles.stream()
                .map(tile -> new MoveImpl(boardState, tile, piece, false))
                .filter(Predicates.pieceOnTileIsNotOfPlayerColor)
                .filter(Predicates.isPlayerNotInCheckAfterMove).collect(Collectors.toSet()).size()
        );*/

        if (piece.getPieceTypeProperty().get() != PieceType.KNIGHT) {

            possibleMoves = possibleMoves.filter(Predicates.isTargetTileReachableFromPiece);

            /*System.out.println("Moves after (third) filter: " + possibleTiles.stream()
                    .map(tile -> new MoveImpl(boardState, tile, piece, false))
                    .filter(Predicates.pieceOnTileIsNotOfPlayerColor)
                    .filter(Predicates.isPlayerNotInCheckAfterMove)
                    .filter(Predicates.isTargetTileReachableFromPiece).collect(Collectors.toSet()).size()
            );*/

        }

        possibleTiles = possibleMoves.map(MoveImpl::getTargetTile).collect(Collectors.toSet());

        return  possibleTiles;

    }

    public static Image getPieceImage(Piece piece) {

        String imageUrl = "images/";

        imageUrl = imageUrl.concat(piece.getPlayerColorProperty().get() == PlayerColor.WHITE ? "white" : "black");
        imageUrl = imageUrl.concat("_");
        imageUrl = imageUrl.concat(piece.getPieceTypeProperty().get().name);

        imageUrl = imageUrl.concat(".png");

        // System.out.println("Trying to load: " + imageUrl);

        try {
            Image image = new Image(imageUrl);
            // System.out.println("Success");
            return image;
        } catch (Exception exception){

            // exception.printStackTrace();

            return null;
        }

    }

    public static Image getPromotionPieceImage(PieceType pieceType) {

        String imageUrl = "images/bw_";

        imageUrl = imageUrl.concat(pieceType.name).concat(".png");

        try {
            Image image = new Image(imageUrl);
            // System.out.println("Success");
            return image;
        } catch (Exception exception){

            // exception.printStackTrace();

            return null;
        }

    }

}
