package hu.aberci.entities.data;

import lombok.Data;
import hu.aberci.entities.interfaces.FENCode;

/**
 * Data class responsible for storing FEN codes.
 * No special logic is implemented in this class, for more info see: {@link FENCode}
 * */
@Data
public class FENCodeImpl implements FENCode {

    private String board = "";
    private String turn = "0";
    private String castle = "-";
    private String enPassant = "-";
    private String halfTurns = "0";
    private String turnNumber = "0";

    public String getFENCode() {

        return board + " " +
                turn + " " +
                castle + " " +
                enPassant + " " +
                halfTurns + " " +
                turnNumber;

    }

}
