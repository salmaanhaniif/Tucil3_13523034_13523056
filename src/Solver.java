
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


public class Solver {
    // Algorithm;
    private Algorithm algorithm;
    private Heuristic heuristic;
    Board board; // Inisial state
    // prioqueue
    private PriorityQueue<State> queue = new PriorityQueue<>(); // queue of state
    // visited state
    private Set<String> visited = new HashSet<>();

    public Solver(Board board, Algorithm algorithm, Heuristic heuristic) {
        this.board = board;
        this.algorithm = algorithm;
        this.heuristic = heuristic;
    }

    public void explore(State state) {
        // dapetin anak state
        // buat state baru untuk setiap move dari state lama
        // cek apakah state baru itu goal
        // filter berdasarkan apakah sudah pernah dikunjungi state yang similar
        // hitung heuristik dari masing2 state
        // jika belum pernah dikunjungi, masukkan ke dalam prioqueue
        List<State.Movement> allMoves = state.getAllPossibleMoves();
        for (State.Movement move : allMoves) {
            // System.out.println("Move: " + move.symbol + " " + move.direction + " " + move.distance);
            Board newBoard = state.getBoard().clone();
            newBoard.movePiece(move.symbol, move.direction, move.distance);
            
            State newState = new State(newBoard, false, state.cost + 1);
            newState.setParent(state);
            newState.setMovement(new State.Movement(move.symbol, move.direction, move.distance));

            if (!visited.contains(newState.getBoard().hashCodeSigma())) {
                queue.add(newState);
            }
        }
    }

    public void solve() {
        queue.clear();
        visited.clear();
        
        State initialState = new State(this.board, true, 0);
        queue.add(initialState);

        // queue.add(initialState);
        visited.add(initialState.getBoard().hashCodeSigma());
        
        while (!queue.isEmpty()) {
            State currentState = queue.poll();
            
            if (currentState.getBoard().isGoal()) {
                System.out.println("Found a solution!");
                printSolutionPath(currentState);
                return;
            }
            
            visited.add(currentState.getBoard().hashCodeSigma());
            explore(currentState);
            // currentState.getBoard().printBoard();
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
        System.out.println("Solution path (total moves: " + (path.size()) + "):");
        int i = 0;
        for (State s : path) {
            if (i==0) {
                System.out.println("Initial State");
            } else {
                System.out.println("Step " + (i) + ":");
            }
            if (s.getMovement() != null) {
                System.out.println("Move: " + s.getMovement().getSymbol() + " Direction: " + s.getMovement().getDirection() + " Distance: " + s.getMovement().getDistance());
            }
            i++;
            if (s.getMovement() == null) {
                s.getBoard().printBoard();
            } else {
                s.getBoard().printColouredBoard(s.getMovement());
            }
        }
        State lastState = path.get(path.size() - 1);
        lastState.getBoard().removePiece(lastState.getBoard().getPrimaryPiece().getSymbol());
        System.out.println("Final State:");
        lastState.getBoard().printBoard();
    }
}
