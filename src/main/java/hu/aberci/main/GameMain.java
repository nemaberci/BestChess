package hu.aberci.main;

import hu.aberci.controllers.MenuController;
import hu.aberci.util.ExecutorUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.ObjectInputStream;

/**
 * Main class of the application
 * */
public class GameMain extends Application {

    /**
     * Controller instance stored to be accessed from the game controller. Game settings
     * are set and stored in the menu controller.
     * */
    @Getter
    @Setter
    private static MenuController menuController;

    /**
     * Filename for the savegame file.
     * */
    public static final String savedGameFileName = "savegame.data";

    /**
     * Filename for the executable of the engine.
     * */
    public static final String engineExecutableName = "stockfish";

    /**
     * Main function. Launches the JavaFX application and initializes the ExecutorUtil.
     * */
    public static void main(String args[]) {

        ExecutorUtil.init();

        launch(args);

    }

    /**
     * Starts the application by loading the menu controller and setting the static menucontroller variable
     * to the controller instance.
     * */
    public void start(Stage stage) throws Exception {

        stage.setTitle("BestChess");

        // System.out.println(getClass().getClassLoader().getResource("fxml/menu.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
        AnchorPane p = fxmlLoader.load();

        menuController = fxmlLoader.getController();

        Scene scene = new Scene(p);
        stage.setScene(scene);
        stage.show();

        stage.setResizable(false);

        stage.setOnCloseRequest(
                windowEvent -> {

                    Platform.exit();

                    ExecutorUtil.destroy();

                }
        );

    }

}
