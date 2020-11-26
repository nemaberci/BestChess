package hu.aberci.entities.events;

import hu.aberci.entities.interfaces.Move;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

/**
 * Custom {@link Event} that can happen to any piece.
 * */
public class ChessPieceEvent extends ChessEvent {

    /**
     * An event that happens after a piece gets taken.
     * */
    public static final EventType<ChessPieceEvent> CHESS_PIECE_EVENT_PIECE_TAKEN = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "PieceTaken");
    /**
     * An event signaling that a pawn is being promoted.
     * */
    public static final EventType<ChessPieceEvent> CHESS_PIECE_EVENT_PAWN_PROMOTION = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "PiecePromotion");
    /**
     * An event signaling that a piece has started to move.
     * */
    public static final EventType<ChessPieceEvent> CHESS_PIECE_EVENT_PIECE_MOVING = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "PieceMove");
    /**
     * An event signaling that a piece has finished moving.
     * */
    public static final EventType<ChessPieceEvent> CHESS_PIECE_EVENT_PIECE_MOVED = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "PieceMoved");
    /**
     * An event signaling that a piece has been selected on the GUI.
     * */
    public static final EventType<ChessPieceEvent> CHESS_PIECE_EVENT_PIECE_SELECTED = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "PieceSelected");

    /**
     * For every ChessPieceEvent is a Move that triggered it.
     * */
    @Getter
    private Move move;

    /**
     * Creates a new ChessPieceEvent
     *
     * @param eventType The event type that is being created
     * @param sourceMove The move that caused the event
     * */
    public ChessPieceEvent(EventType<? extends Event> eventType, Move sourceMove) {
        super(eventType);
        move = sourceMove;
    }
}
