
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


public class Solver {
    private static int nodesExplored;
    private static long timeExecution;
    // Algorithm;
    private static Algorithm algorithm;
    private static int n_algo;
    private static Heuristic heuristic;
    Board board; // Inisial state
    // prioqueue
    private final PriorityQueue<State> queue;
    // visited state
    private final Set<String> visited;

    public Solver(Board board, Algorithm algorithm, int n_algo, Heuristic heuristic) {
        this.board = board;
        Solver.algorithm = algorithm;
        Solver.n_algo = n_algo;
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
        nodesExplored++;

        List<State.Movement> allMoves = state.getAllPossibleMoves();
        List<State> allStates = new ArrayList<>();
        
        for (State.Movement move : allMoves) {
            Board newBoard = state.getBoard().clone();
            newBoard.movePiece(move.symbol, move.direction, move.distance);

            State newState = new State(newBoard, false, state.costSoFar + 1);
            newState.setParent(state);
            newState.setMovement(new State.Movement(move.symbol, move.direction, move.distance));

            allStates.add(newState);
        }

        if (algorithm == Algorithm.GBFS || algorithm == Algorithm.BEAM) {
            allStates = allStates.stream()
                .sorted()
                .limit(Solver.n_algo)
                .toList();
        }

        for (State newState : allStates) {
            if (!visited.contains(newState.getBoard().hashCodeSigma())) {
                queue.add(newState);
            }  
        }
    }

    public String solve() {
        queue.clear();
        visited.clear();
        
        State initialState = new State(this.board, true, 0);
        queue.add(initialState);

        // queue.add(initialState);
        visited.add(initialState.getBoard().hashCodeSigma());
        
        nodesExplored = 0;
        timeExecution = System.currentTimeMillis();

        while (!queue.isEmpty()) {
            State currentState = queue.poll();
            
            if (currentState.getBoard().isGoal()) {
                System.out.println("Found a solution!");
                timeExecution = System.currentTimeMillis() - timeExecution;
                return printSolutionPath(currentState);
            }
            
            visited.add(currentState.getBoard().hashCodeSigma());
            explore(currentState);
            // currentState.getBoard().printBoard();
        }
        
        System.out.println("No solution found.");
        return "No solution found.";
    }

    private String printSolutionPath(State state) {
        String output = "";
        System.out.println("Nodes Explored: " + nodesExplored);
        output += "Nodes Explored: " + nodesExplored + "\n";
        System.out.println("Time Execution: " + (timeExecution) + " ms");
        output += "Time Execution: " + (timeExecution) + " ms\n";
        List<State> path = new ArrayList<>();
        while (state != null) {
            path.add(state);
            state = state.getParent();
        }
        Collections.reverse(path);
        System.out.println("Solution path (total moves: " + (path.size()) + "):");
        output += "Solution path (total moves: " + (path.size()) + "):\n";
        int i = 0;
        for (State s : path) {
            if (i==0) {
                System.out.println("Initial State");
                output += "Initial State\n";
            } else {
                System.out.println("Step " + (i) + ":");
                output += "Step " + (i) + ":\n";
            }
            if (s.getMovement() != null) {
                System.out.println(s.getMovement().getSymbol() + " - " + s.getMovement().getDirection() + " - " + s.getMovement().getDistance());
                output += s.getMovement().getSymbol() + " - " + s.getMovement().getDirection() + " - " + s.getMovement().getDistance() + "\n";
            }
            i++;
            if (s.getMovement() == null) {
                output += s.getBoard().printBoard();
            } else {
                output += s.getBoard().printColouredBoard(s.getMovement());
            }
        }
        State lastState = path.get(path.size() - 1);
        Board newBoard = lastState.getBoard().clone();
        Direction lastDir;
        int lastDist;
        if (newBoard.getPrimaryPiece().getOrientation() == Orientation.VERTICAL) {
            if (newBoard.getYExit() > newBoard.getPrimaryPiece().getY()) {
                lastDir = Direction.DOWN;
                lastDist = newBoard.getYExit() - newBoard.getPrimaryPiece().getY() - newBoard.getPrimaryPiece().getSize();
            } else {
                lastDir = Direction.UP;
                lastDist = newBoard.getPrimaryPiece().getY();
            }
        } else {
            if (newBoard.getXExit() > newBoard.getPrimaryPiece().getX()) {
                lastDir = Direction.RIGHT;
                lastDist = newBoard.getXExit() - newBoard.getPrimaryPiece().getX() - newBoard.getPrimaryPiece().getSize();
            } else {
                lastDir = Direction.LEFT;
                lastDist = newBoard.getPrimaryPiece().getX();
            }
        }
        newBoard.movePiece('P', lastDir, lastDist);

        State finalState = new State(newBoard, false, 0);
        finalState.setMovement(new State.Movement('P', lastDir, lastDist));
        System.out.println("Step " + (path.size()) + ":");
        output += "Step " + (path.size()) + ":\n";
        System.out.println(finalState.getMovement().getSymbol() + " - " + finalState.getMovement().getDirection() + " - " + finalState.getMovement().getDistance());
        output += finalState.getMovement().getSymbol() + " - " + finalState.getMovement().getDirection() + " - " + finalState.getMovement().getDistance() + "\n";
        output += finalState.getBoard().printColouredBoard(finalState.getMovement());

        return output;
    }
}
