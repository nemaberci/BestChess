package hu.aberci.views;

import hu.aberci.entities.data.MoveImpl;
import hu.aberci.entities.events.ChessPieceEvent;
import hu.aberci.entities.interfaces.Tile;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import lombok.Getter;

/**
 * Custom View housing a single Tile of a chess board.
 * */
public class TileView extends StackPane {

    /**
     * JAVAFX property that stores the Tile for this TileView.
     * */
    @Getter
    ObjectProperty<Tile> tileProperty;

    /**
     * The Parent element that will receive the events.
     * */
    ChessBoardView parent;

    /**
     * This is done so "this" can be used in event handlers.
     * */
    TileView me;

    /**
     * Creates a new TileView with parent element gridPane and inner tile tile.
     *
     * @param gridPane The parent element that will receive the events.
     * @param tile The tile that is stored in this TileView.
     * */
    public TileView(ChessBoardView gridPane, Tile tile) {

        tileProperty = new SimpleObjectProperty<>(tile);
        parent = gridPane;
        me = this;

        parent.getSelectedPieceLegalMoves().addListener(
                new ListChangeListener<TileView>() {
                    @Override
                    public void onChanged(Change<? extends TileView> change) {
                        // System.out.println("I AM CHANGING");

                        while (change.next()) {

                            if (change.wasRemoved()) {

                                if (change.getRemoved().contains(me)) {

                                    setStyle(
                                            "-fx-background-color: " + (((tileProperty.get().getYProperty().get() + tileProperty.get().getXProperty().get()) % 2 == 1) ? "gray" : "white")
                                    );

                                }

                            } else {

                                if (change.wasAdded()) {

                                    if (change.getAddedSubList().contains(me)) {

                                        setStyle(
                                                "-fx-background-color: " + (((tileProperty.get().getYProperty().get() + tileProperty.get().getXProperty().get()) % 2 == 1) ? "forestgreen" : "palegreen")
                                        );

                                    }

                                }

                            }

                        }
                    }
                }
        );

        setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                        // System.out.println("I WAS CLICKED, I AM AT " + tileProperty.get().getXProperty().get() + ", " + tileProperty.get().getYProperty().get());

                        if (parent.getSelectedPieceView().get() != null) {

                            parent.fireEvent(
                                    new ChessPieceEvent(
                                            ChessPieceEvent.CHESS_PIECE_EVENT_PIECE_MOVING,
                                            new MoveImpl(parent.getBoardStateProperty().get(), tileProperty.get(), parent.getSelectedPieceView().get().getPieceProperty().get(), tileProperty.get().getPieceProperty() != null)
                                    )
                            );

                        }

                    }
                }
        );

        setStyle(
                "-fx-background-color: " + (((tileProperty.get().getYProperty().get() + tileProperty.get().getXProperty().get()) % 2 == 1) ? "gray" : "white")
        );

    }

}
