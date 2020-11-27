package hu.aberci.views;

import hu.aberci.entities.events.ChessPawnPromotionEvent;
import hu.aberci.entities.events.ChessPieceEvent;
import hu.aberci.entities.interfaces.PieceType;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import lombok.Getter;

import java.util.ArrayList;

/**
 * Custom View that houses 4 PromotionPieceViews.
 * */
public class PromotionView extends GridPane {

    /**
     * The Parent element that will receive the pawn promotion event.
     * */
    @Getter
    private ObjectProperty<Parent> gameSpace;

    /**
     * The inner PromotionPieceViews
     * */
    @Getter
    private ListProperty<PromotionPieceView> promotionPieceViews;

    /**
     * Initializes the inner promotion piece views and creates the pane itself.
     * */
    public PromotionView() {

        super();

        Label label = new Label();

        promotionPieceViews = new SimpleListProperty<>(
                FXCollections.observableList(
                        new ArrayList<>()
                )
        );

        promotionPieceViews.addAll(
                new PromotionPieceView(this, PieceType.BISHOP),
                new PromotionPieceView(this, PieceType.KNIGHT),
                new PromotionPieceView(this, PieceType.QUEEN),
                new PromotionPieceView(this, PieceType.ROOK)
        );

        add(
                label,
                0,
                0,
                4,
                1
        );

        label.setPrefHeight(50.0);
        label.setPrefWidth(200.0);

        label.setAlignment(Pos.CENTER);

        label.setFont(
                new Font(
                        "Arial",
                        14
                )
        );

        //setRowSpan(label, 4);

        label.setText(
                "Choose a piece to promote to"
        );

        int index = 0;

        for (PromotionPieceView promotionPieceView: promotionPieceViews.get()) {

            add(promotionPieceView, index, 1);
            index++;

        }

        gameSpace = new SimpleObjectProperty<>(
                null
        );

    }

    /**
     * Adds event handlers to the initialized components.
     * */
    public void initialize() {

        gameSpace.get().addEventHandler(
                ChessPieceEvent.CHESS_PIECE_EVENT_PAWN_PROMOTION_STARTED,
                chessPieceEvent -> {

                    promotionPieceViews.forEach(
                            promotionPieceView -> {

                                promotionPieceView.getPieceProperty().set(
                                        chessPieceEvent.getMove().getPiece()
                                );

                            }
                    );

                    setVisible(true);

                    Platform.runLater(
                            this::toFront
                    );

                }
        );

        addEventHandler(
                ChessPawnPromotionEvent.CHESS_PAWN_PROMOTION_EVENT_EVENT_TYPE,
                chessPawnPromotionEvent -> gameSpace.get().fireEvent(chessPawnPromotionEvent)
        );

    }

}
