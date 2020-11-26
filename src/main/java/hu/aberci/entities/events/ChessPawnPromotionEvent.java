package hu.aberci.entities.events;

import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.PieceType;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

public class ChessPawnPromotionEvent extends ChessEvent {

    public static final EventType<ChessPawnPromotionEvent> CHESS_PAWN_PROMOTION_EVENT_EVENT_TYPE = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "PawnPromotion");

    @Getter
    private PieceType pieceType;

    @Getter
    private Piece piece;

    public ChessPawnPromotionEvent(EventType<? extends Event> eventType, Piece pawn, PieceType promoteTo) {
        super(eventType);

        piece = pawn;
        pieceType = promoteTo;
    }

}
