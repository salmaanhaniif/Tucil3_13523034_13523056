import java.util.ArrayList;
import java.util.List;

public class State {
    private State parent;
    private Board board;
    int cost;

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

    public static class Move {
        public Movement direction;
        public int distance;
    }

    public static class AllPossibleMovesOfAPiece{
        private Piece piece;
        // private Board board;
        private List<Move> possibleMoves;

        public AllPossibleMovesOfAPiece(Piece piece, Board board) {
            this.piece = piece;
            // this.board = board;
            this.possibleMoves = generateAllPossibleMoves(board);
        }

        public Move newMove(Movement direction, int distance) {
            Move move = new Move();
            move.direction = direction;
            move.distance = distance;
            return move;
        }

        public Piece getPiece() {
            return piece;
        }

        public List<Move> generateAllPossibleMoves(Board board) {
            List<Move> result = new ArrayList<>();
            Orientation orientation = piece.getOrientation();

            if (orientation == Orientation.VERTICAL) {
                for (int i = 1; i <= board.getHeight() - piece.getSize(); i++) {
                    if (board.isMovePossible(piece.getSymbol(), Movement.UP, i)) {
                        result.add(newMove(Movement.UP, i));
                    }
                    if (board.isMovePossible(piece.getSymbol(), Movement.DOWN, i)) {
                        result.add(newMove(Movement.DOWN, i));
                    }
                }
            } else if (orientation == Orientation.HORIZONTAL) {
                for (int i = 1; i <= board.getWidth() - piece.getSize(); i++) {
                    if (board.isMovePossible(piece.getSymbol(), Movement.LEFT, i)) {
                        result.add(newMove(Movement.LEFT, i));
                    }
                    if (board.isMovePossible(piece.getSymbol(), Movement.RIGHT, i)) {
                        result.add(newMove(Movement.RIGHT, i));
                    }
                }
            }
            return result;
        }
    }

    public static class AllPossibleMoves{
        private List<AllPossibleMovesOfAPiece> allPosibbleMoves;
        // private Board board;

        public List<AllPossibleMovesOfAPiece> getAllPossibleMoves() {
            return allPosibbleMoves;
        }

        public AllPossibleMoves(Board board) {
            // this.board = board;
            this.allPosibbleMoves = new ArrayList<AllPossibleMovesOfAPiece>();
            for (Piece piece : board.getListOfPieces()) {
                AllPossibleMovesOfAPiece allPosibbleMovesOfAPiece = new AllPossibleMovesOfAPiece(piece, board);
                allPosibbleMoves.add(allPosibbleMovesOfAPiece);
            }
            AllPossibleMovesOfAPiece primaryPieceMoves = new AllPossibleMovesOfAPiece(board.getPrimaryPiece(), board);
            allPosibbleMoves.add(primaryPieceMoves);
        }
        public void add(AllPossibleMovesOfAPiece allPosibbleMovesOfAPiece) {
            allPosibbleMoves.add(allPosibbleMovesOfAPiece);
        }
    }
}
