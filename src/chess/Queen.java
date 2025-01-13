package chess;

import javax.swing.*;

public class Queen extends Piece {

    public Queen(String color) {
        super(color);
        if (color.equals(WHITE)) {
            icon = new ImageIcon("img/whiteQueen.png");
        } else {
            icon = new ImageIcon("img/blackQueen.png");
        }
    }

    @Override
    public void setCanMoveList(Piece[][] board, int row, int col) {
        this.canMoveList.clear();
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        addMovesInDirections(board, directions, row, col);
    }
}
