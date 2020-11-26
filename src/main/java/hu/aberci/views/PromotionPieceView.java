package hu.aberci.views;

import hu.aberci.entities.events.ChessPawnPromotionEvent;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.PieceType;
import hu.aberci.exceptions.NoPromotionSetException;
import hu.aberci.util.Util;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;

public class PromotionPieceView extends Button {

    @Getter
    private ObjectProperty<PieceType> pieceTypeProperty;

    @Getter
    private ObjectProperty<Piece> pieceProperty;

    private PromotionView parent;

    public PromotionPieceView(PromotionView p, PieceType pieceType) {

        super();

        parent = p;

        pieceTypeProperty = new SimpleObjectProperty<>(pieceType);

        pieceProperty = new SimpleObjectProperty<>(null);

        setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                        if (pieceProperty.get() != null) {

                            parent.fireEvent(
                                    new ChessPawnPromotionEvent(
                                            ChessPawnPromotionEvent.CHESS_PAWN_PROMOTION_EVENT_EVENT_TYPE,
                                            pieceProperty.get(),
                                            pieceTypeProperty.get()
                                    )
                            );

                        } else {

                            throw new NoPromotionSetException();

                        }

                        getParent().setVisible(false);

                    }
                }
        );

        ImageView imageView = new ImageView();

        imageView.imageProperty().set(
                Util.getPromotionPieceImage(
                        pieceTypeProperty.get()
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
