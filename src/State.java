import java.util.ArrayList;
import java.util.List;

public class State {
    private Board board;
    private AllPossibleMoves allPossibleMoves;

    public State(Board board) {
        this.board = board;
        this.allPossibleMoves = new AllPossibleMoves(board);
    }


    public class Move {
        public Movement direction;
        public int distance;
    }

    public class AllPossibleMovesOfAPiece{
        private Piece piece;
        // private Board board;
        private List<Move> possibleMoves;

        public AllPossibleMovesOfAPiece(Piece piece, Board board) {
            this.piece = piece;
            // this.board = board;
            this.possibleMoves = generateAllPossibleMoves(board);
        }

        public char getSymbol() {
            return piece.getSymbol();
        }

        public Move newMove(Movement direction, int distance) {
            Move move = new Move();
            move.direction = direction;
            move.distance = distance;
            return move;
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

    public class AllPossibleMoves{
        private List<AllPossibleMovesOfAPiece> allPosibbleMoves;
        // private Board board;

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
    }
}
