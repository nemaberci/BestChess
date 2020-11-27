package hu.aberci.controllers;

import hu.aberci.entities.data.BoardStateImpl;
import hu.aberci.entities.data.PieceImpl;
import hu.aberci.entities.events.ChessBoardEvent;
import hu.aberci.entities.data.MoveImpl;
import hu.aberci.entities.data.TileImpl;
import hu.aberci.entities.events.ChessPieceEvent;
import hu.aberci.entities.events.ChessPawnPromotionEvent;
import hu.aberci.entities.interfaces.*;
import hu.aberci.exceptions.*;
import hu.aberci.util.Predicates;
import hu.aberci.util.Util;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Class responsible for controlling chess game logic.
 * */
public class ChessGameController {

    /**
     * Stored BoardState. This BoardState is not modified, rather replaced entirely when changing.
     * This means that everyone listening for changes gets notified.
     * */
    @Getter
    protected ObjectProperty<BoardState> boardStateProperty;

    /**
     * The Parent element that will be receiving the events that can occur during gameplay.
     *
     * @see ChessBoardEvent
     * @see ChessPieceEvent
     * */
    @Setter
    private Parent parent;

    /**
     * Static number used to identify pieces. A piece recieves a unique ID after creation, so it can be
     * used to check piece equality.
     * */
    private static Integer currentPieceId = 0;

    /**
     * Returns the current ID, then increments it.
     *
     * @return the next piece ID
     * */
    public static Integer nextPieceId() {

        Integer toReturn = currentPieceId;
        currentPieceId++;

        return toReturn;

    }

    /**
     * Constructor for when we know the time controls. The inner BoardState gets initialized
     * with the given time control settings.
     *
     * @param parent1 Parent element, which should recieve the Events
     * @param increment Timer increment in seconds. This gets added after every move.
     * @param startingTime Timer starting time in seconds. Both white and black start with this time.
     *
     * @see ChessBoardEvent
     * @see ChessPieceEvent
     * @see ChessPawnPromotionEvent
     * */
    public ChessGameController(Parent parent1, int startingTime, int increment) {

        parent = parent1;

        boardStateProperty = new SimpleObjectProperty<>(
                new BoardStateImpl(startingTime, increment, true, true)
        );

    }

    /**
     * Constructor for when we create a controller for a previously created BoardSate. The
     * boardstate's properties are used for creating this instance's inner BoardState.
     *
     * @param parent1 Parent element, which should recieve the Events
     * @param boardState BoardState whose properties are to be copied and used.
     *
     * @see ChessBoardEvent
     * @see ChessPieceEvent
     * @see ChessPawnPromotionEvent
     * */
    public ChessGameController(Parent parent1, BoardState boardState) {

        parent = parent1;

        boardStateProperty = new SimpleObjectProperty<>(
                boardState
        );

    }

    /**
     * Returns whether a player has any legal moves in this position. This is used to determine
     * whether a player is in checkmate or draw.
     *
     * @return Returns {@code true} if the player has any moves and {@code false} if he does not
     * */
    public boolean doesCurrentPlayerHaveAnyMoves() {

        for (Piece piece: boardStateProperty.get().getPiecesProperty().get().get(boardStateProperty.get().getPlayerTurnProperty().get())) {

            Set<Tile> legalMoves = Util.getAllLegalMoves(boardStateProperty.get(), piece);

            if (legalMoves.size() != 0) {
                return true;
            }

        }

        return false;

    }

    /**
     * Returns if the current player is in check. A player is in check if there exists an enemy
     * piece that could move to this player's king's tile.
     *
     * @return Returns {@code true} if the current player is in check and {@code false} if he is not.
     * */
    public boolean isPlayerInCheck() {

        return Predicates.isPlayerInCheck.test(boardStateProperty.get());

    }

    /**
     * Returns if the current player is in a drawn position. A draw occurs if the player is not in check
     * and he does not have any legal moves.
     *
     * @return Returns {@code true} if the current player is in draw and {@code false} if he is not.
     * */
    public boolean isPlayerInDraw() {

        return !isPlayerInCheck() && !doesCurrentPlayerHaveAnyMoves();

    }

    /**
     * Returns if the current player is in checkmate. A checkmate occurs if the player is in check
     * and he does not have any legal moves.
     *
     * @return Returns {@code true} if the current player is checkmated and {@code false} if he is not.
     * */
    public boolean isPlayerInCheckmate() {

        return isPlayerInCheck() && !doesCurrentPlayerHaveAnyMoves();

    }

    /**
     * Returns all available tiles for the given piece.
     *
     * @return All possible legal moves for a given piece
     * */
    public Set<Tile> getLegalMovesOf(Piece piece) {

        return Util.getAllLegalMoves(boardStateProperty.get(), piece);

    }

    /**
     * Returns whether a given piece can move to a given tile
     *
     * @return Returns {@code true} if the piece can move there and {@code false} if it can not.
     * */
    public boolean canPieceMoveToTile(Piece piece, Tile tile) {

        return getLegalMovesOf(piece).contains(tile);

    }

    /**
     * Promotes a given pawn to the given piece type. Replaces inner BoardState, forcing a GUI update.
     *
     * @param piece The given pawn to be promoted
     * @param pieceType The piece type of the new piece
     * @throws PieceIsNotPawnException if the specified piece is not a pawn
     *
     * @see PieceIsNotPawnException
     * */
    public void promotePawnToPieceType(Piece piece, PieceType pieceType) throws PieceIsNotPawnException {

        BoardState newBoardState = new BoardStateImpl(
                boardStateProperty.get(),
                true
        );

        if (!PieceType.PAWN.equals(piece.getPieceTypeProperty().get())) {

            throw new PieceIsNotPawnException();

        }

        Piece pieceInNewBoardState = null;

        for (Piece piece1: newBoardState.getPiecesProperty().get(piece.getPlayerColorProperty().get())) {

            if (piece1.equals(piece)) {

                pieceInNewBoardState = piece1;
                break;

            }

        }

        Tile tileOfPiece = newBoardState.getTilesProperty().get().get(
                piece.getTileProperty().get().getXProperty().get()
        ).get(
                piece.getTileProperty().get().getYProperty().get()
        );

        Piece newPiece = new PieceImpl(
                tileOfPiece,
                pieceType,
                piece.getPlayerColorProperty().get()
        );

        pieceInNewBoardState.getTileProperty().set(null);

        tileOfPiece.getPieceProperty().set(
                newPiece
        );

        // pawn gets removed
        newBoardState.getTakenPiecesProperty().add(
                piece
        );

        newBoardState.getPiecesProperty().get(
                pieceInNewBoardState.getPlayerColorProperty().get()
        ).remove(
                pieceInNewBoardState
        );

        newBoardState.getPiecesProperty().get(
                pieceInNewBoardState.getPlayerColorProperty().get()
        ).add(
                newPiece
        );

        boardStateProperty.set(
                newBoardState
        );

    }

    /**
     * Moves a given piece to a given tile on the board. It does this by creating a new BoardState,
     * changing it and then replacing the current BoardState with the new one, forcing a GUI update.
     * If a piece gets removed, it gets moved into the BoardState's takenPieces property.
     * This function sends all events that have to do with game logic. The full list of events sent are:
     * <ul>
     *     <li>{@link ChessBoardEvent#CHESS_BOARD_EVENT_CHECK}: When a player gets in check</li>
     *     <li>{@link ChessBoardEvent#CHESS_BOARD_EVENT_CHECKMATE}: When a player gets checkmated</li>
     *     <li>{@link ChessBoardEvent#CHESS_BOARD_EVENT_DRAW}: When the position is drawn</li>
     *     <li>{@link ChessPieceEvent#CHESS_PIECE_EVENT_PAWN_PROMOTION_STARTED}: When a pawn is about to be promoted</li>
     *     <li>{@link ChessPieceEvent#CHESS_PIECE_EVENT_PIECE_MOVED}: After a piece has been moved, signaling that this function is over</li>
     *     <li>{@link ChessPieceEvent#CHESS_PIECE_EVENT_PIECE_TAKEN}: When a piece gets taken</li>
     * </ul>
     *
     * @param piece The piece to be moved
     * @param tile The tile to move the piece to
     * @throws PieceCannotMoveThereException If the given Piece can not legally move to the given Tile
     * @throws WrongPlayerTurnException If the given Piece can not legally be moved this turn
     * */
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
                        newBoardState, newBoardState.getTilesProperty().get(pieceX).get(pieceY), tileInNewBoardState, pieceInNewBoardState, tile.getPieceProperty().get() != null
                )
        );

        if (newBoardState.getPositionCounterProperty().get().containsKey(newBoardState.getFEN().getBoard())) {
            newBoardState.getPositionCounterProperty().replace(
                    newBoardState.getFEN().getBoard(),
                    newBoardState.getPositionCounterProperty().get(newBoardState.getFEN().getBoard()) + 1
            );
        } else {
            newBoardState.getPositionCounterProperty().put(
                    newBoardState.getFEN().getBoard(),
                    1
            );
        }

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

        if (Integer.parseInt(boardStateProperty.get().getFEN().getHalfTurns()) >= 50) {
            parent.fireEvent(new ChessBoardEvent(ChessBoardEvent.CHESS_BOARD_EVENT_DRAW, boardStateProperty.get()));
        }

        if (boardStateProperty.get().getPositionCounterProperty().get(boardStateProperty.get().getFEN().getBoard()) >= 3) {
            parent.fireEvent(new ChessBoardEvent(ChessBoardEvent.CHESS_BOARD_EVENT_DRAW, boardStateProperty.get()));
        }

        if (pieceInNewBoardState.getPieceTypeProperty().get() == PieceType.PAWN) {
            // Because pawns only move forward, if they reach one end of the board
            // they are promoting
            if (pieceInNewBoardState.getTileProperty().get().getXProperty().get() == 0 || pieceInNewBoardState.getTileProperty().get().getXProperty().get() == 7) {

                parent.fireEvent(new ChessPieceEvent(ChessPieceEvent.CHESS_PIECE_EVENT_PAWN_PROMOTION_STARTED, new MoveImpl(newBoardState, tileInNewBoardState, pieceInNewBoardState, tileInNewBoardState.getPieceProperty().get() != null)));

            }

        }

        if (tile.getPieceProperty().get() != null) {

            parent.fireEvent(new ChessPieceEvent(ChessPieceEvent.CHESS_PIECE_EVENT_PIECE_TAKEN, new MoveImpl(newBoardState, tileInNewBoardState, pieceInNewBoardState, true)));

        }

        // What caused the piece moved event does not matter, we only want to know when it happens
        parent.fireEvent(new ChessPieceEvent(ChessPieceEvent.CHESS_PIECE_EVENT_PIECE_MOVED, new MoveImpl()));

        // System.out.println(boardStateProperty.get().getFEN().getFENCode());

    }

}
