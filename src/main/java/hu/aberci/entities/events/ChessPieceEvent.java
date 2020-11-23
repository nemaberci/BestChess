package hu.aberci.entities.events;

import hu.aberci.entities.interfaces.Move;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

public class ChessPieceEvent extends ChessEvent {

    public static final EventType<ChessPieceEvent> CHESS_PIECE_EVENT_PIECE_TAKEN = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "PieceTaken");
    public static final EventType<ChessPieceEvent> CHESS_PIECE_EVENT_PAWN_PROMOTION = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "PiecePromotion");
    public static final EventType<ChessPieceEvent> CHESS_PIECE_EVENT_PIECE_MOVE = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "PieceMove");
    public static final EventType<ChessPieceEvent> CHESS_PIECE_EVENT_PIECE_SELECTED = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "PieceSelected");

    @Getter
    private Move move;

    public ChessPieceEvent(EventType<? extends Event> eventType, Move sourceMove) {
        super(eventType);
        move = sourceMove;
    }
}
