public class Piece {
    private char symbol;
    private int x;
    private int y;
    private int size;
    private Orientation orientation;
    
    public Piece(char symbol, int x, int y, int size, Orientation orientation) {
        this.symbol = symbol;
        this.x = x;
        this.y = y;
        this.size = size;
        this.orientation = orientation;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getSize() {
        return size;
    }
    
    public Orientation getOrientation() {
        return orientation;
    }

    public boolean equal(Piece p) {
        return (x == p.getX() && y == p.getY() && size == p.getSize() && orientation == p.getOrientation());
    }

    public void move(Movement m) {
        switch (m) {
            case UP:
                y--;
                break;
            case DOWN:
                y++;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
        }
    }
}
