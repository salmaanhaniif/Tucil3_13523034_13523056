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
    
    public char getSymbol() {
        return symbol;
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

    public void move(Direction m, int distance) {
        switch (m) {
            case UP:
                y -= distance;
                break;
            case DOWN:
                y += distance;
                break;
            case LEFT:
                x -= distance;
                break;
            case RIGHT:
                x += distance;
                break;
        }
    }

    public Piece clone() {
        return new Piece(symbol, x, y, size, orientation);
    }
}
