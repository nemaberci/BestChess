package hu.aberci.controllers;

import hu.aberci.entities.data.ChessClockImpl;
import hu.aberci.entities.data.SerializableBoardStateImpl;
import hu.aberci.entities.events.ChessBoardEvent;
import hu.aberci.entities.events.ChessPawnPromotionEvent;
import hu.aberci.entities.events.ChessPieceEvent;
import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Move;
import hu.aberci.entities.interfaces.PlayerColor;
import hu.aberci.main.GameMain;
import hu.aberci.util.ChessEngineMoveTask;
import hu.aberci.util.ChessEngineUtil;
import hu.aberci.util.ExecutorUtil;
import hu.aberci.views.ChessBoardView;
import hu.aberci.views.PromotionView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class GameController implements Initializable {

    ChessGameController chessGameController;

    ChessClockController chessClockController;

    @FXML
    ChessBoardView gridPane;

    @FXML
    Button backToMenu;

    @FXML
    Button newGame;

    @FXML
    TextArea textArea;

    @FXML
    TextArea whiteTime;

    @FXML
    TextArea blackTime;

    @FXML
    Label blackTimeLabel;

    @FXML
    Label whiteTimeLabel;

    @FXML
    PromotionView promotionView;

    ChessBoardView chessBoard;

    int time, increment;

    private boolean isChessClock = false;
    private boolean isAIEnabled = false;

    private PlayerColor AIColor = PlayerColor.BLACK;

    private void moveAI() {

        BoardState boardState = chessBoard.getBoardStateProperty().get();

        if (AIColor.equals(boardState.getPlayerTurnProperty().get())) {

            ChessEngineUtil.sendStockfishPosition(
                    boardState.getFEN()
            );

            if (isChessClock) {

                ChessEngineUtil.sendStockfishTimeControls(
                        chessClockController.getChessClockProperty().get().getWhiteTimeProperty().get(),
                        chessClockController.getChessClockProperty().get().getBlackTimeProperty().get(),
                        chessClockController.getChessClockProperty().get().getIncrement()
                );

            } else {

                ChessEngineUtil.sendStockfishNoTimeControls();

            }

            ChessEngineMoveTask chessEngineMoveTask = new ChessEngineMoveTask();

            chessEngineMoveTask.setBoardState(
                    chessBoard.getBoardStateProperty().get()
            );

            chessEngineMoveTask.setOnSucceeded(
                    e -> {

                        Move AIMove = chessEngineMoveTask.getValue();

                        System.out.println("MOVING FOR AI");

                        chessGameController.movePieceToTile(
                                AIMove.getPiece(),
                                AIMove.getTargetTile()
                        );

                        if (AIMove.getPromotingTo() != null) {

                            chessGameController.promotePawnToPieceType(
                                    AIMove.getPiece(),
                                    AIMove.getPromotingTo()
                            );

                        }

                    }
            );

            Platform.runLater(
                    chessEngineMoveTask::run
            );

        }

    }

    private void restart() {

        textArea.setVisible(false);

        if (isChessClock) {

            ExecutorUtil.stop();
            chessClockController.getChessClockProperty().unbindBidirectional(
                    chessGameController.getBoardStateProperty().get().getChessClockProperty()
            );

        }

        chessGameController = new ChessGameController(null, time, increment);

        chessGameController.setParent(gridPane);

        chessBoard.getBoardStateProperty()
                .bindBidirectional(chessGameController.getBoardStateProperty());

        chessBoard.setChessGameController(chessGameController);

        if (isChessClock) {

            chessClockController.getChessClockProperty().bindBidirectional(
                    chessGameController.getBoardStateProperty().get().getChessClockProperty()
            );
            ExecutorUtil.start();

        }

    }

    public GameController() {

        chessClockController = null;

        int clockTime = 0;
        int clockIncrement = 0;

        try {

            MenuController menuController = GameMain.getMenuController();

            // isChessClock = menuController.getChessClockEnabled().isSelected();
            clockIncrement = parseInt(menuController.getClockIncrement().getText());
            clockTime = parseInt(menuController.getClockTime().getText());
            // isAIEnabled = menuController.getAIEnabled().isSelected();

            chessGameController = new ChessGameController(null, clockTime, clockIncrement);

            chessClockController = new ChessClockController(
                    gridPane,
                    chessGameController.getBoardStateProperty().get(),
                    chessGameController.getBoardStateProperty().get().getChessClockProperty().get()
            );

            ExecutorUtil.setChessClockController(chessClockController);

        } catch (Exception exception) {

            exception.printStackTrace();

        }

        time = clockTime;
        increment = clockIncrement;

    }

    private void saveGame() {

        File file = new File(GameMain.savedGameFileName);

        if (file.isFile()) {

            file.delete();

        }

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(GameMain.savedGameFileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(new SerializableBoardStateImpl(chessBoard.getBoardStateProperty().get()));

            objectOutputStream.close();
            fileOutputStream.close();

        } catch (Exception exception) {

            exception.printStackTrace();

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            MenuController menuController = GameMain.getMenuController();

            isAIEnabled = menuController.getAIEnabled().isSelected();

            AIColor = menuController.getAiColorComboBox().getValue();

            isChessClock = menuController.getChessClockEnabled().isSelected();

            if (menuController.getBoardState() != null) {

                chessGameController = new ChessGameController(null, menuController.getBoardState());

                isChessClock = menuController.getBoardState().getIsTimeControlledProperty().get();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        chessGameController.setParent(gridPane);

        chessBoard = gridPane;
        chessBoard.initialize();

        chessGameController.getBoardStateProperty().get().getIsTimeControlledProperty().set(
                isChessClock
        );

        chessBoard.getBoardStateProperty()
                .bindBidirectional(chessGameController.getBoardStateProperty());

        chessBoard.setChessGameController(chessGameController);

        chessBoard.getChessAIColorProperty().set(
                AIColor
        );

        chessBoard.getChessAIEnabledProperty().set(
                isAIEnabled
        );

        textArea.setVisible(false);

        blackTime.textProperty().bindBidirectional(
                chessGameController.getBoardStateProperty().get().getChessClockProperty().get().getBlackTimeProperty(),
                new NumberStringConverter()
        );
        blackTime.setEditable(false);
        blackTime.setVisible(isChessClock);
        blackTimeLabel.visibleProperty().bindBidirectional(
                blackTime.visibleProperty()
        );

        whiteTime.textProperty().bindBidirectional(
                chessGameController.getBoardStateProperty().get().getChessClockProperty().get().getWhiteTimeProperty(),
                new NumberStringConverter()
        );
        whiteTime.setEditable(false);
        whiteTime.setVisible(isChessClock);
        whiteTimeLabel.visibleProperty().bindBidirectional(
                whiteTime.visibleProperty()
        );

        if (isChessClock) {

            //System.out.println("STARTING TIMER");

            chessClockController.getChessClockProperty().bindBidirectional(
                    chessGameController.getBoardStateProperty().get().getChessClockProperty()
            );

            chessClockController.setParent(chessBoard);
            ExecutorUtil.setChessClockController(chessClockController);
            ExecutorUtil.start();

        }

        if (isAIEnabled) {

            System.out.println("AI IS ENABLED");

            ChessEngineUtil.startEngine();

            ChessEngineUtil.sendStockfishNewGameCommand();

            ChessEngineUtil.sendStockfishPosition(
                    chessBoard.getBoardStateProperty().get().getFEN()
            );

            chessBoard.addEventHandler(
                    ChessPieceEvent.CHESS_PIECE_EVENT_PIECE_MOVED,
                    (move) -> {
                        moveAI();
                    }
            );

        }

        newGame.setVisible(false);
        newGame.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        restart();
                    }
                }
        );

        backToMenu.setOnAction(
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        try {

                            if (isChessClock) {

                                ExecutorUtil.stop();

                            }

                            /*
                            * Same thing as in MenuController
                            *  */

                            final FXMLLoader loader = new FXMLLoader(
                                    MenuController.class.getResource("/fxml/menu.fxml")
                            );

                            loader.setRoot(null);

                            ((Stage) backToMenu.getScene().getWindow()).setScene(
                                    new Scene(loader.load())
                            );

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
        );

        chessBoard.addEventHandler(
                ChessBoardEvent.CHESS_BOARD_EVENT_CHECKMATE,
                chessBoardEvent -> {

                    textArea.setVisible(true);

                    textArea.setText("Checkmate");
                    newGame.setVisible(true);

                    textArea.setText(
                            textArea.getText() + "\n" + (PlayerColor.WHITE.equals(chessBoard.getBoardStateProperty().get().getPlayerTurnProperty().get()) ? "Black" : "White") + " wins"
                    );

                }
        );

        chessBoard.addEventHandler(
                ChessBoardEvent.CHESS_BOARD_EVENT_DRAW,
                chessBoardEvent -> {

                    textArea.setVisible(true);

                    textArea.setText("Draw");
                    newGame.setVisible(true);

                }
        );

        chessBoard.addEventHandler(
                ChessPieceEvent.CHESS_PIECE_EVENT_PIECE_MOVED,
                chessPieceEvent -> {

                    saveGame();

                    if (isChessClock) {

                        chessClockController.click();

                    }

                }
        );

        if (isChessClock) {

            chessClockController.getChessClockProperty().get().getBlackTimeProperty().addListener(
                    change -> {

                        saveGame();

                    }
            );

            chessClockController.getChessClockProperty().get().getWhiteTimeProperty().addListener(
                    change -> {

                        saveGame();

                    }
            );

        }

        chessBoard.addEventHandler(
                ChessBoardEvent.CHESS_BOARD_EVENT_CLOCK_FLAG,
                chessBoardEvent -> {

                    System.out.println("TIME RAN OUT");

                    ExecutorUtil.stop();

                    textArea.setVisible(true);
                    textArea.setText("Time ran out");

                    textArea.setText(
                            textArea.getText() + "\n" + (PlayerColor.WHITE.equals(chessBoard.getBoardStateProperty().get().getPlayerTurnProperty().get()) ? "Black" : "White") + " wins"
                    );

                }
        );

        chessBoard.addEventHandler(
                ChessPawnPromotionEvent.CHESS_PAWN_PROMOTION_EVENT_EVENT_TYPE,
                chessPawnPromotionEvent -> {

                    System.out.println("PROMOTING");

                    chessGameController.promotePawnToPieceType(
                            chessPawnPromotionEvent.getPiece(),
                            chessPawnPromotionEvent.getPieceType()
                    );
                }
        );

        if (isAIEnabled && AIColor.equals(chessGameController.boardStateProperty.get().getPlayerTurnProperty().get())) {

            // We move the AI if the game starts
            moveAI();

        }

        promotionView.getGameSpace().set(
                chessBoard
        );
        promotionView.initialize();
        promotionView.setVisible(false);

    }
}
