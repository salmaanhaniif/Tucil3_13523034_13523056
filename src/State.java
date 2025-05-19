import java.util.ArrayList;
import java.util.List;

public class State implements Comparable<State> {
    private State parent;
    private Board board;
    Movement movement;
    int cost;

    public static class Movement {
        public char symbol;
        public Direction direction;
        public int distance;

        public Movement(char symbol, Direction direction, int distance) {
            this.symbol = symbol;
            this.direction = direction;
            this.distance = distance;
        }

        public char getSymbol() {
            return this.symbol;
        }
        public Direction getDirection() {
            return this.direction;
        }
        public int getDistance() {
            return this.distance;
        }
    }

    public State(Board board, boolean isInitial, int cost) {
        if (isInitial) {
            this.parent = null;
            this.cost = 0;
        } else {
            this.parent = this;
            this.cost = cost;
        }
        this.board = board;
    }

    public Movement getMovement() {
        return this.movement;
    }

    public State getParent() {
        return this.parent;
    }

    public void setParent(State parent) {
        this.parent = parent;
    }

    public Board getBoard() {
        return this.board;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    public boolean equals(Board board) {
        return this.board.isBoardEqual(board);
    }

    public List<Movement> getAllPossibleMoves() {
        List<Movement> result = new ArrayList<>();

        if (board.getPrimaryPiece().getOrientation() == Orientation.VERTICAL) {
            for (int i = 1; i <= board.getHeight() - board.getPrimaryPiece().getSize(); i++) {
                if (board.isMovePossible(board.getPrimaryPiece().getSymbol(), Direction.UP, i)) {
                    result.add(new Movement(board.getPrimaryPiece().getSymbol(), Direction.UP, i));
                }
                if (board.isMovePossible(board.getPrimaryPiece().getSymbol(), Direction.DOWN, i)) {
                    result.add(new Movement(board.getPrimaryPiece().getSymbol(), Direction.DOWN, i));
                }
            }
        } else if (board.getPrimaryPiece().getOrientation() == Orientation.HORIZONTAL) {
            for (int i = 1; i <= board.getWidth() - board.getPrimaryPiece().getSize(); i++) {
                if (board.isMovePossible(board.getPrimaryPiece().getSymbol(), Direction.LEFT, i)) {
                    result.add(new Movement(board.getPrimaryPiece().getSymbol(), Direction.LEFT, i));
                }
                if (board.isMovePossible(board.getPrimaryPiece().getSymbol(), Direction.RIGHT, i)) {
                    result.add(new Movement(board.getPrimaryPiece().getSymbol(), Direction.RIGHT, i));
                }
            }
        }

        for (Piece piece : board.getListOfPieces()) {
            if (piece.getOrientation() == Orientation.VERTICAL) {
                for (int i = 1; i <= board.getHeight() - piece.getSize(); i++) {
                    if (board.isMovePossible(piece.getSymbol(), Direction.UP, i)) {
                        result.add(new Movement(piece.getSymbol(), Direction.UP, i));
                    }
                    if (board.isMovePossible(piece.getSymbol(), Direction.DOWN, i)) {
                        result.add(new Movement(piece.getSymbol(), Direction.DOWN, i));
                    }
                }
            } else if (piece.getOrientation() == Orientation.HORIZONTAL) {
                for (int i = 1; i <= board.getWidth() - piece.getSize(); i++) {
                    if (board.isMovePossible(piece.getSymbol(), Direction.LEFT, i)) {
                        result.add(new Movement(piece.getSymbol(), Direction.LEFT, i));
                    }
                    if (board.isMovePossible(piece.getSymbol(), Direction.RIGHT, i)) {
                        result.add(new Movement(piece.getSymbol(), Direction.RIGHT, i));
                    }
                }
            }
        }

        return result;
    }

    public int compareTo(State other) {
        return Integer.compare(this.cost, other.cost);
    }

}
