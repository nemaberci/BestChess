package hu.aberci.entities.interfaces;

public interface Move {

    Piece getPiece();
    BoardState getBoardState();
    Tile getTargetTile();
    Tile getSourceTile();
    Move setTargetTile(Tile tile);
    Move setSourceTile(Tile tile);
    Move setBoardState(BoardState boardState);
    Move setPiece(Piece piece);
    boolean isCapture();

}
