package hu.aberci.util;

import hu.aberci.entities.interfaces.*;
import hu.aberci.main.GameMain;
import org.apache.commons.lang3.SystemUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/*
* Inspired by https://github.com/rahular/chess-misc/blob/master/JavaStockfish/src/com/rahul/stockfish/Stockfish.java
* */

/**
 * Util function that deals with the engine executable.
 * */
public class ChessEngineUtil {

    /**
     * The engine to be worked with.
     * */
    private static Process engineProcess;
    /**
     * The reader used for reading engine output.
     * */
    private static BufferedReader bufferedReader;
    /**
     * The writer used for giving commands to the engine.
     * */
    private static OutputStreamWriter outputStreamWriter;

    /**
     * Initializes the static variables.
     *
     * @return {@code true} if the engine could be started and {@code false} otherwise.
     * */
    public static boolean startEngine() {

        try {

            String engineFileName = GameMain.engineExecutableName;
            if (SystemUtils.IS_OS_WINDOWS) {
                engineFileName = engineFileName.concat(".exe");
            }

            if (!(new File(engineFileName)).isFile()) {

                return false;

            }

            engineProcess = Runtime.getRuntime().exec(GameMain.engineExecutableName);
            bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            engineProcess.getInputStream()
                    )
            );
            outputStreamWriter = new OutputStreamWriter(
                    engineProcess.getOutputStream()
            );

        } catch (Exception exception) {

            exception.printStackTrace();
            return false;

        }

        return true;

    }

    /**
     * Reads output of engine until it finds a line that starts with "bestmove".
     *
     * @return The line from the engine that starts with "bestmove".
     * */
    public static String parseStockfishAnswer() {

        try {

            while (true) {

                String line = bufferedReader.readLine();

                if (line.startsWith("bestmove")) {
                    //System.out.println("Parsed move: " + line);
                    return line;
                }

            }

        } catch (Exception exception) {

            exception.printStackTrace();
            return "";

        }

    }

    /**
     * Sends the engine a New Game UCI command.
     * */
    public static void sendStockfishNewGameCommand() {

        try {
            outputStreamWriter.write("ucinewgame\n");
            outputStreamWriter.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Sends the engine the current position. It uses FEC codes for this.
     *
     * @param fenCode The FENCode of the current position.
     * */
    public static void sendStockfishPosition(FENCode fenCode) {

        try {
            outputStreamWriter.write("position fen " + fenCode.getFENCode() + "\n");
            outputStreamWriter.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Sends stockfish the current time controls and tells it to make a move in the current position.
     * This should only be used when time controls are enabled.
     *
     * @param whiteTime White's remaining time in seconds.
     * @param blackTime Black's remaining time in seconds.
     * @param increment Increment in seconds.
     * */
    public static void sendStockfishTimeControls(int whiteTime, int blackTime, int increment) {

        try {
            outputStreamWriter.write("go wtime " + whiteTime*1000 + " btime " + blackTime*1000 + " winc " + increment*1000 + " binc " + increment*1000 + "\n");
            outputStreamWriter.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Tells stockfish to make a move by only thinking 12 moves deep. This should
     * not take too long, however it could be replaced by a hard limit on time (eg. "go movespeed 500" would mean
     * that it makes a move in .5 seconds).
     * */
    public static void sendStockfishNoTimeControls() {

        try {
            outputStreamWriter.write("go depth 12\n");
            outputStreamWriter.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

}
