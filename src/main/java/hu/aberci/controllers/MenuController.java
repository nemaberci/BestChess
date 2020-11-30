package hu.aberci.controllers;

import hu.aberci.entities.data.BoardStateImpl;
import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.PlayerColor;
import hu.aberci.entities.interfaces.SerializableBoardState;
import hu.aberci.main.GameMain;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Class responsible for the Menu GUI. This is the controller class for the FXML file menu.fxml.
 * */
public class MenuController {

    /**
     * CheckBox that stores whether AI is enabled this game.
     * */
    @Getter
    @FXML
    private CheckBox AIEnabled;

    /**
     * ComboBox that stores which side the AI plays. Is only visible when AI is enabled.
     * */
    @Getter
    @FXML
    private ComboBox<PlayerColor> aiColorComboBox;

    /**
     * Label that explains the AI ComboBox's purpose.
     * */
    @FXML
    private Label aiColorLabel;

    /**
     * Pane storing the chess clock configuration. This pane is only visible when the
     * chess clock is enabled.
     * */
    @FXML
    private Pane chessClockConfig;

    /**
     * TextField responsible for the chess clock's initial time in seconds. This is part of chessClockConfig
     * and is only visible when that pane is. This TextField only accepts numbers between 30 and 5400.
     * */
    @Getter
    @FXML
    private TextField clockTime;

    /**
     * TextField responsible for the chess clock's increment in seconds. This is part of chessClockConfig
     * and is only visible when that pane is. This TextField only accepts numbers between 1 and 120.
     * */
    @Getter
    @FXML
    private TextField clockIncrement;

    /**
     * Starts a new game. This button does not delete the currently saved game, however the
     * newly started game may.
     * */
    @FXML
    private Button startButton;

    /**
     * Loads the previously saved game and starts it. This button is only visible when there
     * is a previously saved game. Previously saved games are continued with the same clock
     * settings but AI settings are not saved.
     * */
    @FXML
    Button continueGame;

    /**
     * Deletes currently saved game. This button is only visible when there is a previously
     * saved game.
     * */
    @FXML
    Button deleteSavedGame;

    /**
     * CheckBox that stores whether the chessclock should be enabled for the next game.
     * This is always visible but changing it may hide {@link MenuController#chessClockConfig}.
     * */
    @Getter
    @FXML
    private CheckBox chessClockEnabled;

    /**
     * The BoardState that may be loaded from disk. On startup, this will be attempted to
     * be read from disk.
     * */
    @Getter
    private BoardState boardState;

    /**
     * The MenuItem that continues the current game. Functionality is equivalent to {@link MenuController#continueGame}.
     * Only visible when {@link MenuController#continueGame} is.
     * */
    @FXML
    private MenuItem continueMenuItem;

    /**
     * The MenuItem that deletes currently stored savegame. Functionality is equivalent to {@link MenuController#deleteSavedGame}.
     * Only visible when {@link MenuController#deleteSavedGame} is.
     * */
    @FXML
    private MenuItem deleteSavedGameMenuItem;

    /**
     * This controller only has one non-GUI element that cannot be initialized now. This
     * constructor does absolutely nothing.
     * */
    public MenuController() {

        boardState = null;

    }

    /**
     * Disables chess clock by setting the checkbox to not checked.
     * */
    public void disableChessClock() {

        chessClockEnabled.setSelected(false);

    }

    /**
     * Enables the chess clock and sets the time format to 30 second starting time and 1 second increment.
     * */
    public void setChessClockSettings_30_1 () {

        chessClockEnabled.setSelected(true);
        clockTime.setText("30");
        clockIncrement.setText("1");

    }

    /**
     * Enables the chess clock and sets the time format to 60 second starting time and 5 second increment.
     * */
    public void setChessClockSettings_60_5 () {

        chessClockEnabled.setSelected(true);
        clockTime.setText("60");
        clockIncrement.setText("5");

    }

    /**
     * Enables the chess clock and sets the time format to 300 second starting time and 15 second increment.
     * */
    public void setChessClockSettings_300_15 () {

        chessClockEnabled.setSelected(true);
        clockTime.setText("300");
        clockIncrement.setText("15");

    }

    /**
     * Enables the chess clock and sets the time format to 5400 second starting time and 120 second increment.
     * */
    public void setChessClockSettings_5400_120 () {

        chessClockEnabled.setSelected(true);
        clockTime.setText("5400");
        clockIncrement.setText("120");

    }

    /**
     * Starts the game by calling FXMLLoader and asking it to load the game.fxml file.
     * */
    private void loadGame() {

        try {

            final FXMLLoader loader = new FXMLLoader(
                    MenuController.class.getResource("/fxml/game.fxml")
            );

            loader.setRoot(null);

            ((Stage) startButton.getScene().getWindow()).setScene(
                    new Scene(loader.load())
            );

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Deletes the current savegame.
     * */
    public void deleteSavedGame() {

        File file = new File(GameMain.savedGameFileName);

        if (file.isFile()) {

            file.delete();
            deleteSavedGame.setVisible(false);
            boardState = null;

        }

    }

    /**
     * Starts a new game.
     * */
    public void newGame() {

        boardState = null;

        loadGame();

    }

    /**
     * Continues a currently started game.
     * */
    public void continueGame() {

        File file = new File(GameMain.savedGameFileName);

        if (file.isFile()) {

            try {

                FileInputStream fileInputStream = new FileInputStream(GameMain.savedGameFileName);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                boardState = new BoardStateImpl((SerializableBoardState) objectInputStream.readObject());

                objectInputStream.close();
                fileInputStream.close();

                loadGame();

            } catch (Exception exception) {

                exception.printStackTrace();

            }

        }

    }

    /**
     * GUI components are loaded, ComboBox list elements are added, previously saved
     * game is attempted to be loaded and the different properties are connected.
     * */
    @FXML
    public void initialize() {

        GameMain.setMenuController(this);

        aiColorComboBox.getItems().add(
                PlayerColor.WHITE
        );
        aiColorComboBox.getItems().add(
                PlayerColor.BLACK
        );

        aiColorComboBox.getSelectionModel().select(
                PlayerColor.BLACK
        );

        aiColorComboBox.visibleProperty().bindBidirectional(
                AIEnabled.selectedProperty()
        );

        aiColorLabel.visibleProperty().bindBidirectional(
                AIEnabled.selectedProperty()
        );

        try {

            if (new File(GameMain.savedGameFileName).isFile()) {

                FileInputStream fileInputStream = new FileInputStream(GameMain.savedGameFileName);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                boardState = new BoardStateImpl((SerializableBoardState) objectInputStream.readObject());

                objectInputStream.close();
                fileInputStream.close();

                continueGame.setVisible(true);
                deleteSavedGame.setVisible(true);

            } else {

                continueGame.setVisible(false);
                deleteSavedGame.setVisible(false);

            }

        } catch (Exception exception) {

            exception.printStackTrace();

            continueGame.setVisible(false);
            deleteSavedGame.setVisible(false);

        }

        continueGame.visibleProperty().bindBidirectional(
                deleteSavedGame.visibleProperty()
        );

        continueMenuItem.disableProperty().bind(
                continueGame.visibleProperty().not()
        );

        deleteSavedGameMenuItem.disableProperty().bind(
                deleteSavedGame.visibleProperty().not()
        );

        chessClockConfig.visibleProperty().bind(
                chessClockEnabled.selectedProperty()
        );

        clockTime.textProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (!newValue.matches("[0-9]+")) {
                            clockTime.setText(oldValue);
                        } else {
                            if (Integer.parseInt(newValue) < 30) {
                                clockTime.setText("30");
                            }
                            if (Integer.parseInt(newValue) > 5400) {
                                clockTime.setText("5400");
                            }
                        }
                    }
                }
        );

        clockIncrement.textProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (!newValue.matches("[0-9]+")) {
                            clockIncrement.setText(oldValue);
                        } else {
                            if (Integer.parseInt(newValue) < 0) {
                                clockIncrement.setText("0");
                            }
                            if (Integer.parseInt(newValue) > 120) {
                                clockIncrement.setText("120");
                            }
                        }
                    }
                }
        );

    }

}
