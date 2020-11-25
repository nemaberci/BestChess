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

public class GameMain extends Application {

    @Getter
    @Setter
    private static MenuController menuController;

    public static String savedGameFileName = "savegame.data";
    public static String engineExecutableName = "stockfish";

    public static void main(String args[]) {

        ExecutorUtil.init();

        launch(args);

    }

    public void start(Stage stage) throws Exception {

        stage.setTitle("BestChess");

        // System.out.println(getClass().getClassLoader().getResource("fxml/menu.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
        AnchorPane p = fxmlLoader.load();

        menuController = fxmlLoader.getController();

        Scene scene = new Scene(p);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(
                windowEvent -> {

                    Platform.exit();

                    ExecutorUtil.destroy();

                }
        );

    }

}
