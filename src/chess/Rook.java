package chess;

import javax.swing.*;

public class Rook extends Piece {

    public Rook(String color) {
        super(color);
        if (color.equals(WHITE)) {
            icon = new ImageIcon("img/whiteRook.png");
        } else {
            icon = new ImageIcon("img/blackRook.png");
        }
    }

    @Override
    public void setCanMoveList(Piece[][] board, int row, int col) {
        this.canMoveList.clear();
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        addMovesInDirections(board, directions, row, col);
    }
}
