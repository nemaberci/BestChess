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

import java.util.Set;

public class ChessGameController {

    protected BoardState boardState;
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

        return Util.getAllLegalMoves(boardState, piece);

    }

    public boolean canPieceMoveToTile(Piece piece, Tile tile) {

        return Util.getAllLegalMoves(boardState, piece).contains(tile);

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

    }

}
