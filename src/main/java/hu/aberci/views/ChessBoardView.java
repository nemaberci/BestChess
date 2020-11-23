package hu.aberci.views;

import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.PlayerColor;
import hu.aberci.entities.interfaces.Tile;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import lombok.Getter;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ChessBoardView extends GridPane {

    @Getter
    ObjectProperty<BoardState> boardStateProperty = new SimpleObjectProperty<>();
    @Getter
    ObservableMap<PlayerColor, List<PieceView>> pieceViews;
    @Getter
    ObservableList<ObservableList<TileView>> tileViews;

    @Getter
    ObjectProperty<PieceView> selectedPieceView;
    @Getter
    ObservableSet<TileView> availableTileViews;

    public ChessBoardView() {
        super();
    }

    public void initialize() {

        tileViews = FXCollections.observableList(new ArrayList<>());

        boardStateProperty.addListener(
                (obs, old, val) -> {

                    getChildren().clear();
                    getRowConstraints().clear();
                    getColumnConstraints().clear();

                    for (int i = 0; i < 8; i++) {

                        getRowConstraints().add(
                                new RowConstraints(50)
                        );

                        getColumnConstraints().add(
                                new ColumnConstraints(50)
                        );

                    }

                    boolean isColoredBlack = false;

                    for (List<Tile> tileList: val.getTilesProperty().get()) {

                        tileViews.add(FXCollections.observableList(new ArrayList<>()));

                        for (Tile tile: tileList) {

                            TileView tileView = new TileView(this, tile);
                            tileViews.get(tile.getXProperty().get()).add(tileView);
                            add(tileView, tile.getXProperty().get(), tile.getYProperty().get());
                            System.out.println("ADDED NEW TILEVIEW AT " + tile.getXProperty().get() + ", " + tile.getYProperty().get());

                            tileView.setStyle("-fx-background-color: " + (isColoredBlack ? "black" : "white"));
                            isColoredBlack = !isColoredBlack;

                            tileView.setVisible(true);

                            tileView.setPrefSize(50, 50);
                            tileView.setMaxSize(50, 50);

                            setHgrow(tileView, Priority.NEVER);
                            setVgrow(tileView, Priority.NEVER);

                        }

                    }

                    //setGridLinesVisible(true);

                    List<PieceView> whitePieceViews = new ArrayList<>();
                    List<PieceView> blackPieceViews = new ArrayList<>();

                    for (Piece whitePiece: val.getPiecesProperty().get().get(PlayerColor.WHITE)) {

                        PieceView pieceView = new PieceView(this, whitePiece);
                        whitePieceViews.add(pieceView);

                        tileViews.get(
                                whitePiece.getTileProperty().get().getXProperty().get()
                        ).get(
                                whitePiece.getTileProperty().get().getYProperty().get()
                        ).getChildren().add(
                                pieceView
                        );

                        pieceView.setPrefSize(50, 50);
                        pieceView.setMaxSize(50, 50);

                    }
                    for (Piece blackPiece: val.getPiecesProperty().get().get(PlayerColor.BLACK)) {

                        PieceView pieceView = new PieceView(this, blackPiece);
                        blackPieceViews.add(pieceView);

                        tileViews.get(
                                blackPiece.getTileProperty().get().getXProperty().get()
                        ).get(
                                blackPiece.getTileProperty().get().getYProperty().get()
                        ).getChildren().add(
                                pieceView
                        );

                        pieceView.setPrefSize(50, 50);
                        pieceView.setMaxSize(50, 50);

                    }

                    pieceViews = FXCollections.observableMap(new HashMap<>());

                    pieceViews.put(PlayerColor.WHITE, whitePieceViews);
                    pieceViews.put(PlayerColor.BLACK, blackPieceViews);

                    selectedPieceView = new SimpleObjectProperty<>();
                    availableTileViews = new SimpleSetProperty<>();

                }
        );

    }
}
