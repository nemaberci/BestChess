package hu.aberci.entities.events;

import hu.aberci.entities.interfaces.BoardState;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

/**
 * Custom {@link Event} that describes an event affecting a Board.
 * */
public class ChessBoardEvent extends ChessEvent {

    /**
     * Event that means a checkmate happened.
     * */
    public static final EventType<ChessBoardEvent> CHESS_BOARD_EVENT_CHECKMATE = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "Checkmate");
    /**
     * Event that means a draw happened.
     * */
    public static final EventType<ChessBoardEvent> CHESS_BOARD_EVENT_DRAW = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "Draw");
    /**
     * Event that means a check happened.
     * */
    public static final EventType<ChessBoardEvent> CHESS_BOARD_EVENT_CHECK = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "Check");
    /**
     * Event that means a player ran out of time.
     * */
    public static final EventType<ChessBoardEvent> CHESS_BOARD_EVENT_CLOCK_FLAG = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "ClockFlag");

    /**
     * Every ChessBoardEvent affects a BoardState.
     * */
    @Getter
    private BoardState boardState;

    /**
     * Creates a new ChessBoardEvent with a given BoardState
     *
     * @param board The BoardState affected
     * @param eventType The type of the BoardStateEvent
     * */
    public ChessBoardEvent(EventType<? extends Event> eventType, BoardState board) {
        super(eventType);
        boardState = board;
    }
}
