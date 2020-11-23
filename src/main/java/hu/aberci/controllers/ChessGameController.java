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
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

public class ChessGameController {

    @Getter
    protected BoardState boardState;
    @Setter
    private Parent parent;

    public ChessGameController(Parent parent1, int startingTime, int increment) {

        parent = parent1;

        boardState = new BoardStateImpl(startingTime, increment, true, true);

    }

    public boolean doesCurrentPlayerHaveAnyMoves() {

        for (Piece piece: boardState.getPiecesProperty().get().get(boardState.getPlayerTurnProperty().get())) {

            Set<Tile> legalMoves = Util.getAllLegalMoves(boardState, piece);

            if (legalMoves.size() != 0) {
                return true;
            }

        }

        return false;

    }

    public boolean isPlayerInCheck() {

        return Predicates.isPlayerInCheck.test(boardState);

    }

    public boolean isPlayerInDraw() {

        return !isPlayerInCheck() && !doesCurrentPlayerHaveAnyMoves();

    }

    public boolean isPlayerInCheckmate() {

        return isPlayerInCheck() && !doesCurrentPlayerHaveAnyMoves();

    }

    public Set<Tile> getLegalMovesOf(Piece piece) {

        Set<Tile> base = Util.getAllLegalMoves(boardState, piece);

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

                    base.add(
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

        return base;

    }

    public boolean canPieceMoveToTile(Piece piece, Tile tile) {

        return getLegalMovesOf(piece).contains(tile);

    }

    public void movePieceToTile(Piece piece, Tile tile) throws PieceCannotMoveThereException, WrongPlayerTurnException {

        if (piece.getPlayerColorProperty().get() != boardState.getPlayerTurnProperty().get()) {
            throw new WrongPlayerTurnException();
        }

        if (!canPieceMoveToTile(piece, tile)) {
            throw new PieceCannotMoveThereException();
        }

        ObjectProperty<Tile> currentTileProperty = piece.getTileProperty();
        tile.getPieceProperty().set(piece);

        piece.getTileProperty().set(tile);

        /*
        * If the move is en passant, we remove the pawn that was taken
        * */

        Move lastMove = boardState.getMovesProperty().get().get(
                boardState.getMovesProperty().get().size() - 1);

        if (PieceType.PAWN.equals(lastMove.getPiece().getPieceTypeProperty().get()) &&
                Math.abs(lastMove.getSourceTile().getXProperty().get() - lastMove.getTargetTile().getXProperty().get()) == 2) {

            if (piece.getTileProperty().get().getXProperty().get() == lastMove.getTargetTile().getXProperty().get() &&
                    Math.abs(piece.getTileProperty().get().getYProperty().get() - lastMove.getSourceTile().getYProperty().get()) == 1) {

                boardState.getTilesProperty().get()
                        .get(
                                (lastMove.getSourceTile().getXProperty().get() + lastMove.getTargetTile().getXProperty().get()) / 2
                        )
                        .get(
                                lastMove.getSourceTile().getYProperty().get()
                        )
                        .getPieceProperty().set(
                                null
                );

            }

        }

        currentTileProperty.set(new TileImpl(null, currentTileProperty.get().getXProperty().get(), currentTileProperty.get().getYProperty().get()));
        boardState.getPlayerTurnProperty().set(
                boardState.getPlayerTurnProperty().get() == PlayerColor.BLACK ? PlayerColor.WHITE : PlayerColor.BLACK
        );

        if (isPlayerInCheckmate()) {
            parent.fireEvent(new ChessBoardEvent(ChessBoardEvent.CHESS_BOARD_EVENT_CHECKMATE, boardState));
        }

        if (isPlayerInDraw()) {
            parent.fireEvent(new ChessBoardEvent(ChessBoardEvent.CHESS_BOARD_EVENT_DRAW, boardState));
        }

        if (isPlayerInCheck() && !isPlayerInCheckmate()) {
            parent.fireEvent(new ChessBoardEvent(ChessBoardEvent.CHESS_BOARD_EVENT_CHECK, boardState));
        }

        if (piece.getPieceTypeProperty().get() == PieceType.PAWN) {
            // Because pawns only move forward, if they reach one end of the board
            // they are promoting
            if (piece.getTileProperty().get().getXProperty().get() == 0 || piece.getTileProperty().get().getXProperty().get() == 7) {

                parent.fireEvent(new ChessPieceEvent(ChessPieceEvent.CHESS_PIECE_EVENT_PAWN_PROMOTION, new MoveImpl(boardState, tile, piece)));

            }

        }

        if (tile.getPieceProperty().get() != null) {

            parent.fireEvent(new ChessPieceEvent(ChessPieceEvent.CHESS_PIECE_EVENT_PIECE_TAKEN, new MoveImpl(boardState, tile, piece)));

        }

        boardState.getMovesProperty().add(
                new MoveImpl(
                        boardState, tile, piece
                )
        );

    }

}
