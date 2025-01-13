package chess;

import javax.swing.*;

public class Knight extends Piece {

    public Knight(String color) {
        super(color);
        if (color.equals(WHITE)) {
            icon = new ImageIcon("img/whiteKnight.png");
        } else {
            icon = new ImageIcon("img/blackKnight.png");
        }
    }

    @Override
    public void setCanMoveList(Piece[][] board, int row, int col) {
        this.canMoveList.clear();
        int[][] directions = {
                {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
                {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE) {
                if (board[newRow][newCol] == null || !board[newRow][newCol].color.equals(this.color)) {
                    canMoveList.add(new Integer[]{newRow, newCol});
                }
            }
        }
    }
}
