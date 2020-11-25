package hu.aberci.entities.data;

import lombok.Data;

@Data
public class FENCodeImpl implements hu.aberci.entities.interfaces.FENCode {

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
