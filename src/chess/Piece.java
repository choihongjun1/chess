package chess;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    String color;
    final String WHITE = "white";
    final int SIZE = 8;
    List<Integer[]> canMoveList = new ArrayList<>();
    ImageIcon icon;
    int moveCount = 0;

    public Piece(String color) {
        this.color = color;
    }

    public abstract void setCanMoveList(Piece[][] board, int row, int col);

    public void addMovesInDirections(Piece[][] board, int[][] directions, int row, int col) {
        for (int[] direction : directions) {
            int currentRow = row;
            int currentCol = col;

            while (true) {
                currentRow += direction[0];
                currentCol += direction[1];

                if (currentRow < 0 || currentRow >= SIZE || currentCol < 0 || currentCol >= SIZE) {
                    break;
                }

                Piece target = board[currentRow][currentCol];
                if (target == null) {
                    canMoveList.add(new Integer[]{currentRow, currentCol});
                } else if (!target.color.equals(this.color)) {
                    canMoveList.add(new Integer[]{currentRow, currentCol});
                    break;
                } else {
                    break;
                }
            }
        }
    }

    public boolean isKingInCheckAfterMove(Piece[][] board, int row, int col, int newRow, int newCol) {
        Piece piece = board[row][col];
        Piece destPiece = board[newRow][newCol];

        board[newRow][newCol] = piece;
        board[row][col] = null;

        boolean isInCheck = isKingInCheck(board, piece.color);

        board[row][col] = piece;
        board[newRow][newCol] = destPiece;

        return isInCheck;
    }

    public boolean isKingInCheck(Piece[][] board, String color) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Piece piece = board[i][j];
                if (piece instanceof King && piece.color.equals(color)) {
                    King king = (King) piece;
                    return king.isSquareUnderAttack(board, i, j);
                }
            }
        }
        return false;
    }

    public void removeIfKingCheck(Piece[][] board, int row, int col) {
        canMoveList.removeIf(move -> {
            int newRow = move[0];
            int newCol = move[1];
            return isKingInCheckAfterMove(board, row, col, newRow, newCol);
        });
    }
}
