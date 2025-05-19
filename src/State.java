import java.util.ArrayList;
import java.util.List;

public class State implements Comparable<State> {
    private State parent;
    private final Board board;
    int costSoFar;
    int estimatedCostToGoal;

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

    public State(Board board, boolean isInitial, int costSoFar) {
        if (isInitial) {
            this.parent = null;
            this.costSoFar = 0;
        } else {
            this.parent = this;
            this.costSoFar = costSoFar;
        }
        this.board = board;
        switch (Solver.getHeuristic()) {
            case NONE:
                this.estimatedCostToGoal = 0;
                break;
            case MANHATTAN:
                this.estimatedCostToGoal = calculateManhattan();
                IOHandler.debugPrint("MANHATTAN: " + this.estimatedCostToGoal);
                break;
        }
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

    public final int calculateManhattan() {
        Piece p = board.getPrimaryPiece();
        if (p.getOrientation() == Orientation.VERTICAL) {
            return Math.abs(p.getY() - board.getYExit());
        } else {
            return Math.abs(p.getX() - board.getXExit());
        }
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

    @Override
    public int compareTo(State other) {
        int thisCost = 0, otherCost = 0;
        switch (Solver.getAlgorithm()) {
            case ASTAR:
                thisCost = this.costSoFar + this.estimatedCostToGoal;
                otherCost = other.costSoFar + other.estimatedCostToGoal;
                break;
            case UCS:
                thisCost = this.costSoFar;
                otherCost = other.costSoFar;
                break;
            case GBFS:
                thisCost = this.estimatedCostToGoal;
                otherCost = other.estimatedCostToGoal;
                break;
        }
        IOHandler.debugPrint("thisCost: " + thisCost + ", otherCost: " + otherCost);
        return Integer.compare(thisCost, otherCost);
    }

}
