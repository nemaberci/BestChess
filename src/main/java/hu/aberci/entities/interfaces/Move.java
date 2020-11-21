package hu.aberci.entities.interfaces;

public interface Move {

    Piece getPiece();
    BoardState getBoardState();
    Tile getTargetTile();

}
