package hu.aberci.entities.events;

import hu.aberci.entities.interfaces.BoardState;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

public class ChessBoardEvent extends ChessEvent {

    public static final EventType<ChessBoardEvent> CHESS_BOARD_EVENT_CHECKMATE = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "Checkmate");
    public static final EventType<ChessBoardEvent> CHESS_BOARD_EVENT_DRAW = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "Draw");
    public static final EventType<ChessBoardEvent> CHESS_BOARD_EVENT_PROMOTION = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "BoardPromotion");
    public static final EventType<ChessBoardEvent> CHESS_BOARD_EVENT_CHECK = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "Check");

    @Getter
    private BoardState boardState;

    public ChessBoardEvent(EventType<? extends Event> eventType, BoardState board) {
        super(eventType);
        boardState = board;
    }
}
