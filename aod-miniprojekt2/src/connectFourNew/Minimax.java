package connectFourNew;


public class Minimax {

    public static int minimax(Board board, int depth, boolean maximizingPlayer) {
        if (depth == 0 || board.isGameOver()) {
            if (board.checkWinner()) {
                return maximizingPlayer ? Integer.MIN_VALUE + 1 : Integer.MAX_VALUE - 1;
            }
            return 0; // If it's a draw
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < board.COLUMNS; col++) {
                if (board.isValidMove(col)) {
                    board.makeMove(col, board.isRedTurn);
                    depth = depth-1;
                    int eval = minimax(board, depth, false);
                    maxEval = Math.max(maxEval, eval);
                    board.undoMove(col);
                    depth = depth+1;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < board.COLUMNS; col++) {
                if (board.isValidMove(col)) {
                    board.makeMove(col, !board.isRedTurn);
                    depth = depth-1;
                    int eval = minimax(board, depth, true);
                    minEval = Math.min(minEval, eval);
                    board.undoMove(col);
                    depth = depth+1;
                }
            }
            return minEval;
        }
    }

    public static int findBestMove(Board board, int depth) {
        int bestMove = -1;
        int bestValue = Integer.MIN_VALUE;

        for (int col = 0; col < board.COLUMNS; col++) {
            if (board.isValidMove(col)) {
                board.makeMove(col, board.isRedTurn);
                int moveValue = minimax(board, depth - 1, true);
                board.undoMove(col);

                if (moveValue > bestValue) {
                    bestValue = moveValue;
                    bestMove = col;
                }
            }
        }
        return bestMove;
    }
}