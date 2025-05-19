import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IOHandler {
    static boolean debug = true;

    public static void debugPrint(String message) {
        if (debug) {
            System.out.println("[DEBUG] " + message);
        }
    }

    public static void debugPrint(char[][] messages) {
        if (debug) {
            for (char[] msgs: messages) {
                System.out.print("[DEBUG] ");
                for (char s: msgs) {
                    System.out.print(s);
                }
                System.out.println();   
            }
        }
    }

    public static Board inputFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            if ((line = br.readLine()) == null) {
                throw new IllegalArgumentException("File is invalid. Expected dimensions.");
            }

            String[] dimensions = line.trim().split(" ");
            if (dimensions.length != 2) {
                throw new IllegalArgumentException("File is invalid. Expected dimensions.");
            }

            int rows = Integer.parseInt(dimensions[0]);
            debugPrint("rows: " + rows);
            int cols = Integer.parseInt(dimensions[1]);
            debugPrint("cols: " + cols);

            if ((line = br.readLine()) == null) {
                throw new IllegalArgumentException("File is invalid. Expected piece count.");
            }

            int count = Integer.parseInt(line.trim());
            debugPrint("count: " + count);

            Board board = new Board(cols, rows, 0, 0);

            char[][] charBoard = new char[rows][cols];
            
            int curRow = 0;
            int x_exit = -99, y_exit = -99;
            boolean foundExit = false;
            while ((line = br.readLine()) != null) {
                debugPrint("line" + curRow + ": " + line);
                if (line.trim().equals("K")) {
                    if (foundExit) {
                        throw new IllegalArgumentException("File is invalid. Multiple exits found.");
                    }

                    int i = line.indexOf('K');
                    if (i >= cols) {
                        throw new IllegalArgumentException("File is invalid. Exit is not on the edge.");
                    } else {
                        x_exit = i;
                    }

                    if (curRow == 0) {
                        y_exit = -1;
                    } else if (curRow == rows) {
                        y_exit = rows;
                    } else {
                        throw new IllegalArgumentException("File is invalid. Exit is not on the edge.");
                    }

                    foundExit = true;
                } else if (curRow < rows) {
                    int curCol = 0;
                    line = line.trim();
                    for (int i = 0; i < line.length(); i++) {
                        if (line.charAt(i) == 'K') {
                            if (foundExit) {
                                throw new IllegalArgumentException("File is invalid. Multiple exits found.");
                            }

                            if (curCol == 0) {
                                x_exit = -1;
                            } else if (curCol == cols) {
                                x_exit = cols;
                            } else {
                                throw new IllegalArgumentException("File is invalid. Exit is not on the edge.");
                            }

                            y_exit = curRow;
                            foundExit = true;
                        } else if (curCol < cols) {
                            charBoard[curRow][curCol] = line.charAt(i);
                            curCol++;
                        } else {
                            throw new IllegalArgumentException("File is invalid. Column is too long.");
                        }
                    }
                    if (curCol != cols) {
                        throw new IllegalArgumentException("File is invalid. Column is too short.");
                    }
                    curRow++;
                } else {
                    throw new IllegalArgumentException("File is invalid. Row is too long.");
                }
            }
            if (curRow != rows) {
                throw new IllegalArgumentException("File is invalid. Row is too short.");
            }

            if (!foundExit) {
                throw new IllegalArgumentException("File is invalid. Exit not found.");
            }

            debugPrint("x_exit: " + x_exit);
            debugPrint("y_exit: " + y_exit);

            board.setExit(x_exit, y_exit);

            debugPrint(charBoard);

            int r = 0, c = 0;
            int created = 0;
            char[] createdPiece = new char[count + 1];
            boolean foundPrimary = false;
            boolean[][] visited = new boolean[rows][cols];
            while (r < rows) {
                if (!visited[r][c] && charBoard[r][c] != '.') {
                    if (created == count + 1) {
                        throw new IllegalArgumentException("File is invalid. Too many pieces.");
                    }
                    char symbol = charBoard[r][c];
                    for (int i = 0; i < created; i++) {
                        if (createdPiece[i] == symbol) {
                            throw new IllegalArgumentException("File is invalid. Duplicate piece.");
                        }
                    }
                    createdPiece[created] = symbol;
                    created++;
                    int size = 1;
                    Orientation orientation;
                    if (c + 1 < cols && charBoard[r][c + 1] == symbol) {
                        orientation = Orientation.HORIZONTAL;
                        while (c + size < cols && charBoard[r][c + size] == symbol) {
                            visited[r][c + size] = true;
                            size++;
                        }
                    } else if (r + 1 < rows && charBoard[r + 1][c] == symbol) {
                        orientation = Orientation.VERTICAL;
                        while (r + size < rows && charBoard[r + size][c] == symbol) {
                            visited[r + size][c] = true;
                            size++;
                        }
                    } else {
                        throw new IllegalArgumentException("File is invalid. One sized piece.");
                    }

                    Piece piece = new Piece(symbol, c, r, size, orientation);
                    if (symbol == 'P') {
                        board.addPiece(piece, true);
                        foundPrimary = true;
                        debugPrint("primary piece: " + symbol + " pos: (" + c + "," + r + ") size: " + size + " orientation: " + (orientation == Orientation.HORIZONTAL ? "horizontal" : "vertical"));
                    } else {
                        board.addPiece(piece, false);
                        debugPrint("piece: " + symbol + " pos: (" + c + "," + r + ") size: " + size + " orientation: " + (orientation == Orientation.HORIZONTAL ? "horizontal" : "vertical"));
                    }
                }
                visited[r][c] = true;
                c++;
                if (c == cols) {
                    c = 0;
                    r++;
                }
            }

        if (created < count + 1) {
            throw new IllegalArgumentException("File is invalid. Not enough pieces.");
        }

        if (!foundPrimary) {
            throw new IllegalArgumentException("File is invalid. No primary piece.");
        }
        
        debugPrint("Input from file done!");
        return board;

        } catch (IOException e) {
            throw new IllegalArgumentException("File not found.");
        } catch (NumberFormatException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
