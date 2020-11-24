package hu.aberci.views;

import hu.aberci.controllers.ChessGameController;
import hu.aberci.entities.events.ChessPieceEvent;
import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.PlayerColor;
import hu.aberci.entities.interfaces.Tile;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
    ListProperty<TileView> selectedPieceLegalMoves;

    @Setter
    ChessGameController chessGameController;

    public ChessBoardView() {

        super();

        selectedPieceView = new SimpleObjectProperty<>(null);
        selectedPieceLegalMoves = new SimpleListProperty<>(
                FXCollections.observableList(new ArrayList<>())
        );

        addEventHandler(
                ChessPieceEvent.CHESS_PIECE_EVENT_PIECE_MOVING,
                new EventHandler<ChessPieceEvent>() {
                    @Override
                    public void handle(ChessPieceEvent chessPieceEvent) {

                        if (chessGameController.canPieceMoveToTile(chessPieceEvent.getMove().getPiece(), chessPieceEvent.getMove().getTargetTile())) {

                            chessGameController.movePieceToTile(chessPieceEvent.getMove().getPiece(), chessPieceEvent.getMove().getTargetTile());

                            selectedPieceView = new SimpleObjectProperty<>(null);

                        } else {

                            selectedPieceView = new SimpleObjectProperty<>(null);

                        }

                    }
                }
        );

        addEventHandler(
                ChessPieceEvent.CHESS_PIECE_EVENT_PIECE_SELECTED,
                new EventHandler<ChessPieceEvent>() {
                    @Override
                    public void handle(ChessPieceEvent chessPieceEvent) {

                        selectedPieceLegalMoves.clear();

                        if (selectedPieceView.get() != null) {

                            // System.out.println(chessGameController.getLegalMovesOf(chessPieceEvent.getMove().getPiece()).size());

                            for (TileView tileView: chessGameController.getLegalMovesOf(chessPieceEvent.getMove().getPiece()).stream().map(
                                    tile -> tileViews.get(
                                            tile.getXProperty().get()
                                    ).get(
                                            tile.getYProperty().get()
                                    )
                            ).collect(Collectors.toList())) {

                                // System.out.println(tileView);

                                selectedPieceLegalMoves.add(
                                        tileView
                                );

                            }

                            // System.out.println(selectedPieceLegalMoves.size());

                        }

                    }
                }
        );

    }

    public void initialize() {

        tileViews = FXCollections.observableList(new ArrayList<>());

        boardStateProperty.addListener(
                (obs, old, val) -> {

                    //System.out.println("Changing");

                    getChildren().clear();
                    getRowConstraints().clear();
                    getColumnConstraints().clear();

                    tileViews = FXCollections.observableList(
                            new ArrayList<>()
                    );
                    pieceViews = FXCollections.observableMap(
                            new HashMap<>()
                    );

                    for (int i = 0; i < 8; i++) {

                        getRowConstraints().add(
                                new RowConstraints(50)
                        );

                        getColumnConstraints().add(
                                new ColumnConstraints(50)
                        );

                    }

                    for (List<Tile> tileList: val.getTilesProperty().get()) {

                        tileViews.add(FXCollections.observableList(new ArrayList<>()));

                        for (Tile tile: tileList) {

                            TileView tileView = new TileView(this, tile);
                            tileViews.get(tile.getXProperty().get()).add(tileView);

                            // I messed up my coordinate system kind-of, so we display them correctly now
                            add(tileView, tile.getYProperty().get(), 7 - tile.getXProperty().get());
                            // System.out.println("ADDED NEW TILEVIEW AT " + tile.getXProperty().get() + ", " + tile.getYProperty().get());

                            tileView.setPrefSize(50, 50);
                            tileView.setMaxSize(50, 50);

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

                }
        );

    }
}
