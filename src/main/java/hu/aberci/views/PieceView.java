package hu.aberci.views;

import hu.aberci.entities.data.MoveImpl;
import hu.aberci.entities.events.ChessPieceEvent;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.util.Util;
import javafx.beans.property.*;
import javafx.collections.ObservableSet;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;

/**
 * Custom View that every piece on the board is housed in.
 * */
public class PieceView extends Button {

    /**
     * JAVAFX property that stores the inner piece.
     * */
    @Getter
    @Setter
    private ObjectProperty<Piece> pieceProperty;

    /**
     * The ChessBoardView who should receive the events.
     * */
    private ChessBoardView parentBoard;
    /**
     * This is done so "this" can be used in event handlers.
     * */
    private PieceView me;

    /**
     * This shows the parent ChessBoardView's selected PieceView. It is bidirectionally bound
     * at initialization.
     * */
    private ObjectProperty<PieceView> selectedPieceView;

    /**
     * Creates a new PieceView which houses an inner piece piece and sends events to parent.
     *
     * @param piece The piece that this view contains.
     * @param parent The Parent that will receive the events.
     * */
    public PieceView(ChessBoardView parent, Piece piece) {

        super();

        parentBoard = parent;
        me = this;

        pieceProperty = new SimpleObjectProperty<>(piece);

        selectedPieceView = new SimpleObjectProperty<>();
        parentBoard.selectedPieceView.bindBidirectional(
                selectedPieceView
        );

        setOnMouseClicked(
                mouseEvent -> {

                    if (parentBoard.getSelectedPieceView().get() != null) {

                        if (parentBoard.getSelectedPieceLegalMoves().contains(
                                getParent()
                        )) {
                            getParent().fireEvent(mouseEvent);
                        } else {

                            if (parent.getIsGameLive().get()) {

                                parentBoard.selectedPieceView.set(
                                        me
                                );
                                parentBoard.fireEvent(
                                        new ChessPieceEvent(ChessPieceEvent.CHESS_PIECE_EVENT_PIECE_SELECTED,
                                                new MoveImpl(parentBoard.getBoardStateProperty().get(), null, me.getPieceProperty().get(), false))
                                );

                            }

                        }

                    } else {

                        if (parent.getIsGameLive().get()) {

                            parentBoard.selectedPieceView.set(
                                    me
                            );
                            parentBoard.fireEvent(
                                    new ChessPieceEvent(ChessPieceEvent.CHESS_PIECE_EVENT_PIECE_SELECTED,
                                            new MoveImpl(parentBoard.getBoardStateProperty().get(), null, me.getPieceProperty().get(), false))
                            );

                        }

                    }

                }
        );

        ImageView imageView = new ImageView();

        imageView.imageProperty().set(
                Util.getPieceImage(
                        pieceProperty.get()
                )
        );

        imageView.setSmooth(true);

        imageView.setPreserveRatio(true);

        imageView.setFitHeight(
                30
        );

        imageView.setFitWidth(
                30
        );

        setGraphic(
                imageView
        );

        setStyle(
                "-fx-background-color: transparent; -fx-border-color: transparent"
        );

    }

}
