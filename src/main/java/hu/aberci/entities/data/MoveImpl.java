package hu.aberci.entities.data;

import hu.aberci.entities.interfaces.BoardState;
import hu.aberci.entities.interfaces.Move;
import hu.aberci.entities.interfaces.Piece;
import hu.aberci.entities.interfaces.Tile;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class MoveImpl implements Move {

    BoardState boardState;

    Tile targetTile;

    Tile sourceTile;

    Piece piece;

    public MoveImpl(BoardState b, Tile t, Piece p) {

        boardState = b;
        targetTile = t;
        piece = p;
        sourceTile = p.getTileProperty().get();

    }

    public MoveImpl(BoardState b, Tile sourceTile1, Tile targetTile1, Piece p) {

        targetTile = targetTile1;
        sourceTile = sourceTile1;
        piece = p;
        boardState = b;

    }

}
