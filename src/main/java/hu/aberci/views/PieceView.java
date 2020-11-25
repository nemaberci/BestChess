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

public class PieceView extends Button {

    @Getter
    @Setter
    private ObjectProperty<Piece> pieceProperty;

    private ChessBoardView parentBoard;
    private PieceView me;

    private ObjectProperty<PieceView> selectedPieceView;

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
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                        if (parentBoard.getSelectedPieceView().get() != null) {

                            if (parentBoard.getSelectedPieceLegalMoves().contains(
                                    getParent()
                            )) {
                                getParent().fireEvent(mouseEvent);
                            } else {

                                parentBoard.selectedPieceView.set(
                                        me
                                );
                                parentBoard.fireEvent(
                                        new ChessPieceEvent(ChessPieceEvent.CHESS_PIECE_EVENT_PIECE_SELECTED,
                                                new MoveImpl(parentBoard.getBoardStateProperty().get(), null, me.getPieceProperty().get(), false))
                                );

                            }

                        } else {

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
                40
        );

        imageView.setFitWidth(
                35
        );

        setGraphic(
                imageView
        );

        setStyle(
                "-fx-background-color: transparent; -fx-border-color: transparent"
        );

    }

}
