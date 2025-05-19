
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


public class Solver {
    // Algorithm;
    private static Algorithm algorithm;
    private static Heuristic heuristic;
    Board board; // Inisial state
    // prioqueue
    private final PriorityQueue<State> queue;
    // visited state
    private final Set<String> visited;

    public Solver(Board board, Algorithm algorithm, Heuristic heuristic) {
        this.board = board;
        Solver.algorithm = algorithm;
        Solver.heuristic = heuristic;
        queue = new PriorityQueue<>();
        visited = new HashSet<>();
    }

    public static Algorithm getAlgorithm() {
        return algorithm;
    }

    public static Heuristic getHeuristic() {
        return heuristic;
    }

    public void explore(State state) {
        List<State.Move> allMoves = state.getAllPossibleMoves();
        for (State.Move move : allMoves) {
            Board newBoard = state.getBoard().clone();
            newBoard.movePiece(move.symbol, move.direction, move.distance);

            State newState = new State(newBoard, false, state.costSoFar + 1);
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
