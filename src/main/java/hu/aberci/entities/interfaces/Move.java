package hu.aberci.entities.interfaces;

public interface Move {

    Piece getPiece();
    BoardState getBoardState();
    Tile getTargetTile();
    Tile getSourceTile();
    void setTargetTile(Tile tile);
    void setSourceTile(Tile tile);
    void setBoardState(BoardState boardState);
    void setPiece(Piece piece);

}
