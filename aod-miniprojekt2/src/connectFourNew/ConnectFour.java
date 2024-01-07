package connectFourNew;

import java.util.Scanner;

public class ConnectFour {
	static int globalCount = 0;
	public static void main(String[] args) {
		//maximizing the player == RED
		//minimizing the player == YELLOW
		
		Board board = new Board();
        board.printBoard();
        Scanner scanner = new Scanner(System.in);
        boolean isRedTurn = true;
        int depth = 40;

        while (!board.isGameOver()) {
            int column;
            if (isRedTurn) {
                System.out.print("Red's turn. Enter column (1-7): ");
                column = scanner.nextInt() - 1;
            } else {
                System.out.println("Yellow's turn (Opponent)");
                column = getBestMove(board.clone(),depth);
                // Make the best move found by alphaBeta
            }

            if (board.isValidMove(column)) {
                board.makeMove(column, isRedTurn);
                board.printBoard();
                isRedTurn = !isRedTurn; // Switch player turns
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
        scanner.close();
    }
	
    public static int getBestMove(Board board, int depth) {
        int bestMove = -1;
        int bestValue = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        
        Board copyBoard = board.clone();

        for (int column = 0; column < board.COLUMNS; column++) {
            Board currentBoard = copyBoard.clone(); // Create a new instance from the copied board
            if (currentBoard.isValidMove(column)) {
                currentBoard.makeMove(column, currentBoard.isRedTurn);
                int value = min(currentBoard, depth - 1, alpha, beta);
                currentBoard.undoMove(column);

                //System.out.println("Col nbr: " + column + " Value: " + value + " BestValue: " + bestValue);

                if (value > bestValue) {
                    bestValue = value;
                    bestMove = column;
                }
                alpha = Math.max(alpha, bestValue);
            }
        }
        System.out.println(globalCount);
        return bestMove;
    }

    private static int max(Board board, int depth, int alpha, int beta) {
        if (depth == 0 || board.isGameOver()) {
            return evaluate(board);
        }

        int value = Integer.MIN_VALUE;
        
        globalCount++;
        //System.out.println("globalCount: " + globalCount);

        for (int column = 0; column < board.COLUMNS; column++) {
            if (board.isValidMove(column)) {
                board.makeMove(column, board.isRedTurn);
                value = Math.max(value, min(board, depth - 1, alpha, beta));
                board.undoMove(column);
                alpha = Math.max(alpha, value);
                if (alpha <= beta) {
                    break;
                }
            }
        }
        return value;
    }

    private static int min(Board board, int depth, int alpha, int beta) {
        if (depth == 0 || board.isGameOver()) {
            return evaluate(board);
        }

        int value = Integer.MAX_VALUE;
        
        globalCount++;
        //System.out.println("globalCount: " + globalCount);

        for (int column = 0; column < board.COLUMNS; column++) {
            if (board.isValidMove(column)) {
                board.makeMove(column, !board.isRedTurn);
                value = Math.min(value, max(board, depth - 1, alpha, beta));
                board.undoMove(column);
                beta = Math.min(beta, value);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return value;
    }


	/**
     * Evaluates the given board state based on connected pieces for each player.
     *
     * @param board The current game board.
     * @return The evaluation score based on connected pieces for both players.
     */
	public static int evaluate(Board board) {
	    int redScore = countConnectedPieces(board, 'R');
	    int yellowScore = countConnectedPieces(board, 'Y');

	    // Evaluate the board position
	    return redScore - yellowScore;
	}
	/**
     * Counts the number of connected pieces for the specified player.
     *
     * @param board       The current game board.
     * @param playerPiece The player's piece ('R' for red, 'Y' for yellow).
     * @return The count of connected pieces for the specified player.
     */
	private static int countConnectedPieces(Board board, char playerPiece) {
	    int score = 0;

	    // Check horizontally
	    for (int row = 0; row < board.ROWS; row++) {
	        for (int col = 0; col <= board.COLUMNS - 4; col++) {
	            int count = 0;
	            int openEnds = 0;
	            for (int i = 0; i < 4; i++) {
	                if (board.board[row][col + i] == playerPiece) {
	                    count++;
	                } else if (board.board[row][col + i] == ' ') {
	                    openEnds++;
	                }
	            }
	            score += calculateScore(count, openEnds);
	        }
	    }

	    // Check vertically
	    for (int col = 0; col < board.COLUMNS; col++) {
	        for (int row = 0; row <= board.ROWS - 4; row++) {
	            int count = 0;
	            int openEnds = 0;
	            for (int i = 0; i < 4; i++) {
	                if (board.board[row + i][col] == playerPiece) {
	                    count++;
	                } else if (board.board[row + i][col] == ' ') {
	                    openEnds++;
	                }
	            }
	            score += calculateScore(count, openEnds);
	        }
	    }

	    // Check diagonally
	    for (int row = 0; row <= board.ROWS - 4; row++) {
	        for (int col = 0; col <= board.COLUMNS - 4; col++) {
	            int count = 0;
	            int openEnds = 0;
	            for (int i = 0; i < 4; i++) {
	                if (board.board[row + i][col + i] == playerPiece) {
	                    count++;
	                } else if (board.board[row + i][col + i] == ' ') {
	                    openEnds++;
	                }
	            }
	            score += calculateScore(count, openEnds);
	        }
	    }

	 // Check reverse diagonally
	    for (int row = 0; row <= board.ROWS - 4; row++) {
	        for (int col = 3; col < board.COLUMNS; col++) {
	            int count = 0;
	            int openEnds = 0;
	            for (int i = 0; i < 4; i++) {
	                if (board.board[row + i][col - i] == playerPiece) {
	                    count++;
	                } else if (board.board[row + i][col - i] == ' ') {
	                    openEnds++;
	                }
	            }
	            score += calculateScore(count, openEnds);
	        }
	    }

	    return score;
	}
	private static int calculateScore(int count, int openEnds) {
	    if (count == 4) {
	        return 100; // Four in a row
	    } else if (count == 3 && openEnds == 0) {
	        return 5; // Three in a row with both ends closed
	    } else if (count == 3 && openEnds == 1) {
	        return 3; // Three in a row with one end open
	    } else if (count == 2 && openEnds == 2) {
	        return 2; // Two in a row with both ends open
	    } else {
	        return 0; // No significant sequence detected
	    }
	}
}
