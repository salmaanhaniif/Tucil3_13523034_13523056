import java.util.ArrayList;
import java.util.List;

public class State implements Comparable<State> {
    private State parent;
    Movement movement;
    private final Board board;
    int costSoFar;
    int estimatedCostToGoal;

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
            case BLOCKER:
                this.estimatedCostToGoal = calculateBlocker();
                IOHandler.debugPrint("BLOCKER: " + this.estimatedCostToGoal);
                break;
            case BLOCKERCHAIN:
                this.estimatedCostToGoal = calculateBlockerChain();
                IOHandler.debugPrint("BLOCKER_CHAIN: " + this.estimatedCostToGoal);
                break;
        }
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

    public final int calculateManhattan() {
        Piece p = board.getPrimaryPiece();
        if (p.getOrientation() == Orientation.VERTICAL) {
            return Math.abs(p.getY() - board.getYExit());
        } else {
            return Math.abs(p.getX() - board.getXExit());
        }
    }

    public final int calculateBlocker() {
        Piece p = board.getPrimaryPiece();
        int blocker = 0;
        int start;
        if (p.getOrientation() == Orientation.VERTICAL) {
            if (p.getY() > board.getYExit()) {
                start = p.getY() - 1;
                for (int i = start; i >= 0; i--) {
                    if (board.getBoard()[i][p.getX()]) {
                        blocker += 1;
                    }
                }
            } else {
                start = p.getY() + p.getSize();
                for (int i = start; i < board.getHeight(); i++) {
                    if (board.getBoard()[i][p.getX()]) {
                        blocker += 1;
                    }
                }
            }
        } else if (p.getOrientation() == Orientation.HORIZONTAL) {
            if (p.getX() > board.getXExit()) {
                start = p.getX() - 1;
                for (int i = start; i >= 0; i--) {
                    if (board.getBoard()[p.getY()][i]) {
                        blocker += 1;
                    }
                }
            } else {
                start = p.getX() + p.getSize();
                for (int i = start; i < board.getWidth(); i++) {
                    if (board.getBoard()[p.getY()][i]) {
                        blocker += 1;
                    }
                }
            }
        }
        return blocker;
    }

    public final int calculateBlockerChain() {
        Piece p = board.getPrimaryPiece();
        int depth = 0;

        if (p.getOrientation() == Orientation.HORIZONTAL) {
            int start = p.getX() + p.getSize();
            for (int x = start; x < board.getWidth(); x++) {
                Piece blocker = board.getPieceAt(p.getY(), x);
                if (blocker != null && blocker != p) {
                    depth = Math.max(depth, 1 + getBlockerDepth(blocker));
                }
            }
        } else {
            int start = p.getY() + p.getSize();
            for (int y = start; y < board.getHeight(); y++) {
                Piece blocker = board.getPieceAt(y, p.getX());
                if (blocker != null && blocker != p) {
                    depth = Math.max(depth, 1 + getBlockerDepth(blocker));
                }
            }
        }

        return depth;
    }

    private int getBlockerDepth(Piece piece) {
        int depth = 0;

        Direction[] dirs = (piece.getOrientation() == Orientation.HORIZONTAL)
                ? new Direction[]{Direction.LEFT, Direction.RIGHT}
                : new Direction[]{Direction.UP, Direction.DOWN};

        boolean canMove = false;
        for (Direction dir : dirs) {
            for (int i = 1; i <= board.getWidth(); i++) {
                if (board.isMovePossible(piece.getSymbol(), dir, i)) {
                    canMove = true;
                    break;
                }
            }
            if (canMove) break;
        }

        if (canMove) return 1;

        // Try to find what is blocking this piece
        int px = piece.getX(), py = piece.getY();
        int depthFound = 0;

        if (piece.getOrientation() == Orientation.HORIZONTAL) {
            int start = px + piece.getSize();
            for (int x = start; x < board.getWidth(); x++) {
                Piece b = board.getPieceAt(py, x);
                if (b != null && b != piece) {
                    depthFound = Math.max(depthFound, getBlockerDepth(b));
                }
            }
        } else {
            int start = py + piece.getSize();
            for (int y = start; y < board.getHeight(); y++) {
                Piece b = board.getPieceAt(y, px);
                if (b != null && b != piece) {
                    depthFound = Math.max(depthFound, getBlockerDepth(b));
                }
            }
        }

        return 1 + depthFound;
    }

    public List<Movement> getAllPossibleMoves() {
        List<Movement> result = new ArrayList<>();

        if (board.getPrimaryPiece().getOrientation() == Orientation.VERTICAL) {
            int i = 1;
            while (board.isMovePossible(board.getPrimaryPiece().getSymbol(), Direction.UP, i)) {
                result.add(new Movement(board.getPrimaryPiece().getSymbol(), Direction.UP, i));
                i++;
            }
            i = 1;
            while (board.isMovePossible(board.getPrimaryPiece().getSymbol(), Direction.DOWN, i)) {
                result.add(new Movement(board.getPrimaryPiece().getSymbol(), Direction.DOWN, i));
                i++;
            }
        } else if (board.getPrimaryPiece().getOrientation() == Orientation.HORIZONTAL) {
            int i = 1;
            while (board.isMovePossible(board.getPrimaryPiece().getSymbol(), Direction.LEFT, i)) {
                result.add(new Movement(board.getPrimaryPiece().getSymbol(), Direction.LEFT, i));
                i++;
            }
            i = 1;
            while (board.isMovePossible(board.getPrimaryPiece().getSymbol(), Direction.RIGHT, i)) {
                result.add(new Movement(board.getPrimaryPiece().getSymbol(), Direction.RIGHT, i));
                i++;
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
            case BEAM:
                thisCost = this.estimatedCostToGoal;
                otherCost = other.estimatedCostToGoal;
                break;
        }
        IOHandler.debugPrint("thisCost: " + thisCost + ", otherCost: " + otherCost);
        return Integer.compare(thisCost, otherCost);
    }

}
