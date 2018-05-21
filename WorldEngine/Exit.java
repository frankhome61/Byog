package byog.WorldEngine;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Exit {
    private Coordinate pos;
    private int type; // 0 == LOCKED; 1 == UNLOCKED

    public Exit() {
        pos = null;
        type = -1;
    }

    public Exit(Coordinate position, int type) {
        this.pos = position;
        this.type = type;
    }

    public void setPos(Coordinate position) {
        this.pos = position;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Coordinate getPos() {
        return this.pos;
    }

    public int getType() {
        return this.type;
    }

    public boolean isLocked() {
        return type == 0;
    }
    public void setExit(TETile[][] world) {
        world[pos.getX()][pos.getY()] = Tileset.WALL;
    }
}
