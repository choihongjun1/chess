package chess;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    List<Integer[]> kingSideCastlingList = new ArrayList<>();
    List<Integer[]> queenSideCastlingList = new ArrayList<>();

    public King(String color) {
        super(color);
        if (color.equals(WHITE)) {
            icon = new ImageIcon("img/whiteKing.png");
        } else {
            icon = new ImageIcon("img/blackKing.png");
        }
    }

    @Override
    public void setCanMoveList(Piece[][] board, int row, int col) {
        this.canMoveList.clear();
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE) {
                Piece target = board[newRow][newCol];
                if ((target == null || !target.color.equals(this.color)) && !isSquareUnderAttack(board, newRow, newCol)) {
                    canMoveList.add(new Integer[]{newRow, newCol});
                }
            }
        }

        if ((this.moveCount == 0) && (!isKingInCheck(board, this.color))) {
            // 킹사이드 캐슬링
            if ((board[row][col + 1] == null) && (board[row][col + 2] == null)) {
                if ((!isSquareUnderAttack(board, row, col + 1)) && (!isSquareUnderAttack(board, row, col + 2))) {
                    if ((board[row][col + 3] instanceof Rook) && (board[row][col + 3].moveCount == 0)) {
                        canMoveList.add(new Integer[]{row, col + 2});
                        kingSideCastlingList.add(new Integer[]{row, col + 2});
                    }
                }
            }
            // 퀸사이드 캐슬링
            if ((board[row][col - 1] == null) && (board[row][col - 2] == null) && (board[row][col - 3] == null)) {
                if ((!isSquareUnderAttack(board, row, col - 1)) && (!isSquareUnderAttack(board, row, col - 2))) {
                    if ((board[row][col - 4] instanceof Rook) && (board[row][col - 4].moveCount == 0)) {
                        canMoveList.add(new Integer[]{row, col - 2});
                        queenSideCastlingList.add(new Integer[]{row, col - 2});
                    }
                }
            }
        }

    }

    // 공격 받는 위치인지 확인
    public boolean isSquareUnderAttack(Piece[][] board, int row, int col) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Piece piece = board[i][j];
                if (piece != null && !piece.color.equals(this.color)) {
                    if (piece instanceof King) {
                        if (Math.abs(i - row) <= 1 && Math.abs(j - col) <= 1) {
                            return true;
                        }
                    } else if (piece instanceof Pawn) {
                        Pawn pawn = (Pawn) piece;
                        pawn.setCanMoveList(board, i, j);
                        if (pawn.diagCanMoveList.stream().anyMatch(move -> move[0] == row && move[1] == col)) {
                            return true;
                        }
                    } else {
                        piece.setCanMoveList(board, i, j);
                        if (piece.canMoveList.stream().anyMatch(move -> move[0] == row && move[1] == col)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
