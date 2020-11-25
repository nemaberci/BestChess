package hu.aberci.controllers;

import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;
import hu.aberci.entities.data.ChessClockImpl;
import hu.aberci.entities.data.SerializableBoardStateImpl;
import hu.aberci.entities.events.ChessBoardEvent;
import hu.aberci.entities.events.ChessPieceEvent;
import hu.aberci.entities.interfaces.PlayerColor;
import hu.aberci.main.GameMain;
import hu.aberci.util.ExecutorUtil;
import hu.aberci.views.ChessBoardView;
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

    ChessBoardView chessBoard;

    int time, increment;

    private boolean isChessClock = false;

    public GameController() {

        chessClockController = null;

        int clockTime = 0;
        int clockIncrement = 0;

        try {

            MenuController menuController = GameMain.getMenuController();

            isChessClock = menuController.getChessClockEnabled().isSelected();
            clockIncrement = parseInt(menuController.getClockIncrement().getText());
            clockTime = parseInt(menuController.getClockTime().getText());

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

        newGame.setVisible(false);
        newGame.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

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



    }
}
