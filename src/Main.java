import java.util.Scanner;

public class Main {

    static Board initBoard;

    public static void configMenu(Scanner scanner) {
        while (true) { 
            System.out.print("Enter file path: ");
            String filePath = scanner.nextLine();

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
            String algorithm = scanner.nextLine();

            int choice;
            try {
                choice = Integer.parseInt(algorithm);
            } catch (Exception e) {
                System.out.println("Invalid algorithm number.");
                continue;
            }
            switch (choice) {
                case 1:
                    System.out.println("UCS");
                    break;
                case 2:
                    System.out.println("GBFS");
                    break;
                case 3:
                    System.out.println("A*");
                    break;
            }
            if (choice == 1 || choice == 2 || choice == 3) break;
            else System.out.println("Invalid algorithm number.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) { 
            Board board;
            configMenu(scanner);
            algorithmMenu(scanner);

            initBoard.printBoard();

            break;
        }
        scanner.close();
    }
}