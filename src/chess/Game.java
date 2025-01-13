package chess;

import java.util.ArrayList;
import java.util.List;

public class Game {
    final String WHITE = "white";
    final String BLACK = "black";
    final int SIZE = 8;
    Piece[][] board = new Piece[SIZE][SIZE];
    String[] players = {WHITE, BLACK};
    int turn = 0;

    public Game() {
        for (int i = 0; i < SIZE; i++) {
            board[1][i] = new Pawn(BLACK);
            board[6][i] = new Pawn(WHITE);
        }

        board[0][0] = new Rook(BLACK);
        board[0][7] = new Rook(BLACK);
        board[7][0] = new Rook(WHITE);
        board[7][7] = new Rook(WHITE);

        board[0][1] = new Knight(BLACK);
        board[0][6] = new Knight(BLACK);
        board[7][1] = new Knight(WHITE);
        board[7][6] = new Knight(WHITE);

        board[0][2] = new Bishop(BLACK);
        board[0][5] = new Bishop(BLACK);
        board[7][2] = new Bishop(WHITE);
        board[7][5] = new Bishop(WHITE);

        board[0][3] = new Queen(BLACK);
        board[7][3] = new Queen(WHITE);

        board[0][4] = new King(BLACK);
        board[7][4] = new King(WHITE);
    }

    // 체크메이트
    public boolean isCheckmate(String color) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Piece piece = board[i][j];
                if (piece instanceof King && piece.color.equals(color)) {
                    King king = (King) piece;
                    if (!king.isKingInCheck(board, color)) {
                        return false;
                    }
                }
            }
        }

        // 같은 색 모든 기물 탐색
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Piece piece = board[i][j];
                if (piece != null && piece.color.equals(color)) {
                    piece.setCanMoveList(board, i, j);
                    piece.removeIfKingCheck(board, i, j);
                    if (!piece.canMoveList.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // 스테일메이트
    public boolean isStalemate(String color) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Piece piece = board[i][j];
                if (piece instanceof King && piece.color.equals(color)) {
                    King king = (King) piece;
                    if (king.isKingInCheck(board, color)) {
                        return false;
                    }
                }
            }
        }

        // 같은 색 모든 기물 탐색
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Piece piece = board[i][j];
                if (piece != null && piece.color.equals(color)) {
                    piece.setCanMoveList(board, i, j);
                    piece.removeIfKingCheck(board, i, j);
                    if (!piece.canMoveList.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    // 기물부족 무승부
    public boolean isDraw() {
        List<Piece> pieces = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != null) {
                    pieces.add(board[i][j]);
                }
            }
        }

        if(pieces.size() == 3) {
            long knightCount = pieces.stream().filter(p -> p instanceof Knight).count();
            long bishopCount = pieces.stream().filter(p -> p instanceof Bishop).count();

            return (knightCount == 1) || (bishopCount == 1);
        } else if (pieces.size() == 2) {
            return true;
        }

        return false;
    }

}
