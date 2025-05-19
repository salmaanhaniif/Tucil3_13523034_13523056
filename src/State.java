import java.util.ArrayList;
import java.util.List;

public class State implements Comparable<State> {
    private State parent;
    private Board board;
    int cost;

    public static class Move {
        public char symbol;
        public Movement direction;
        public int distance;

        public Move(char symbol, Movement direction, int distance) {
            this.symbol = symbol;
            this.direction = direction;
            this.distance = distance;
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

    public State getParent() {
        return this.parent;
    }

    public void setParent(State parent) {
        this.parent = parent;
    }

    public Board getBoard() {
        return this.board;
    }

    public boolean equals(Board board) {
        return this.board.isBoardEqual(board);
    }

    public int hashCode() {
        return this.board.hashCode();
    }

    public List<Move> getAllPossibleMoves() {
        List<Move> result = new ArrayList<>();

        if (board.getPrimaryPiece().getOrientation() == Orientation.VERTICAL) {
            for (int i = 1; i <= board.getHeight() - board.getPrimaryPiece().getSize(); i++) {
                if (board.isMovePossible(board.getPrimaryPiece().getSymbol(), Movement.UP, i)) {
                    result.add(new Move(board.getPrimaryPiece().getSymbol(), Movement.UP, i));
                }
                if (board.isMovePossible(board.getPrimaryPiece().getSymbol(), Movement.DOWN, i)) {
                    result.add(new Move(board.getPrimaryPiece().getSymbol(), Movement.DOWN, i));
                }
            }
        } else if (board.getPrimaryPiece().getOrientation() == Orientation.HORIZONTAL) {
            for (int i = 1; i <= board.getWidth() - board.getPrimaryPiece().getSize(); i++) {
                if (board.isMovePossible(board.getPrimaryPiece().getSymbol(), Movement.LEFT, i)) {
                    result.add(new Move(board.getPrimaryPiece().getSymbol(), Movement.LEFT, i));
                }
                if (board.isMovePossible(board.getPrimaryPiece().getSymbol(), Movement.RIGHT, i)) {
                    result.add(new Move(board.getPrimaryPiece().getSymbol(), Movement.RIGHT, i));
                }
            }
        }

        for (Piece piece : board.getListOfPieces()) {
            if (piece.getOrientation() == Orientation.VERTICAL) {
                for (int i = 1; i <= board.getHeight() - piece.getSize(); i++) {
                    if (board.isMovePossible(piece.getSymbol(), Movement.UP, i)) {
                        result.add(new Move(piece.getSymbol(), Movement.UP, i));
                    }
                    if (board.isMovePossible(piece.getSymbol(), Movement.DOWN, i)) {
                        result.add(new Move(piece.getSymbol(), Movement.DOWN, i));
                    }
                }
            } else if (piece.getOrientation() == Orientation.HORIZONTAL) {
                for (int i = 1; i <= board.getWidth() - piece.getSize(); i++) {
                    if (board.isMovePossible(piece.getSymbol(), Movement.LEFT, i)) {
                        result.add(new Move(piece.getSymbol(), Movement.LEFT, i));
                    }
                    if (board.isMovePossible(piece.getSymbol(), Movement.RIGHT, i)) {
                        result.add(new Move(piece.getSymbol(), Movement.RIGHT, i));
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
