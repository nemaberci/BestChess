package hu.aberci.util;

import hu.aberci.entities.data.MoveImpl;
import hu.aberci.entities.interfaces.*;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generic Util class housing utility functions
 * */
public class Util {

    /**
     * Returns all the legal moves for a given Piece.
     *
     * @param boardState The BoardState that the Piece belongs to
     * @param piece The Piece whose moves are to be returned.
     * @return All tiles that can be reached by the piece legally.
     * */
    public static Set<Tile> getAllLegalMoves(BoardState boardState, Piece piece) {

        Set<Tile> baseTiles = piece.getPieceTypeProperty().get().moveGenerator.moveGenerator(boardState, piece);
        Stream<MoveImpl> baseMoves = baseTiles.stream()
                .map(tile -> new MoveImpl(boardState, tile, piece, false))
                .filter(Predicates.pieceOnTileIsNotOfPlayerColor)
                .filter(Predicates.isPlayerNotInCheckAfterMove);

        if (piece.getPieceTypeProperty().get() != PieceType.KNIGHT) {

            baseMoves = baseMoves.filter(Predicates.isTargetTileReachableFromPiece);

        }

        baseTiles = baseMoves.map(MoveImpl::getTargetTile).collect(Collectors.toSet());

        /*
         * We add en passant logic
         * en passant can happen if one is true:
         *   - a black pawn moved from (x, y) to (x-2, y) and there exists a white pawn on (x, y-1) or (x, y+1)
         *   - a white pawn moved from (x, y) to (x+2, y) and there exists a black pawn on (x, y-1) or (x, y+1)
         * */

        if (boardState.getMovesProperty().get().size() != 0) {

            Move lastMove = null;

            lastMove = boardState.getMovesProperty().get().get(
                    boardState.getMovesProperty().get().size() - 1);

            if (PieceType.PAWN.equals(lastMove.getPiece().getPieceTypeProperty().get()) &&
                    Math.abs(lastMove.getSourceTile().getXProperty().get() - lastMove.getTargetTile().getXProperty().get()) == 2) {

                if (piece.getTileProperty().get().getXProperty().get() == lastMove.getTargetTile().getXProperty().get() &&
                        Math.abs(piece.getTileProperty().get().getYProperty().get() - lastMove.getSourceTile().getYProperty().get()) == 1) {

                    // only now can we take en passant

                    baseTiles.add(
                            boardState.getTilesProperty().get()
                                    .get(
                                            (lastMove.getSourceTile().getXProperty().get() + lastMove.getTargetTile().getXProperty().get()) / 2
                                    )
                                    .get(
                                            lastMove.getSourceTile().getYProperty().get()
                                    )
                    );

                }

            }

        }

        /*
         * We add castling logic
         * A king can castle if
         *   - he has not moved and
         *   - the rook he wants to castle with has not moved
         *   - and there are no pieces in between
         *  */

        if (PieceType.KING.equals(piece.getPieceTypeProperty().get())) {

            boolean hasMoved = false;

            for (Move move: boardState.getMovesProperty().get()) {

                if (move.getPiece().equals(piece)) {

                    hasMoved = true;
                    break;

                }

            }

            if (!hasMoved) {

                // We try to find the rooks to castle with

                for (Piece potentialRook: boardState.getPiecesProperty().get(
                        piece.getPlayerColorProperty().get()
                )) {

                    if (PieceType.ROOK.equals(potentialRook.getPieceTypeProperty().get())) {

                        boolean rookHasMoved = false;

                        for (Move move: boardState.getMovesProperty().get()) {

                            if (move.getPiece().equals(potentialRook)) {

                                rookHasMoved = true;
                                break;

                            }

                        }

                        // At this point we have found a rook that has not moved yet and so has the king
                        // Now we have to check every square between them to make sure
                        // they can switch places

                        int kingX = piece.getTileProperty().get().getXProperty().get();
                        int kingY = piece.getTileProperty().get().getYProperty().get();
                        int rookY = potentialRook.getTileProperty().get().getYProperty().get();

                        boolean canCastle = true;

                        while (rookY != kingY) {

                            if (rookY > kingY) {
                                rookY--;
                            }
                            if (rookY < kingY) {
                                rookY++;
                            }

                            if (rookY == kingY) {
                                break;
                            }

                            if (boardState.getTilesProperty().get(
                                    kingX
                            ).get(
                                    rookY
                            ).getPieceProperty().get() != null) {

                                canCastle = false;
                                break;

                            }

                        }

                        // Castling is only possible if the king is not in check in any of the 3 squares
                        // (king's square, the square he is moving to, and the one in-between)

                        rookY = potentialRook.getTileProperty().get().getYProperty().get();

                        List<Move> theoreticalMoves = new ArrayList<>();
                        int futureY = (rookY > kingY) ? kingY + 2 : kingY - 2;

                        theoreticalMoves.add(
                                new MoveImpl(
                                        boardState,
                                        boardState.getTilesProperty().get(kingX).get(kingY),
                                        boardState.getTilesProperty().get(kingX).get(futureY),
                                        piece,
                                        false
                                )
                        );

                        theoreticalMoves.add(
                                new MoveImpl(
                                        boardState,
                                        boardState.getTilesProperty().get(kingX).get(kingY),
                                        boardState.getTilesProperty().get(kingX).get((rookY > kingY) ? kingY + 1 : kingY - 1),
                                        piece,
                                        false
                                )
                        );

                        theoreticalMoves.add(
                                new MoveImpl(
                                        boardState,
                                        boardState.getTilesProperty().get(kingX).get(kingY),
                                        boardState.getTilesProperty().get(kingX).get(kingY),
                                        piece,
                                        false
                                )
                        );

                        for (Move theoreticalMove: theoreticalMoves) {

                            if (!Predicates.isPlayerNotInCheckAfterMove.test(theoreticalMove)) {

                                canCastle = false;

                            }

                        }

                        if (canCastle) {

                            baseTiles.add(
                                    boardState.getTilesProperty().get(
                                            kingX
                                    ).get(
                                            futureY
                                    )
                            );

                        }

                    }

                }

            }

        }

        return  baseTiles;

    }

    /**
     * Returns the loaded image for a given piece.
     *
     * @param piece The Piece whose image is to be loaded.
     * @return The image loaded.
     * */
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

    /**
     * Returns the loaded image of a promotion piece. Promotion pieces do not have a color, so
     * this function does not take a Piece but a PieceType.
     *
     * @param pieceType The PieceType whose image is to be returned.
     * @return The image of the promotion piece.
     * */
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
