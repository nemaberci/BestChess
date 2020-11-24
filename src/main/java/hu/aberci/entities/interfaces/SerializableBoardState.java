package hu.aberci.entities.interfaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public interface SerializableBoardState extends Serializable {

    ArrayList<ArrayList<SerializableTile>> getTiles();
    PlayerColor getPlayerColor();
    SerializableChessClock getChessClock();
    ArrayList<SerializableMove> getMoves();
    ArrayList<SerializablePiece> getTakenPieces();
    HashMap<PlayerColor, ArrayList<SerializablePiece>> getPieces();

}
