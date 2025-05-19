
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


public class Solver {
    // Algorithm;
    private Algorithm algorithm;
    Board board; // Inisial state
    // prioqueue
    private PriorityQueue<State> queue = new PriorityQueue<>(); // queue of state
    // visited state
    private Set<State> visited = new HashSet<>();
    private State solutionState = null;
    private boolean isSolved = false;

    public Solver(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void explore(State state) {
        // dapetin anak state
        // buat state baru untuk setiap move dari state lama
        // cek apakah state baru itu goal
        // filter berdasarkan apakah sudah pernah dikunjungi state yang similar
        // hitung heuristik dari masing2 state
        // jika belum pernah dikunjungi, masukkan ke dalam prioqueue
        State.AllPossibleMoves allMoves = new State.AllPossibleMoves(state.getBoard());
        for (State.AllPossibleMovesOfAPiece moves : allMoves.getAllPossibleMoves()) {
            Piece piece = moves.getPiece();
            for (State.Move move : moves.generateAllPossibleMoves(state.getBoard())) {
                Board newBoard = state.getBoard().clone();
                newBoard.movePiece(piece.getSymbol(), move.direction, move.distance);
                
                // Heuristik (?)
                
                State newState = new State(newBoard, false, state.cost + 1);
                newState.setParent(state);
                
                if (!visited.contains(newState)) {
                    queue.add(newState);
                }
            }
        }
    }

    public void solve() {
        queue.clear();
        visited.clear();
        isSolved = false;
        solutionState = null;
        
        State initialState = new State(this.board, true, 0);
        
        // queue.add(initialState);
        visited.add(initialState);
        
        while (!queue.isEmpty()) {
            State currentState = queue.poll();
            
            if (currentState.getBoard().isGoal()) {
                System.out.println("Found a solution!");
                printSolutionPath(currentState);
                return;
            }
            
            visited.add(currentState);
            explore(currentState);
        }
        
        System.out.println("No solution found.");
    }

    private void printSolutionPath(State state) {
        List<State> path = new ArrayList<>();
        while (state != null) {
            path.add(state);
            state = state.getParent();
        }
        Collections.reverse(path);
        System.out.println("Solution path (total moves: " + (path.size() - 1) + "):");
        int i = 0;
        for (State s : path) {
            if (i==0) {
                System.out.println("Initial State");
            } else {
                System.out.println("Step " + (i) + ":");
            }
            s.getBoard().printBoard();
        }
    }
}
