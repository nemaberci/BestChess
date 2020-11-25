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

public class ChessEngineUtil {

    private static Process engineProcess;
    private static BufferedReader bufferedReader;
    private static OutputStreamWriter outputStreamWriter;

    public static boolean startEngine() {

        //System.out.println("ChessEngineUtil started");

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

    public static String parseStockfishAnswer() {

        try {

            while (true) {

                String line = bufferedReader.readLine();

                //System.out.println("Read line from stockfish: " + line);

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

    public static void sendStockfishNewGameCommand() {

        //System.out.println("Sending stockfish newgame command");

        try {
            outputStreamWriter.write("ucinewgame\n");
            outputStreamWriter.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public static void sendStockfishPosition(FENCode fenCode) {

        //System.out.println("Sending stockfish move to fen: " + fenCode.getFENCode());

        try {
            outputStreamWriter.write("position fen " + fenCode.getFENCode() + "\n");
            outputStreamWriter.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public static void sendStockfishTimeControls(int whiteTime, int blackTime, int increment) {

        //System.out.println("Sending stockfish with parameters wtime " + whiteTime*1000 + " btime " + blackTime*1000 + " winc " + increment*1000 + " binc " + increment*1000);

        try {
            outputStreamWriter.write("go wtime " + whiteTime*1000 + " btime " + blackTime*1000 + " winc " + increment*1000 + " binc " + increment*1000 + "\n");
            outputStreamWriter.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public static void sendStockfishNoTimeControls() {

        //System.out.println("Sending stockfish move in 2 seconds");

        try {
            outputStreamWriter.write("go movetime 2000\n");
            outputStreamWriter.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

}
