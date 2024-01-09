package connectFourNew;

public class Board implements Cloneable {
    final int ROWS = 6;
    final int COLUMNS = 7;
    char[][] board;
    boolean isRedTurn; // To keep track of player turns

    public Board() {
        board = new char[ROWS][COLUMNS];
        initializeBoard();
        isRedTurn = true; // Red starts the game
    }
    
 // Method to create a deep copy of the Board object
    @Override
    public Board clone() {
        Board clonedBoard = new Board(); // Create a new Board object

        // Deep copy of the board
        for (int row = 0; row < ROWS; row++) {
            clonedBoard.board[row] = this.board[row].clone(); // Clone each row individually
        }

        clonedBoard.isRedTurn = this.isRedTurn; // Copy the boolean field
        return clonedBoard;
    }

    private void initializeBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                board[row][col] = ' '; // Empty space in the beginning
            }
        }
    }

    public boolean isValidMove(int column) {
        return column >= 0 && column < COLUMNS && board[0][column] == ' ';
    }
    

    public void makeMove(int column, boolean isRed) {
        if (isValidMove(column)) {
            int row = ROWS - 1;
            while (row >= 0 && board[row][column] != ' ') {
                row--;
            }
            if (row >= 0) {
                char piece = isRed ? 'R' : 'Y'; // 'R' for red, 'Y' for yellow
                board[row][column] = piece;
            }
        }
    }

    public boolean checkWinner() {
        // Check horizontally
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col <= COLUMNS - 4; col++) {
                char current = board[row][col];
                if (current != ' ' && current == board[row][col + 1] && current == board[row][col + 2]
                        && current == board[row][col + 3]) {
                    return true;
                }
            }
        }
        // Check vertically
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row <= ROWS - 4; row++) {
                char current = board[row][col];
                if (current != ' ' && current == board[row + 1][col] && current == board[row + 2][col]
                        && current == board[row + 3][col]) {
                    return true;
                }
            }
        }
        // Check diagonally
        for (int row = 0; row <= ROWS - 4; row++) {
            for (int col = 0; col <= COLUMNS - 4; col++) {
                char current = board[row][col];
                if (current != ' ' && current == board[row + 1][col + 1] && current == board[row + 2][col + 2]
                        && current == board[row + 3][col + 3]) {
                    return true;
                }
            }
        }
        // Check reverse diagonally
        for (int row = 0; row <= ROWS - 4; row++) {
            for (int col = 3; col < COLUMNS; col++) {
                char current = board[row][col];
                if (current != ' ' && current == board[row + 1][col - 1] && current == board[row + 2][col - 2]
                        && current == board[row + 3][col - 3]) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isBoardFull() {
        for (int col = 0; col < COLUMNS; col++) {
            if (board[0][col] == ' ') {
                return false;
            }
        }
        return true;
    }

    public boolean isGameOver() {
        return checkWinner() || isBoardFull();
    }
    
    public void undoMove(int column) {
        int row = 0;
        while (row < ROWS && board[row][column] == ' ') {
            row++;
        }

        if (row > 0) {
            board[row - 1][column] = ' '; // Clear the piece from the specified column
        }
    }
    

    public void printBoard() {
        for (int row = -1; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (row < 0) {
                    System.out.print(" " + (col + 1) + " ");
                } else {
                    System.out.print("[");
                    System.out.print(board[row][col] + "]");
                }
            }
            System.out.println();
        }
    }


    // Other methods for player input, game loop, etc.
}

