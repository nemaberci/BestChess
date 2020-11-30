package hu.aberci.controllers;

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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import lombok.Getter;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import static java.lang.Integer.parseInt;

/**
 * Class responsible for the game GUI. This is the controller class for the FXML file game.fxml.
 * This class is not constructed by hand, the FXMLLoader creates this task and then calls its inner
 * initialize function.
 * */
public class GameController implements Initializable {

    /**
     * The ChessGameController that controls the game logic.
     * */
    ChessGameController chessGameController;

    /**
     * The ChessClockController responsible for the game's clock.
     * */
    ChessClockController chessClockController;

    /**
     * The GridPane responsible for hosting the TileViews and PieceViews.
     * */
    @FXML
    ChessBoardView chessBoard;

    /**
     * The button that sends the user back to the menu when clicked. This button is always visible.
     * */
    @FXML
    Button backToMenu;

    /**
     * The button that starts a new game. This button is only visible after a game ends.
     * */
    @FXML
    Button newGame;

    /**
     * The text area that shows the game state after the game ended. It shows whether the game was drawn
     * or won. If the game was won by one side, shows who won. This is hidden by default and is
     * only visible when the game is over.
     * */
    @FXML
    TextArea textArea;

    /**
     * Displays white's remaining time in seconds. Not visible when the clock is disabled.
     * */
    @FXML
    TextArea whiteTime;

    /**
     * Displays black's remaining time in seconds. Not visible when the clock is disabled.
     * */
    @FXML
    TextArea blackTime;

    /**
     * Says that the black time box is black's time. Is only visible when black's time is.
     * */
    @FXML
    Label blackTimeLabel;

    /**
     * Says that the white time box is white's time. Is only visible when white's time is.
     * */
    @FXML
    Label whiteTimeLabel;

    /**
     * The GridPane responsible for storing the promotion views (PromotionPieceView). This is
     * not visible by default. It is only visible when a pawn is promoting.
     * */
    @FXML
    PromotionView promotionView;

    /**
     * The MenuItem that restarts the game. It is only visible when the New Game button is visible.
     * */
    @FXML
    private MenuItem restartButton;

    /**
     * Variable used in the chess clock. This value is read from the MenuController or
     * a previously loaded BoardState.
     * */
    int time = 1, increment = 0;

    /**
     * Shows if the current game has the chess clock enabled. This value is read from the MenuController
     * or a previously loaded BoardState.
     * */
    private boolean isChessClock = false;
    /**
     * Shows if the current game has AI enabled. This value is read from the MenuController.
     * */
    private boolean isAIEnabled = false;

    /**
     * Only used when AI is enabled. Shows which side the AI is playing.
     * */
    private PlayerColor AIColor = PlayerColor.BLACK;

    /**
     * JAVAFX property storing whether the game is still going on.
     * */
    @Getter
    private BooleanProperty isGameLive;

    /**
     * Sends the AI (Stockfish) the current position and time controls. Creates a new {@link ChessEngineMoveTask}
     * that parses the AI's answer and gives its move. After recieving the move, this function executes it.
     * */
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

    /**
     * Restarts the game without going back into the menu.
     * */
    public void restart() {

        textArea.setVisible(false);

        if (isChessClock) {

            ExecutorUtil.stop();
            chessClockController.getChessClockProperty().unbindBidirectional(
                    chessGameController.getBoardStateProperty().get().getChessClockProperty()
            );

        }

        chessGameController = new ChessGameController(null, time, increment);

        chessGameController.setParent(chessBoard);

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

    /**
     * No argument constructor. This is called by the FXMLLoader. Only non-GUI things are initialized here.
     * */
    public GameController() {

        chessClockController = null;

        isGameLive = new SimpleBooleanProperty(
                true
        );

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
                    chessBoard,
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

    /**
     * Writes the current game state into a file. This function is called every time a move is made
     * or a second passes.
     * */
    private void saveGame() {

        File file = new File(GameMain.savedGameFileName);

        if (file.isFile()) {

            file.delete();

        }

        chessGameController.getBoardStateProperty().get().getChessClockProperty().set(
                chessClockController.getChessClockProperty().get()
        );

        chessClockController.getChessClockProperty().bindBidirectional(
                chessGameController.getBoardStateProperty().get().getChessClockProperty()
        );

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(GameMain.savedGameFileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            System.out.println(chessGameController.getBoardStateProperty().get());

            objectOutputStream.writeObject(new SerializableBoardStateImpl(chessGameController.getBoardStateProperty().get()));

            objectOutputStream.close();
            fileOutputStream.close();

        } catch (Exception exception) {

            exception.printStackTrace();

        }

    }

    /**
     * Moves back to the menu. It does this by asking the FXMLLoader to load the file menu.fxml.
     * */
    public void backToMenu() {

        if (isChessClock) {

            ExecutorUtil.stop();

        }

        /*
         * Same thing as in MenuController
         *  */

        try {

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

    /**
     * Initializes the GUI. Reads the current settings from the MenuController, sets the
     * event handlers for the chess board and moves the AI if necessary.
     * */
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

        // System.out.println(chessGameController.isPlayerInCheckmate() + " " + chessGameController.isPlayerInDraw() + " " + chessClockController.hasFlagFallen());
        isGameLive.set(
                !(
                        chessGameController.isPlayerInCheckmate() || chessGameController.isPlayerInDraw() || chessClockController.hasFlagFallen()
                )
        );

        chessGameController.setParent(chessBoard);

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

        chessBoard.getIsGameLive().bindBidirectional(
                isGameLive
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

        restartButton.disableProperty().bind(
                newGame.visibleProperty().not()
        );

        backToMenu.setOnAction(
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        backToMenu();
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

                    isGameLive.set(
                            false
                    );

                }
        );

        chessBoard.addEventHandler(
                ChessBoardEvent.CHESS_BOARD_EVENT_DRAW,
                chessBoardEvent -> {

                    textArea.setVisible(true);

                    textArea.setText("Draw");
                    newGame.setVisible(true);

                    isGameLive.set(
                            false
                    );

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

                    isGameLive.set(
                            false
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
