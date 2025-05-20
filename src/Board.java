import java.util.*;

public class Board {
    private final boolean debug = true;
    private final int width;
    private final int height;
    private boolean[][] board;
    private Piece primaryPiece;
    private List<Piece> listOfPieces;
    private int x_Exit;
    private int y_Exit;

    public Board() {
        this.height = 4;
        this.width = 5;
        this.board = new boolean[4][5]; 
        this.primaryPiece = null;
        this.listOfPieces = new ArrayList<Piece>();
        this.x_Exit = 0;
        this.y_Exit = 0;
    }

    public Board(int width, int height, int x_Exit, int y_Exit) {
        this.width = width;
        this.height = height;
        this.board = new boolean[height][width];
        this.primaryPiece = null;
        this.listOfPieces = new ArrayList<Piece>();
        this.x_Exit = x_Exit;
        this.y_Exit = y_Exit;
        // this.listOfPieces = new ArrayList<Piece> (); tidak perlu inisialisasi lagi
    }

    public Board clone() {
        Board newBoard = new Board(this.getWidth(), this.getHeight(), this.x_Exit, this.y_Exit);
        this.listOfPieces.forEach(piece -> newBoard.addPiece(piece.clone(), false));
        newBoard.addPiece(this.getPrimaryPiece().clone(), true);
        return newBoard;
    }

    public void printDebug(String message) {
        if (this.debug) {
            System.out.println("[DEBUG] " + message);
        }
    }

    public boolean isBoardEqual(Board b) {
        if (this.width != b.getWidth() || this.height != b.getHeight()) {
            return false;
        }
        if (this.listOfPieces.size() != b.getListOfPieces().size()) {
            return false;
        }
        for (int i = 0; i < this.listOfPieces.size(); i++) {
            if (!this.listOfPieces.get(i).equal(b.getListOfPieces().get(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean isGoal() {
        // Check if the primary piece is at the exit
        if (this.primaryPiece.getOrientation() == Orientation.HORIZONTAL) {
            int x_DistanceToExit = this.x_Exit - this.primaryPiece.getX();
            Direction dir;
            // Jika di kiri akan bernilai negatif, jika di kanan akan bernilai positif
            if (x_DistanceToExit < 0) {
                x_DistanceToExit = -x_DistanceToExit;
                dir = Direction.LEFT;
            } else {
                x_DistanceToExit = x_DistanceToExit - this.primaryPiece.getSize();
                dir = Direction.RIGHT;
            }
            if (isMovePossible(primaryPiece.getSymbol(), dir, x_DistanceToExit)) {
                return true;
            }

        } else if (this.primaryPiece.getOrientation() == Orientation.VERTICAL) {
            int y_DistanceToExit = this.y_Exit - this.primaryPiece.getY();
            Direction dir;
            if (y_DistanceToExit < 0) {
                y_DistanceToExit = -y_DistanceToExit;
                dir = Direction.UP;
            } else {
                y_DistanceToExit = y_DistanceToExit - this.primaryPiece.getSize();
                dir = Direction.DOWN;
            }
            if (isMovePossible(primaryPiece.getSymbol(), dir, y_DistanceToExit)) {
                return true;
            }
        }
        return false;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Piece getPrimaryPiece() {
        return this.primaryPiece;
    }

    public String getStringExit() {
        return "(" + this.x_Exit + "," + this.y_Exit + ")";
    }

    public int getXExit() {
        return this.x_Exit;
    }

    public int getYExit() {
        return this.y_Exit;
    }

    public void setExit(int x, int y) {
        this.x_Exit = x;
        this.y_Exit = y;
    }

    public boolean[][] getBoard() {
        return this.board;
    }

    public void setBoard(boolean[][] board) {
        this.board = board;
    }

    public void addPiece(Piece piece, boolean isPrimary) {
        // Check if the piece is already in the list
        for (Piece p : this.listOfPieces) {
            if (p.equal(piece)) {
                printDebug("Piece already exists in the list.");
                return;
            }
        }

        if (piece.getX() < 0 || piece.getY() < 0 || piece.getX() >= this.width || piece.getY() >= this.height) {
            printDebug("Piece " + piece.getSymbol() + " is out of bounds.");
            return;
        }
        if (piece.getOrientation() == Orientation.VERTICAL && piece.getSize() > this.height) {
            printDebug("Piece " + piece.getSymbol() + " is too long.");
            return;
        }
        if (piece.getOrientation() == Orientation.HORIZONTAL && piece.getSize() > this.width) {
            printDebug("Piece " + piece.getSymbol() + " is too long.");
            return;
        }
        if (piece.getOrientation() == Orientation.HORIZONTAL && (piece.getX()+piece.getSize()) > this.width) {
            printDebug("Piece " + piece.getSymbol() + " is out of bounds.");
            return;
        }
        if (piece.getOrientation() == Orientation.VERTICAL && (piece.getY()+piece.getSize()) > this.height) {
            printDebug("Piece " + piece.getSymbol() + " is out of bounds.");
            return;
        }

        if (isPrimary) primaryPiece = piece;
        else this.listOfPieces.add(piece);


        if (piece.getOrientation() == Orientation.VERTICAL) {
            for (int i = 0; i < piece.getSize(); i++) {
                this.board[piece.getY() + i][piece.getX()] = true;
            }
        } else if (piece.getOrientation() == Orientation.HORIZONTAL) {
            for (int i = 0; i < piece.getSize(); i++) {
                this.board[piece.getY()][piece.getX() + i] = true;
            }
        }
    }

    public void removePiece(char symbol) {
        Piece piece = getPiece(symbol);
        if (piece == null) {
            printDebug("Piece not found.");
            return;
        }
        this.listOfPieces.remove(piece);
        if (piece.getSymbol() == this.primaryPiece.getSymbol()) {
            this.primaryPiece = null;
        }
        if (piece.getOrientation() == Orientation.VERTICAL) {
            for (int i = 0; i < piece.getSize(); i++) {
                this.board[piece.getY() + i][piece.getX()] = false;
            }
        } else if (piece.getOrientation() == Orientation.HORIZONTAL) {
            for (int i = 0; i < piece.getSize(); i++) {
                this.board[piece.getY()][piece.getX() + i] = false;
            }
        }
    }

    public boolean isMovePossible(char symbol, Direction direction, int distance) {
        Piece piece = getPiece(symbol);
        if (piece == null) {
            printDebug("Piece not found.");
            return false;
        }

        int x = piece.getX();
        int y = piece.getY();
        int size = piece.getSize();
        Orientation orient = piece.getOrientation();

        // 1) Orientation and bounds check
        if (orient == Orientation.VERTICAL) {
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                printDebug("Piece " + symbol + " cannot move " + direction + " when vertical.");
                return false;
            }
            if (direction == Direction.UP && y - distance < 0) {
                printDebug("Piece " + symbol + " would move out of top bounds.");
                return false;
            }
            if (direction == Direction.DOWN && y + size - 1 + distance >= getHeight()) {
                printDebug("Piece " + symbol + " would move out of bottom bounds.");
                return false;
            }
        } else { // HORIZONTAL
            if (direction == Direction.UP || direction == Direction.DOWN) {
                printDebug("Piece " + symbol + " cannot move " + direction + " when horizontal.");
                return false;
            }
            if (direction == Direction.LEFT && x - distance < 0) {
                printDebug("Piece " + symbol + " would move out of left bounds.");
                return false;
            }
            if (direction == Direction.RIGHT && x + size - 1 + distance >= getWidth()) {
                printDebug("Piece " + symbol + " would move out of right bounds.");
                return false;
            }
        }

        // 2) Collision check: only check the 'front' cells over the distance
        switch (direction) {
            case RIGHT:
                for (int i = 1; i <= distance; i++) {
                    if (board[y][x + size - 1 + i]) {
                        printDebug("Collision detected at (" + (x+size-1+i) + "," + y + ").");
                        return false;
                    }
                }
                break;

            case LEFT:
                for (int i = 1; i <= distance; i++) {
                    if (board[y][x - i]) {
                        printDebug("Collision detected at (" + (x-i) + "," + y + ").");
                        return false;
                    }
                }
                break;

            case UP:
                for (int i = 1; i <= distance; i++) {
                    if (board[y - i][x]) {
                        printDebug("Collision detected at (" + x + "," + (y-i) + ").");
                        return false;
                    }
                }
                break;

            case DOWN:
                for (int i = 1; i <= distance; i++) {
                    if (board[y + size - 1 + i][x]) {
                        printDebug("Collision detected at (" + x + "," + (y+size-1+i) + ").");
                        return false;
                    }
                }
                break;
        }

        // No issues: move is possible
        return true;
    }


    public void updateBoard(Piece piece, Direction direction, int distance) {
        int x = piece.getX();
        int y = piece.getY();
        int size = piece.getSize();

        switch (direction) {
        case UP:
            for (int i = 0; i < distance; i++) {
                // Hapus baris paling bawah
                board[y + size - 1 - i][x] = false;
                // Tambah baris di atas
                board[y - 1 - i][x] = true;
            }
            break;

        case DOWN:
            for (int i = 0; i < distance; i++) {
                // Hapus baris paling atas
                board[y + i][x] = false;
                // Tambah baris di bawah
                board[y + size + i][x] = true;
            }
            break;

        case LEFT:
            for (int i = 0; i < distance; i++) {
                // Hapus kolom paling kanan
                board[y][x + size - i - 1] = false;
                // Tambah kolom di kiri
                board[y][x - i - 1] = true;
            }
            break;

        case RIGHT:
            for (int i = 0; i < distance; i++) {
                // Hapus kolom paling kiri
                board[y][x + i] = false;
                // Tambah kolom di kanan
                board[y][x + size + i] = true;
            }
            break;
        }
    }

    public void movePiece(char symbol, Direction direction, int distance) {
        updateBoard(getPiece(symbol), direction, distance);
        getPiece(symbol).move(direction, distance);
    }

    public Piece getPiece(char symbol) {
        if (symbol == this.primaryPiece.getSymbol()) {
            return this.primaryPiece;
        }
        for (Piece piece : this.listOfPieces) {
            if (piece.getSymbol() == symbol) {
                return piece;
            }
        }
        return null;
    }

    public List<Piece> getListOfPieces() {
        return this.listOfPieces;
    }

    public void setListOfPieces(List<Piece> listOfPieces) {
        this.listOfPieces = listOfPieces;
    }

    public String printBoard() {
        // Board of Characters
        char[][] charBoard = new char[this.height + 2][this.width + 2];

        // Fill the board with '.'
        for (int i = 1; i < this.height + 1; i++) {
            for (int j = 1; j < this.width + 1; j++) {
                charBoard[i][j] = '.';
            }
        }

        // Board of Pieces
        // Primary Piece
        if (this.primaryPiece != null) {
            if (this.primaryPiece.getOrientation() == Orientation.VERTICAL) {
                for (int i = 0; i<this.primaryPiece.getSize(); i++) {
                    charBoard[(this.primaryPiece.getY()+1) + i][(this.primaryPiece.getX()+1)] = this.primaryPiece.getSymbol();
                }
            }
            else if (this.primaryPiece.getOrientation() == Orientation.HORIZONTAL) {
                for (int i = 0; i<this.primaryPiece.getSize(); i++) {
                    charBoard[(this.primaryPiece.getY()+1)][(this.primaryPiece.getX()+1) + i] = this.primaryPiece.getSymbol();
                }
            }
        }
        // Other Pieces
        for (Piece piece : this.listOfPieces) {
            if (piece.getOrientation() == Orientation.VERTICAL) {
                for (int i = 0; i<piece.getSize(); i++) {
                    charBoard[(piece.getY()+1) + i][(piece.getX()+1)] = piece.getSymbol();
                }
            }
            else if (piece.getOrientation() == Orientation.HORIZONTAL) {
                for (int i = 0; i<piece.getSize(); i++) {
                    charBoard[(piece.getY()+1)][(piece.getX()+1) + i] = piece.getSymbol();
                }
            }
        }

        // Print the board
        String boardString = "";
        for (int i = 0; i < this.height+2; i++) {
            for (int j = 0; j < this.width+2; j++) {
                if (i==0) {
                    if (i==y_Exit + 1 && j==x_Exit + 1) {
                        System.out.print("^^");
                        boardString += "^^";
                    } else if (j==0) {
                        System.out.print("╔═");
                        boardString += "╔═";
                    } else if (j==this.width+1) {
                        System.out.print("╗");
                        boardString += "╗";
                    } else {
                        System.out.print("══");
                        boardString += "══";
                    }
                } else if (i==this.height+1) {
                    if (i==y_Exit + 1 && j==x_Exit + 1) {
                        System.out.print("vv");
                        boardString += "vv";
                    }
                    else if (j==0) {
                        System.out.print("╚═");
                        boardString += "╚═";
                    } else if (j==this.width+1) {
                        System.out.print("╝");
                        boardString += "╝";
                    } else {
                        System.out.print("══");
                        boardString += "══";
                    }
                } else if (j == 0) {
                    if (i == y_Exit + 1 && j == x_Exit + 1) {
                        System.out.print("<"); // Kosongkan kiri
                        boardString += "<";
                    } else {
                        System.out.print("║ ");
                        boardString += "║ ";
                    }
                } else if (j == width + 1) {
                    if (i == y_Exit + 1 && j == x_Exit + 1) {
                        System.out.print(">"); // Kosongkan kanan
                        boardString += ">";
                    } else {
                        System.out.print("║");
                        boardString += "║";
                    }
                } else {
                    System.out.print(charBoard[i][j] + " ");
                    boardString += charBoard[i][j] + " ";
                }
            }
            System.err.println();
            boardString += "\n";
        }

        return boardString;
    }

    public void printBooleanBoard() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (this.board[i][j]) {
                    System.out.print("1 ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }

    public String hashCodeSigma() {
        String code = "";
        code += this.primaryPiece.getSymbol() + this.primaryPiece.getX() + "," + this.primaryPiece.getY();
        for (Piece piece : this.listOfPieces) {
            code += piece.getSymbol() + piece.getX() + "," + piece.getY();
        }
        return code;
    }

    public String printColouredBoard(State.Movement m) {
        // Board of Characters
        char[][] charBoard = new char[this.height + 2][this.width + 2];

        // Fill the board with '.'
        for (int i = 1; i < this.height + 1; i++) {
            for (int j = 1; j < this.width + 1; j++) {
                charBoard[i][j] = '.';
            }
        }

        // Board of Pieces
        // Primary Piece
        if (this.primaryPiece != null) {
            if (this.primaryPiece.getOrientation() == Orientation.VERTICAL) {
                for (int i = 0; i<this.primaryPiece.getSize(); i++) {
                    charBoard[(this.primaryPiece.getY()+1) + i][(this.primaryPiece.getX()+1)] = this.primaryPiece.getSymbol();
                }
            }
            else if (this.primaryPiece.getOrientation() == Orientation.HORIZONTAL) {
                for (int i = 0; i<this.primaryPiece.getSize(); i++) {
                    charBoard[(this.primaryPiece.getY()+1)][(this.primaryPiece.getX()+1) + i] = this.primaryPiece.getSymbol();
                }
            }
        }
        // Other Pieces
        for (Piece piece : this.listOfPieces) {
            if (piece.getOrientation() == Orientation.VERTICAL) {
                for (int i = 0; i<piece.getSize(); i++) {
                    charBoard[(piece.getY()+1) + i][(piece.getX()+1)] = piece.getSymbol();
                }
            }
            else if (piece.getOrientation() == Orientation.HORIZONTAL) {
                for (int i = 0; i<piece.getSize(); i++) {
                    charBoard[(piece.getY()+1)][(piece.getX()+1) + i] = piece.getSymbol();
                }
            }
        }

        Piece movedPiece = getPiece(m.getSymbol());
        int x_Initial;
        int y_Initial;
        int x_Final;
        int y_Final;
        if (m.direction == Direction.DOWN) {
            x_Initial = movedPiece.getX();
            x_Final = movedPiece.getX();
            y_Initial = movedPiece.getY() - m.getDistance();
            y_Final = movedPiece.getY() + movedPiece.getSize() - 1;
        } else if (m.direction == Direction.UP) {
            x_Initial = movedPiece.getX();
            x_Final = movedPiece.getX();
            y_Initial = movedPiece.getY();
            y_Final = movedPiece.getY() + m.getDistance() + movedPiece.getSize() - 1;
        } else if (m.direction == Direction.LEFT) {
            x_Initial = movedPiece.getX();
            x_Final = movedPiece.getX() + m.getDistance() + movedPiece.getSize() - 1;
            y_Initial = movedPiece.getY();
            y_Final = movedPiece.getY();
        } else { // RIGHT
            x_Initial = movedPiece.getX() - m.getDistance();
            x_Final = movedPiece.getX() + movedPiece.getSize() - 1;
            y_Initial = movedPiece.getY();
            y_Final = movedPiece.getY();
        }

        // Print the board
        String boardString = "";
        for (int i = 0; i < this.height+2; i++) {
            for (int j = 0; j < this.width+2; j++) {
                if (i==0) {
                    if (i==y_Exit + 1 && j==x_Exit + 1) {
                        System.out.print("\u001B[32m^^\u001B[0m");
                        boardString += "^^";
                    } else if (j==0) {
                        System.out.print("╔═");
                        boardString += "╔═";
                    } else if (j==this.width+1) {
                        System.out.print("╗");
                        boardString += "╗";
                    } else {
                        System.out.print("══");
                        boardString += "══";
                    }
                } else if (i==this.height+1) {
                    if (i==y_Exit + 1 && j==x_Exit + 1) {
                        System.out.print("\u001B[32mvv\u001B[0m");
                        boardString += "vv";
                    }
                    else if (j==0) {
                        System.out.print("╚═");
                        boardString += "╚═";
                    } else if (j==this.width+1) {
                        System.out.print("╝");
                        boardString += "╝";
                    } else {
                        System.out.print("══");
                        boardString += "══";
                    }
                } else if (j == 0) {
                    if (i == y_Exit + 1 && j == x_Exit + 1) {
                        System.out.print("\u001B[32m<\u001B[0m"); // Kosongkan kiri
                        boardString += "<";
                    } else {
                        System.out.print("║ ");
                        boardString += "║ ";
                    }
                } else if (j == width + 1) {
                    if (i == y_Exit + 1 && j == x_Exit + 1) {
                        System.out.print("\u001B[32m>\u001B[0m"); // Kosongkan kanan
                        boardString += ">";
                    } else {
                        System.out.print("║");
                        boardString += "║";
                    }
                } else {
                    if (i >= y_Initial + 1 && i <= y_Final + 1 && j >= x_Initial + 1 && j <= x_Final + 1 && (m.direction == Direction.RIGHT || m.direction == Direction.LEFT)) {
                        if (charBoard[i][j] == 'P') System.out.print("\u001B[31m");
                        System.out.print("\u001B[104m\u001B[97m" + charBoard[i][j] + " " + "\u001B[0m");
                        if (charBoard[i][j] == 'P') System.out.print("\u001B[0m");
                        boardString += charBoard[i][j] + " ";
                    } else if (i >= y_Initial + 1 && i <= y_Final + 1 && j >= x_Initial + 1 && j <= x_Final + 1 && (m.direction == Direction.UP || m.direction == Direction.DOWN)){
                        if (charBoard[i][j] == 'P') System.out.print("\u001B[31m");
                        System.out.print("\u001B[104m\u001B[97m" + charBoard[i][j] + "\u001B[0m ");
                        if (charBoard[i][j] == 'P') System.out.print("\u001B[0m");
                        boardString += charBoard[i][j] + " ";
                    } else {
                        if (charBoard[i][j] == 'P') System.out.print("\u001B[31m");
                        System.out.print(charBoard[i][j] + " ");
                        if (charBoard[i][j] == 'P') System.out.print("\u001B[0m");
                        boardString += charBoard[i][j] + " ";
                    }
                }
            }
            System.out.println();
            boardString += "\n";
        }

        return boardString;
    }
    
    // DEBUGGING
    // public static void main(String[] args) {
    //     Board board = new Board(10, 10, 10, 2);

    //     Piece piece1 = new Piece('A', 1, 1, 3, Orientation.HORIZONTAL);
    //     Piece piece2 = new Piece('B', 3, 1, 2, Orientation.HORIZONTAL);
    //     Piece piece3 = new Piece('C', 0, 0, 5, Orientation.HORIZONTAL);
    //     Piece piece4 = new Piece('D', 3, 0, 2, Orientation.VERTICAL);
        
    //     board.addPiece(piece1, false);
    //     board.addPiece(piece2, false);
    //     board.addPiece(piece3, false);
    //     board.addPiece(piece4, false);
    //     board.printBoard();
    //     board.printBooleanBoard();
    //     if (board.isMovePossible('A', Movement.LEFT, 2)) {
    //         board.movePiece('A', Movement.LEFT, 2);
    //     }
    //     if (board.isMovePossible('A', Movement.LEFT, 1)) {
    //         board.movePiece('A', Movement.LEFT, 1);
    //     }
    //     board.addPiece(piece4, false);
    //     board.printBoard();
    //     board.printBooleanBoard();
    //     if (board.isMovePossible('D', Movement.LEFT, 1)) {
    //         board.movePiece('D', Movement.LEFT, 1);
    //     }
    //     if (board.isMovePossible('D', Movement.DOWN, 1)) {
    //         board.movePiece('D', Movement.DOWN, 1);
    //     }
    //     board.printBoard();
    //     board.printBooleanBoard();
    //     if (board.isMovePossible('D', Movement.DOWN, 2)) {
    //         board.movePiece('D', Movement.DOWN, 2);
    //     }
    //     if (board.isMovePossible('D', Movement.UP, 1)) {
    //         board.movePiece('D', Movement.UP, 1);
    //     }
    //     if (board.isMovePossible('A', Movement.RIGHT, 1)) {
    //         board.movePiece('A', Movement.RIGHT, 1);
    //     }
    //     board.printBoard();
    //     board.printBooleanBoard();
    //     board.removePiece('D');
    //     board.printBoard();
    //     board.printBooleanBoard();
    //     if (board.isMovePossible('A', Movement.RIGHT, 1)) {
    //         board.movePiece('A', Movement.RIGHT, 1);
    //     }
    //     board.printBoard();
    //     board.printBooleanBoard();
    //     if (board.isMovePossible('A', Movement.RIGHT, 1)) {
    //         board.movePiece('A', Movement.RIGHT, 1);
    //     }
    //     if (board.isMovePossible('A', Movement.UP, 1)) {
    //         board.movePiece('A', Movement.UP, 1);
    //     }
    // }
}
