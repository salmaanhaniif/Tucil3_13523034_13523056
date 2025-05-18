import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IOHandler {
    static boolean debug = true;

    private static void debugPrint(String message) {
        if (debug) {
            System.out.println("[debug] " + message);
        }
    }

    private static void debugPrint(char[][] messages) {
        if (debug) {
            for (char[] msgs: messages) {
                System.out.print("[debug] ");
                for (char s: msgs) {
                    System.out.print(s);
                }
                System.out.println();   
            }
        }
    }

    public static void inputFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            if ((line = br.readLine()) == null) {
                System.out.println("File is invalid.");
                return;
            }

            String[] dimensions = line.trim().split(" ");
            if (dimensions.length != 2) {
                System.out.println("File is invalid.");
                return;
            }

            int rows = Integer.parseInt(dimensions[0]);
            debugPrint("rows: " + rows);
            int cols = Integer.parseInt(dimensions[1]);
            debugPrint("cols: " + cols);

            if ((line = br.readLine()) == null) {
                System.out.println("File is invalid.");
                return;
            }

            int count = Integer.parseInt(line.trim());
            debugPrint("count: " + count);

            char[][] charBoard = new char[rows][cols];
            
            int curRow = 0;
            int x_exit = -99, y_exit = -99;
            boolean foundExit = false;
            while ((line = br.readLine()) != null) {
                debugPrint("line" + curRow + ": " + line);
                if (line.trim().equals("K")) {
                    if (foundExit) {
                        System.out.println("File is invalid. Multiple exits found.");
                        return;
                    }

                    int i = line.indexOf('K');
                    if (i >= cols) {
                        System.out.println("File is invalid. Exit is not on the edge.");
                        return;
                    } else {
                        x_exit = i;
                    }

                    if (curRow == 0) {
                        y_exit = -1;
                    } else if (curRow == rows) {
                        y_exit = rows;
                    } else {
                        System.out.println("File is invalid. Exit is not on the edge.");
                        return;
                    }

                    foundExit = true;
                } else if (curRow < rows) {
                    int curCol = 0;
                    line = line.trim();
                    for (int i = 0; i < line.length(); i++) {
                        if (line.charAt(i) == 'K') {
                            if (foundExit) {
                                System.out.println("File is invalid. Multiple exits found.");
                                return;
                            }

                            if (curCol == 0) {
                                x_exit = -1;
                            } else if (curCol == cols) {
                                x_exit = cols;
                            } else {
                                System.out.println("File is invalid. Exit is not on the edge.");
                                return;
                            }

                            y_exit = curRow;
                            foundExit = true;
                        } else if (curCol < cols) {
                            charBoard[curRow][curCol] = line.charAt(i);
                            curCol++;
                        } else {
                            System.out.println("File is invalid. Column is too long.");
                            return;
                        }
                    }
                    if (curCol != cols) {
                        System.out.println("File is invalid. Column is too short.");
                        return;
                    }
                    curRow++;
                } else {
                    System.out.println("File is invalid. Row is too long.");
                    return;
                }
            }
            if (curRow != rows) {
                System.out.println("File is invalid. Row is too short.");
                return;
            }

            if (!foundExit) {
                System.out.println("File is invalid. Exit not found.");
                return;
            }

            debugPrint("x_exit: " + x_exit);
            debugPrint("y_exit: " + y_exit);

            debugPrint(charBoard);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid file format.");
        }
    }
}
