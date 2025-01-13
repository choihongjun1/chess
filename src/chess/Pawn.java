package chess;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    List<Integer[]> diagCanMoveList = new ArrayList<>();
    List<Integer[]> enPassantList = new ArrayList<>();

    public Pawn(String color) {
        super(color);
        if (color.equals(WHITE)) {
            icon = new ImageIcon("img/whitePawn.png");
        } else {
            icon = new ImageIcon("img/blackPawn.png");
        }
    }

    @Override
    public void setCanMoveList(Piece[][] board, int row, int col) {
        this.canMoveList.clear();
        this.diagCanMoveList.clear();
        this.enPassantList.clear();

        int direction = this.color.equals(WHITE) ? -1 : 1;

        if (row + direction >= 0 && row + direction < SIZE) {
            if (board[row + direction][col] == null) {
                canMoveList.add(new Integer[]{row + direction, col});
                if ((row == 6 && direction == -1) || (row == 1 && direction == 1)) {
                    if (board[row + 2 * direction][col] == null) {
                        canMoveList.add(new Integer[]{row + 2 * direction, col});
                    }
                }
            }

            for (int diagCol : new int[]{col - 1, col + 1}) {
                if (diagCol >= 0 && diagCol < SIZE) {
                    Piece target = board[row + direction][diagCol];
                    if (target != null && !target.color.equals(this.color)) {
                        canMoveList.add(new Integer[]{row + direction, diagCol});
                        diagCanMoveList.add(new Integer[]{row + direction, diagCol});
                    }
                }
            }
        }

        // 앙파상
        int enPassantRow = this.color.equals(WHITE) ? 3 : 4;

        if (row == enPassantRow) {
            for (int diagCol : new int[]{col - 1, col + 1}) {
                if (diagCol >= 0 && diagCol < SIZE) {
                    Piece target = board[row][diagCol];
                    if ((target instanceof Pawn) && (!target.color.equals(this.color))) {
                        canMoveList.add(new Integer[]{row + direction, diagCol});
                        enPassantList.add(new Integer[]{row + direction, diagCol});
                    }
                }
            }
        }

    }
}
