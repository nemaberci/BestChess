package hu.aberci.controllers;

import hu.aberci.entities.data.BoardStateImpl;
import hu.aberci.entities.events.ChessBoardEvent;
import hu.aberci.entities.data.MoveImpl;
import hu.aberci.entities.data.TileImpl;
import hu.aberci.entities.events.ChessPieceEvent;
import hu.aberci.entities.interfaces.*;
import hu.aberci.exceptions.*;
import hu.aberci.util.Predicates;
import hu.aberci.util.Util;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChessGameController {

    @Getter
    protected ObjectProperty<BoardState> boardStateProperty;
    @Setter
    private Parent parent;

    private static Integer currentPieceId = 0;

    public static Integer nextPieceId() {

        Integer toReturn = currentPieceId;
        currentPieceId++;

        return toReturn;

    }

    public ChessGameController(Parent parent1, int startingTime, int increment) {

        parent = parent1;

        boardStateProperty = new SimpleObjectProperty<>(
                new BoardStateImpl(startingTime, increment, true, true)
        );

    }

    public ChessGameController(Parent parent1, BoardState boardState) {

        parent = parent1;

        boardStateProperty = new SimpleObjectProperty<>(
                boardState
        );

    }

    public boolean doesCurrentPlayerHaveAnyMoves() {

        for (Piece piece: boardStateProperty.get().getPiecesProperty().get().get(boardStateProperty.get().getPlayerTurnProperty().get())) {

            Set<Tile> legalMoves = Util.getAllLegalMoves(boardStateProperty.get(), piece);

            if (legalMoves.size() != 0) {
                return true;
            }

        }

        return false;

    }

    public boolean isPlayerInCheck() {

        return Predicates.isPlayerInCheck.test(boardStateProperty.get());

    }

    public boolean isPlayerInDraw() {

        return !isPlayerInCheck() && !doesCurrentPlayerHaveAnyMoves();

    }

    public boolean isPlayerInCheckmate() {

        return isPlayerInCheck() && !doesCurrentPlayerHaveAnyMoves();

    }

    public Set<Tile> getLegalMovesOf(Piece piece) {

        Set<Tile> base = Util.getAllLegalMoves(boardStateProperty.get(), piece);

        /*
        * We add en passant logic
        * en passant can happen if one is true:
        *   - a black pawn moved from (x, y) to (x-2, y) and there exists a white pawn on (x, y-1) or (x, y+1)
        *   - a white pawn moved from (x, y) to (x+2, y) and there exists a black pawn on (x, y-1) or (x, y+1)
        * */

        if (boardStateProperty.get().getMovesProperty().get().size() != 0) {

            Move lastMove = null;

            lastMove = boardStateProperty.get().getMovesProperty().get().get(
                    boardStateProperty.get().getMovesProperty().get().size() - 1);

            if (PieceType.PAWN.equals(lastMove.getPiece().getPieceTypeProperty().get()) &&
                    Math.abs(lastMove.getSourceTile().getXProperty().get() - lastMove.getTargetTile().getXProperty().get()) == 2) {

                if (piece.getTileProperty().get().getXProperty().get() == lastMove.getTargetTile().getXProperty().get() &&
                        Math.abs(piece.getTileProperty().get().getYProperty().get() - lastMove.getSourceTile().getYProperty().get()) == 1) {

                    // only now can we take en passant

                    base.add(
                            boardStateProperty.get().getTilesProperty().get()
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

            for (Move move: boardStateProperty.get().getMovesProperty().get()) {

                if (move.getPiece().equals(piece)) {

                    hasMoved = true;
                    break;

                }

            }

            if (!hasMoved) {

                // We try to find the rooks to castle with

                for (Piece potentialRook: boardStateProperty.get().getPiecesProperty().get(
                        piece.getPlayerColorProperty().get()
                )) {

                    if (PieceType.ROOK.equals(potentialRook.getPieceTypeProperty().get())) {

                        boolean rookHasMoved = false;

                        for (Move move: boardStateProperty.get().getMovesProperty().get()) {

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

                            if (boardStateProperty.get().getTilesProperty().get(
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
                                        boardStateProperty.get(),
                                        boardStateProperty.get().getTilesProperty().get(kingX).get(kingY),
                                        boardStateProperty.get().getTilesProperty().get(kingX).get(futureY),
                                        piece
                                )
                        );

                        theoreticalMoves.add(
                                new MoveImpl(
                                        boardStateProperty.get(),
                                        boardStateProperty.get().getTilesProperty().get(kingX).get(kingY),
                                        boardStateProperty.get().getTilesProperty().get(kingX).get((rookY > kingY) ? kingY + 1 : kingY - 1),
                                        piece
                                )
                        );

                        theoreticalMoves.add(
                                new MoveImpl(
                                        boardStateProperty.get(),
                                        boardStateProperty.get().getTilesProperty().get(kingX).get(kingY),
                                        boardStateProperty.get().getTilesProperty().get(kingX).get(kingY),
                                        piece
                                )
                        );

                        for (Move theoreticalMove: theoreticalMoves) {

                            if (!Predicates.isPlayerNotInCheckAfterMove.test(theoreticalMove)) {

                                canCastle = false;

                            }

                        }

                        if (canCastle) {

                            base.add(
                                    boardStateProperty.get().getTilesProperty().get(
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

        return base;

    }

    public boolean canPieceMoveToTile(Piece piece, Tile tile) {

        return getLegalMovesOf(piece).contains(tile);

    }

    public void movePieceToTile(Piece piece, Tile tile) throws PieceCannotMoveThereException, WrongPlayerTurnException {

        BoardState newBoardState = new BoardStateImpl(
                boardStateProperty.get(),
                true
        );

        int pieceX = piece.getTileProperty().get().getXProperty().get();
        int pieceY = piece.getTileProperty().get().getYProperty().get();
        int tileX = tile.getXProperty().get();
        int tileY = tile.getYProperty().get();

        Piece pieceInNewBoardState = null;
        Tile tileInNewBoardState = newBoardState.getTilesProperty().get(tileX).get(tileY);

        for (Piece piece1: newBoardState.getPiecesProperty().get(boardStateProperty.get().getPlayerTurnProperty().get())) {

            if (piece1.equals(piece)) {

                pieceInNewBoardState = piece1;
                break;

            }

        }

        if (pieceInNewBoardState.getPlayerColorProperty().get() != newBoardState.getPlayerTurnProperty().get()) {
            throw new WrongPlayerTurnException();
        }

        if (!canPieceMoveToTile(piece, tile)) {
            throw new PieceCannotMoveThereException();
        }

        if (tileInNewBoardState.getPieceProperty().get() != null) {

            newBoardState.getTakenPiecesProperty().add(
                    tileInNewBoardState.getPieceProperty().get()
            );

            newBoardState.getPiecesProperty().get(
                    tileInNewBoardState.getPieceProperty().get().getPlayerColorProperty().get()
            ).remove(
                    tileInNewBoardState.getPieceProperty().get()
            );

        }

        tileInNewBoardState.getPieceProperty().set(pieceInNewBoardState);

        pieceInNewBoardState.getTileProperty().set(tileInNewBoardState);

        /*
        * If the move is en passant, we remove the pawn that was taken
        * */

        if (newBoardState.getMovesProperty().get().size() != 0) {

            Move lastMove = newBoardState.getMovesProperty().get().get(
                    newBoardState.getMovesProperty().get().size() - 1);

            if (PieceType.PAWN.equals(lastMove.getPiece().getPieceTypeProperty().get()) &&
                    Math.abs(lastMove.getSourceTile().getXProperty().get() - lastMove.getTargetTile().getXProperty().get()) == 2) {

                if (pieceX == lastMove.getTargetTile().getXProperty().get() &&
                        tileY == lastMove.getTargetTile().getYProperty().get()) {

                    newBoardState.getTilesProperty().get()
                            .get(
                                    lastMove.getTargetTile().getXProperty().get()
                            )
                            .get(
                                    lastMove.getTargetTile().getYProperty().get()
                            )
                            .getPieceProperty().set(
                            null
                    );


                    newBoardState.getTakenPiecesProperty().add(
                            lastMove.getPiece()
                    );

                    newBoardState.getPiecesProperty().get(
                            lastMove.getPiece().getPlayerColorProperty().get()
                    ).remove(
                            lastMove.getPiece()
                    );

                }

            }

        }

        /*
        *  we add castling logic
        * */

        if (PieceType.KING.equals(piece.getPieceTypeProperty().get())) {

            // if a king moves two squares we know it is castling
            if (Math.abs(tileY - pieceY) == 2) {

                // if a king castles queenside, the rook is on Y=0,
                // kingside, the rook is on Y=7

                // The king goes where he goes and the rooks go to Y=3 or Y=5

                if (tileY > pieceY) {

                    Piece rook = newBoardState.getTilesProperty().get(
                            pieceX
                    ).get(
                            7
                    ).getPieceProperty().get();

                    newBoardState.getTilesProperty().get(pieceX)
                            .set(7,
                                new TileImpl(null, pieceX, 7)
                            );

                    rook.getTileProperty().set(
                            newBoardState.getTilesProperty().get(
                                    pieceX
                            ).get(
                                    5
                            )
                    );

                } else {

                    Piece rook = newBoardState.getTilesProperty().get(
                            pieceX
                    ).get(
                            0
                    ).getPieceProperty().get();

                    newBoardState.getTilesProperty().get(pieceX)
                            .set(0,
                                    new TileImpl(null, pieceX, 0)
                            );

                    rook.getTileProperty().set(
                            newBoardState.getTilesProperty().get(
                                    pieceX
                            ).get(
                                    3
                            )
                    );

                }

            }

        }

        newBoardState.getTilesProperty().get(pieceX)
                .set(pieceY,
                new TileImpl(null, pieceX, pieceY)
        );
        newBoardState.getPlayerTurnProperty().set(
                newBoardState.getPlayerTurnProperty().get() == PlayerColor.BLACK ? PlayerColor.WHITE : PlayerColor.BLACK
        );

        newBoardState.getMovesProperty().add(
                new MoveImpl(
                        newBoardState, newBoardState.getTilesProperty().get(pieceX).get(pieceY), tileInNewBoardState, pieceInNewBoardState
                )
        );

        boardStateProperty.set(
                newBoardState
        );

        if (isPlayerInCheckmate()) {
            parent.fireEvent(new ChessBoardEvent(ChessBoardEvent.CHESS_BOARD_EVENT_CHECKMATE, boardStateProperty.get()));
        }

        if (isPlayerInDraw()) {
            parent.fireEvent(new ChessBoardEvent(ChessBoardEvent.CHESS_BOARD_EVENT_DRAW, boardStateProperty.get()));
        }

        if (isPlayerInCheck() && !isPlayerInCheckmate()) {
            parent.fireEvent(new ChessBoardEvent(ChessBoardEvent.CHESS_BOARD_EVENT_CHECK, boardStateProperty.get()));
        }

        if (piece.getPieceTypeProperty().get() == PieceType.PAWN) {
            // Because pawns only move forward, if they reach one end of the board
            // they are promoting
            if (piece.getTileProperty().get().getXProperty().get() == 0 || piece.getTileProperty().get().getXProperty().get() == 7) {

                parent.fireEvent(new ChessPieceEvent(ChessPieceEvent.CHESS_PIECE_EVENT_PAWN_PROMOTION, new MoveImpl(newBoardState, tile, piece)));

            }

        }

        if (tile.getPieceProperty().get() != null) {

            parent.fireEvent(new ChessPieceEvent(ChessPieceEvent.CHESS_PIECE_EVENT_PIECE_TAKEN, new MoveImpl(newBoardState, tile, piece)));

        }

        // What caused the piece moved event does not matter, we only want to know when it happens
        parent.fireEvent(new ChessPieceEvent(ChessPieceEvent.CHESS_PIECE_EVENT_PIECE_MOVED, new MoveImpl()));

    }

}
