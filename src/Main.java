import java.util.Scanner;

public class Main {
    private static final boolean debug = true;
    static Board initBoard;
    static Algorithm algorithm;
    static Heuristic heuristic;

    public static void configMenu(Scanner scanner) {
        while (true) { 
            System.out.print("Enter file path: ");
            String filePath = scanner.nextLine();

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
                    System.out.println("GBFS");
                    break;
                case 3:
                    algorithm = Algorithm.ASTAR;
                    System.out.println("A*");
                    break;
            }
            if (choice == 1 || choice == 2 || choice == 3) break;
            else System.out.println("Invalid algorithm number.");
        }
    }

    public static void heuristicMenu(Scanner scanner) {
        while (true) { 
            System.out.println("Heuristics");
            System.out.println("1. Manhattan");
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
                    System.out.println("Manhattan");
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
            if (algorithm == Algorithm.ASTAR || algorithm == Algorithm.GBFS) {
                heuristicMenu(scanner);
            } else {
                heuristic = Heuristic.NONE;
            }
            
            Solver solver = new Solver(initBoard, algorithm, heuristic);
            solver.solve();

            if (scanner.nextLine().trim().equals("exit")) break;
        }
        scanner.close();
    }
}