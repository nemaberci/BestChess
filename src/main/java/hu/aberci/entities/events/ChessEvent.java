package hu.aberci.entities.events;

import javafx.event.Event;
import javafx.event.EventType;

public class ChessEvent extends Event {

    public static final EventType<ChessEvent> CHESS_EVENT_EVENT_TYPE = new EventType<>(ANY);

    ChessEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
