package hu.aberci.entities.events;

import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.PieceType;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

/**
 * Custom {@link Event} that describes a pawn promoting.
 * */
public class ChessPawnPromotionEvent extends ChessEvent {

    /**
     * There is only one kind of pawn promotion
     * */
    public static final EventType<ChessPawnPromotionEvent> CHESS_PAWN_PROMOTION_EVENT_EVENT_TYPE = new EventType<>(ChessEvent.CHESS_EVENT_EVENT_TYPE, "PawnPromotion");

    /**
     * The type of piece the pawn is promoting to
     * */
    @Getter
    private PieceType pieceType;

    /**
     * The pawn that is promoting
     * */
    @Getter
    private Piece piece;

    /**
     * Creates a new ChessPawnPromotionEvent with the given parameters.
     *
     * @param eventType Always CHESS_PAWN_PROMOTION_EVENT_EVENT_TYPE.
     * @param pawn The pawn that is being promoted.
     * @param promoteTo The piece type it should promote to.
     * */
    public ChessPawnPromotionEvent(EventType<? extends Event> eventType, Piece pawn, PieceType promoteTo) {
        super(eventType);

        piece = pawn;
        pieceType = promoteTo;
    }

}
