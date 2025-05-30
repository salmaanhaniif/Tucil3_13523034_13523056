import java.util.Scanner;

public class Main {
    private static final boolean debug = false;
    static String filePath;
    static Board initBoard;
    static Algorithm algorithm;
    static int n_algo = 0;
    static Heuristic heuristic;

    public static void configMenu(Scanner scanner) {
        while (true) { 
            System.out.print("Enter file path: ");
            filePath = scanner.nextLine();

            if (debug && filePath.equals("")) {
                filePath = "test/input.txt";
            } 

            try {
                initBoard = IOHandler.inputFromFile(filePath); 
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                continue;
            }
            break;
        }
    }

    public static void algorithmMenu(Scanner scanner) {
        while (true) { 
            System.out.println("Pathfinding algorithms");
            System.out.println("1. UCS");
            System.out.println("2. GBFS");
            System.err.println("3. A*");
            System.err.println("4. Beam Search");
            System.out.print("Enter algorithm number: ");
            String choiceString = scanner.nextLine();
            
            int choice;
            if (debug && choiceString.equals("")) {
                algorithm = Algorithm.UCS;
                break;
            } 

            try {
                choice = Integer.parseInt(choiceString);
            } catch (Exception e) {
                System.out.println("Invalid algorithm number.");
                continue;
            }
            switch (choice) {
                case 1:
                    algorithm = Algorithm.UCS;
                    System.out.println("UCS");
                    break;
                case 2:
                    algorithm = Algorithm.GBFS;
                    n_algo = 99999;
                    System.out.println("GBFS");
                    break;
                case 3:
                    algorithm = Algorithm.ASTAR;
                    System.out.println("A*");
                    break;
                case 4:
                    algorithm = Algorithm.BEAM;
                    n_algoMenu(scanner);
                    System.out.println("Beam Search");
                    break;
            }
            if (choice == 1 || choice == 2 || choice == 3 || choice == 4) break;
            else System.out.println("Invalid algorithm number.");
        }
    }

    public static void n_algoMenu(Scanner scanner) {
        while (true) { 
            System.out.print("Enter n for beam search: ");
            String n = scanner.nextLine();
            try {
                n_algo = Integer.parseInt(n);
            } catch (Exception e) {
                System.out.println("Invalid n.");
                continue;
            }
            if (n_algo >= 0) break;
            else System.out.println("Invalid n. N must be >= 0.");
        }
    }

    public static void heuristicMenu(Scanner scanner) {
        while (true) { 
            System.out.println("Heuristics");
            System.out.println("1. Distance to Exit");
            System.out.println("2. Blocker Count");
            System.out.println("3. Blocker Chaining");
            System.out.print("Enter heuristic number: ");
            String algorithm = scanner.nextLine();
            
            int choice;
            if (debug && algorithm.equals("")) {
                choice = 1;
                break;
            } 

            try {
                choice = Integer.parseInt(algorithm);
            } catch (Exception e) {
                System.out.println("Invalid heuristic number.");
                continue;
            }
            switch (choice) {
                case 1:
                    heuristic = Heuristic.MANHATTAN;
                    System.out.println("Heuristic picked : Distance to Exit");
                    break;
                case 2:
                    heuristic = Heuristic.BLOCKER;
                    System.out.println("Heuristic picked : Blocker Count");
                    break;
                case 3:
                    heuristic = Heuristic.BLOCKERCHAIN;
                    System.out.println("Heuristic picked : Blocker Chaining");
                    break;
            }
            if (choice == 1 || choice == 2 || choice == 3) break;
            else System.out.println("Invalid heuristic number.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) { 
            Board board;
            configMenu(scanner);
            algorithmMenu(scanner);
            if (algorithm == Algorithm.ASTAR || algorithm == Algorithm.GBFS || algorithm == Algorithm.BEAM) {
                heuristicMenu(scanner);
            } else {
                heuristic = Heuristic.NONE;
            }
            
            Solver solver = new Solver(initBoard, algorithm, n_algo, heuristic);
            String solution = solver.solve();

            try {
                IOHandler.outputToFile(IOHandler.addOutputPrefix(filePath), solution);
            } catch (Exception e) {
                System.out.println("File Output Error: " + e.getMessage());
            }

            if (scanner.nextLine().trim().equals("exit")) break;
        }
        scanner.close();
    }
}