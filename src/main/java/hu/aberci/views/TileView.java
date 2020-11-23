package hu.aberci.views;

import hu.aberci.entities.data.MoveImpl;
import hu.aberci.entities.events.ChessPieceEvent;
import hu.aberci.entities.interfaces.Tile;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.SetChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import lombok.Getter;

public class TileView extends StackPane {

    @Getter
    ObjectProperty<Tile> tileProperty;

    ChessBoardView parent;

    PieceView child;

    public TileView(ChessBoardView gridPane, Tile tile) {

        tileProperty = new SimpleObjectProperty<>(tile);
        parent = gridPane;

        parent.getSelectedPieceLegalMoves().addListener(
                new SetChangeListener<TileView>() {
                    @Override
                    public void onChanged(Change<? extends TileView> change) {
                        System.out.println("I AM CHANGING");
                        if (change.wasAdded()) {
                            if (this.equals(change.getElementAdded())) {
                                setStyle(
                                        "-fx-background-color: #baca44"
                                );
                            }
                        }
                        if (change.wasRemoved()) {
                            if (this.equals(change.getElementRemoved())) {
                                setStyle(
                                        "-fx-background-color:" + (((tileProperty.get().getYProperty().get() + tileProperty.get().getXProperty().get()) % 2 == 1) ? "#769656" : "#eeeed2")
                                );
                            }
                        }
                    }
                }
        );

        setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                        System.out.println("I WAS CLICKED, I AM AT " + tileProperty.get().getXProperty().get() + ", " + tileProperty.get().getYProperty().get());

                        if (parent.getSelectedPieceView().get() != null) {

                            parent.fireEvent(
                                    new ChessPieceEvent(
                                            ChessPieceEvent.CHESS_PIECE_EVENT_PIECE_MOVE,
                                            new MoveImpl(parent.getBoardStateProperty().get(), tileProperty.get(), parent.getSelectedPieceView().get().getPieceProperty().get())
                                    )
                            );

                        }

                    }
                }
        );

        setStyle(
                "-fx-background-color:" + (((tileProperty.get().getYProperty().get() + tileProperty.get().getXProperty().get()) % 2 == 1) ? "#769656" : "#eeeed2")
        );

    }

}