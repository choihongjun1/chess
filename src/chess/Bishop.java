package chess;

import javax.swing.*;

public class Bishop extends Piece {

    public Bishop(String color) {
        super(color);
        if (color.equals(WHITE)) {
            icon = new ImageIcon("img/whiteBishop.png");
        } else {
            icon = new ImageIcon("img/blackBishop.png");
        }
    }

    @Override
    public void setCanMoveList(Piece[][] board, int row, int col) {
        this.canMoveList.clear();
        int[][] directions = {
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        addMovesInDirections(board, directions, row, col);
    }
}
