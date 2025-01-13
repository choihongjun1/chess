package chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MainFrame extends JFrame {
    Container frame = getContentPane();
    JPanel menuPanel, gamePanel, helpPanel;
    JPanel northPanel, centerPanel, southPanel;
    CardLayout cardLayout = new CardLayout();
    JLabel northLabel, southLabel;
    JLabel boardLabel;
    JButton menuButton;
    Dimension buttonSize = new Dimension(150, 50);
    ImageIcon chessBoard = new ImageIcon("img/chessBoard.png");
    final String WHITE = "white";
    final int SIZE = 8;
    final int BOARD_WIDTH = chessBoard.getIconWidth();
    final int BOARD_HEIGHT = chessBoard.getIconHeight();
    final int TILE_SIZE_X = BOARD_WIDTH / SIZE;
    final int TILE_SIZE_Y = BOARD_HEIGHT / SIZE;
    private ArrayList<JLabel> highlightList = new ArrayList<>();
    private List<Integer[]> canMoveList = new ArrayList<>();
    private List<Point> piecePoint = new ArrayList<>();
    private List<Piece> movedPieces = new ArrayList<>();
    private boolean isPromotionActive = false;

    public MainFrame(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        initLayout();
        setVisible(true);
    }

    private void initLayout() {
        initNorthPanel();
        initMenuPanel();
        initHelpPanel();
        initCenterPanel();
        initSouthPanel();
    }

    private void initMenuPanel() {
        menuPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(30, 0, 30, 0);

        JButton playButton = new JButton("Play");
        playButton.setPreferredSize(buttonSize);
        playButton.addActionListener(e -> {
            menuButton.setVisible(true);
            play();
        });
        menuPanel.add(playButton, gbc);

        JButton helpButton = new JButton("Help");
        helpButton.setPreferredSize(buttonSize);
        helpButton.addActionListener(e -> {
            menuButton.setVisible(true);
            cardLayout.show(centerPanel, "help");
        });
        menuPanel.add(helpButton, gbc);

        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(buttonSize);
        exitButton.addActionListener(e -> System.exit(0));
        menuPanel.add(exitButton, gbc);
    }

    private void initHelpPanel() {
        helpPanel = new JPanel(new BorderLayout());

        JTextArea multiLineLabel = new JTextArea(
                "Welcome to the Chess Game!\n" +
                        "Enjoy playing with your friends.\n\n" +

                        "Piece Movement\n" +
                        "- Pawn: Moves one square forward, but captures diagonally. On its first move, it can move two squares forward.\n" +
                        "- Rook: Moves any number of squares along rows or columns.\n" +
                        "- Knight: Moves in an 'L' shape. It moves two squares in one direction, then one square perpendicular.\n" +
                        "- Bishop: Moves any number of squares diagonally.\n" +
                        "- Queen: Combines the movement of the Rook and Bishop, moving any number of squares along rows, columns, or diagonals.\n" +
                        "- King: Moves one square in any direction.\n\n" +

                        "Special Rules\n" +
                        "1. Castling:\n" +
                        "   - The King moves two squares toward a Rook, and the Rook moves to the square next to the King.\n" +
                        "   - Castling is only allowed if neither the King nor the Rook has moved before, and there are no pieces between them.\n" +
                        "   - The King cannot be in check, nor can the squares the King passes through or moves to be under attack.\n\n" +

                        "2. En Passant:\n" +
                        "   - If a Pawn moves two squares forward from its starting position and lands next to an opponent's Pawn,\n" +
                        "     the opponent can capture it as if it had only moved one square.\n" +
                        "   - En Passant can only occur immediately after the Pawn moves two squares forward.\n\n" +

                        "3. Promotion:\n" +
                        "   - When a Pawn reaches the opponent's back rank, it may be promoted to a Queen, Rook, Bishop, or Knight.\n" +
                        "   - While the Queen is the most common promotion, you may choose any of the other pieces.\n\n" +

                        "Check and Checkmate\n" +
                        "1. Check:\n" +
                        "   - A King is in check when it is under attack by an opponent's piece.\n" +
                        "   - When in check, the player must move the King, block the check, or capture the attacking piece to protect the King.\n\n" +

                        "2. Checkmate:\n" +
                        "   - Checkmate occurs when the King is in check and there is no legal move to escape the check.\n" +
                        "   - When checkmate happens, the game ends and the player whose King is checkmated loses.\n\n" +

                        "3. Stalemate:\n" +
                        "   - A Stalemate occurs when the player whose turn it is has no legal move left, but the King is not in check.\n" +
                        "   - In this case, the game ends in a draw.\n\n" +

                        "Enjoy the game and good luck!"
        );
        multiLineLabel.setEditable(false);
        multiLineLabel.setOpaque(false);
        multiLineLabel.setFont(new Font("Serif", Font.BOLD, 20));
        helpPanel.add(multiLineLabel);

        JScrollPane scrollPane = new JScrollPane(multiLineLabel);

        helpPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void initNorthPanel() {
        northPanel = new JPanel(new BorderLayout());

        menuButton = new JButton("Menu");
        menuButton.setVisible(false);
        menuButton.addActionListener(e -> {
            cardLayout.show(centerPanel, "menu");
            southLabel.setVisible(false);
            southLabel.setText(WHITE + "'s turn");
            menuButton.setVisible(false);
        });
        northPanel.add(menuButton, BorderLayout.EAST);

        northLabel = new JLabel("Chess Game", SwingConstants.CENTER);
        northLabel.setFont(new Font("Serif", Font.BOLD, 50));
        northPanel.add(northLabel, BorderLayout.CENTER);

        frame.add(northPanel, BorderLayout.NORTH);
    }

    private void initCenterPanel() {
        centerPanel = new JPanel(cardLayout);

        centerPanel.add(menuPanel, "menu");
        centerPanel.add(helpPanel, "help");

        cardLayout.show(centerPanel, "menu");

        frame.add(centerPanel, BorderLayout.CENTER);
    }

    private void initSouthPanel() {
        southPanel = new JPanel(new BorderLayout());

        southLabel = new JLabel(WHITE + "'s turn", SwingConstants.CENTER);
        southLabel.setFont(new Font("Serif", Font.BOLD, 50));
        southLabel.setVisible(false);
        southPanel.add(southLabel, BorderLayout.CENTER);

        frame.add(southPanel, BorderLayout.SOUTH);
    }

    private void play() {
        gamePanel = new JPanel(null);

        southLabel.setVisible(true);

        Game game = new Game();

        int boardX = (getWidth() - BOARD_WIDTH) / 2; // 200
        int boardY = (getHeight() - 180 - BOARD_HEIGHT) / 2; // 10

        boardLabel = new JLabel();
        boardLabel.setIcon(chessBoard);
        boardLabel.setBounds(boardX, boardY, BOARD_WIDTH, BOARD_HEIGHT);
        boardLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isPromotionActive) {
                    return;
                }

                Point point = getIndex(e.getX(), e.getY());

                if ((point != null) && (!piecePoint.isEmpty()) && (!highlightList.isEmpty()) && (isMoveAvailable(point, canMoveList))) {
                    Piece currentPiece = game.board[piecePoint.getLast().x][piecePoint.getLast().y];

                    if ((currentPiece instanceof King)) {
                        King king = (King) currentPiece;
                        if ((!king.kingSideCastlingList.isEmpty()) && (isMoveAvailable(point, king.kingSideCastlingList))) { // 킹사이드 캐슬링
                            game.board[point.x][point.y - 1] = game.board[point.x][point.y + 1];
                            game.board[point.x][point.y + 1] = null;
                            game.board[point.x][point.y - 1].moveCount++;
                        } else if ((!king.queenSideCastlingList.isEmpty()) && (isMoveAvailable(point, king.queenSideCastlingList))) { // 퀸사이드 캐슬링
                            game.board[point.x][point.y + 1] = game.board[point.x][point.y - 2];
                            game.board[point.x][point.y - 2] = null;
                            game.board[point.x][point.y + 1].moveCount++;
                        }
                    } else if ((currentPiece instanceof Pawn)) { // 앙파상
                        Pawn pawn = (Pawn) currentPiece;
                        if ((!pawn.enPassantList.isEmpty()) && (isMoveAvailable(point, pawn.enPassantList))) {
                            int direction = pawn.color.equals(WHITE) ? 1 : -1;
                            game.board[point.x + direction][point.y] = null;
                            pawn.enPassantList.clear();
                        }
                    }

                    // 기물 이동
                    game.board[point.x][point.y] = currentPiece;
                    game.board[piecePoint.getLast().x][piecePoint.getLast().y] = null;
                    game.board[point.x][point.y].moveCount++;

                    // 프로모션
                    if (currentPiece instanceof Pawn) {
                        int promotionRow = currentPiece.color.equals(WHITE) ? 0 : 7;
                        if (point.x == promotionRow) {
                            initPromotionPanel(game.board, currentPiece, point.x, point.y);
                        }
                    }

                    movedPieces.add(game.board[point.x][point.y]);
                    piecePoint.clear();
                    clearHighlights();
                    updateBoardIcons(game.board);

                    if (game.isCheckmate(game.players[(game.turn + 1) % 2])) { // 체크메이트
                        southLabel.setVisible(false);
                        menuButton.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Checkmate!\n" + game.players[game.turn % 2] + " Wins!");
                        cardLayout.show(centerPanel, "menu");
                    } else if (game.isStalemate(game.players[(game.turn + 1) % 2])) { // 스테일메이트
                        southLabel.setVisible(false);
                        menuButton.setVisible(false);
                        JOptionPane.showMessageDialog(null, "StaleMate!\nDraw!");
                        cardLayout.show(centerPanel, "menu");
                    } else if (game.isDraw()) { // 기물 부족 무승부
                        southLabel.setVisible(false);
                        menuButton.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Draw!");
                        cardLayout.show(centerPanel, "menu");
                    }

                    game.turn++;

                    southLabel.setText(game.players[game.turn % 2] + "'s turn");
                } else {
                    if (point != null) {
                        clearHighlights();

                        Piece target = game.board[point.x][point.y];

                        // 현재 플레이어의 기물인지 확인
                        if (target != null && target.color.equals(game.players[game.turn % 2])) {
                            piecePoint.add(new Point(point.x, point.y));

                            target.setCanMoveList(game.board, point.x, point.y);
                            target.removeIfKingCheck(game.board, point.x, point.y);

                            // 앙파상 조건 확인
                            if (target instanceof Pawn) {
                                Pawn pawn = (Pawn) target;
                                if (!pawn.enPassantList.isEmpty()) {
                                    int direction = pawn.color.equals(WHITE) ? 1 : -1;
                                    Iterator<Integer[]> enPassantIterator = pawn.enPassantList.iterator();
                                    while (enPassantIterator.hasNext()) {
                                        Integer[] enPassantPoint = enPassantIterator.next();
                                        if (!movedPieces.getLast().equals(game.board[enPassantPoint[0] + direction][enPassantPoint[1]])) {
                                            Iterator<Integer[]> moveIterator = pawn.canMoveList.iterator();
                                            while (moveIterator.hasNext()) {
                                                Integer[] move = moveIterator.next();
                                                if (Arrays.equals(move, enPassantPoint)) {
                                                    moveIterator.remove();
                                                    enPassantIterator.remove();
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // 이동 가능 칸 표시
                            if (!target.canMoveList.isEmpty()) {
                                highlightMoves(target.canMoveList);
                            }
                        }

                    }
                }

            }
        });
        gamePanel.add(boardLabel);

        updateBoardIcons(game.board);

        centerPanel.add(gamePanel, "game");
        cardLayout.show(centerPanel, "game");
    }

    private void initPromotionPanel(Piece[][] board, Piece currentPiece, int row, int col) {
        isPromotionActive = true;

        Queen queen = new Queen(currentPiece.color);
        Rook rook = new Rook(currentPiece.color);
        Bishop bishop = new Bishop(currentPiece.color);
        Knight knight = new Knight(currentPiece.color);

        JRadioButton queenButton = new JRadioButton(queen.icon);
        JRadioButton rookButton = new JRadioButton(rook.icon);
        JRadioButton bishopButton = new JRadioButton(bishop.icon);
        JRadioButton knightButton = new JRadioButton(knight.icon);

        JPanel promotionPanel = new JPanel();
        promotionPanel.add(queenButton);
        promotionPanel.add(rookButton);
        promotionPanel.add(bishopButton);
        promotionPanel.add(knightButton);
        promotionPanel.setBackground(Color.GREEN);
        promotionPanel.setSize(250, 60);
        promotionPanel.setLocation((gamePanel.getWidth() - promotionPanel.getWidth()) / 2, (gamePanel.getHeight() - promotionPanel.getHeight()) / 2);

        queenButton.addActionListener(ae -> {
            board[row][col] = new Queen(currentPiece.color);
            gamePanel.remove(promotionPanel);
            updateBoardIcons(board);
            isPromotionActive = false;
        });
        rookButton.addActionListener(ae -> {
            board[row][col] = new Rook(currentPiece.color);
            gamePanel.remove(promotionPanel);
            updateBoardIcons(board);
            isPromotionActive = false;
        });
        bishopButton.addActionListener(ae -> {
            board[row][col] = new Bishop(currentPiece.color);
            gamePanel.remove(promotionPanel);
            updateBoardIcons(board);
            isPromotionActive = false;
        });
        knightButton.addActionListener(ae -> {
            board[row][col] = new Knight(currentPiece.color);
            gamePanel.remove(promotionPanel);
            updateBoardIcons(board);
            isPromotionActive = false;
        });

        ButtonGroup promotionGroup = new ButtonGroup();
        promotionGroup.add(queenButton);
        promotionGroup.add(rookButton);
        promotionGroup.add(bishopButton);
        promotionGroup.add(knightButton);

        gamePanel.add(promotionPanel);
    }

    private Point getIndex(int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseX < BOARD_WIDTH && mouseY >= 0 && mouseY < BOARD_HEIGHT) {
            int col = mouseX / TILE_SIZE_X;
            int row = mouseY / TILE_SIZE_Y;
            return new Point(row, col);
        }
        return null;
    }

    // 이동 가능 칸인지 확인
    private boolean isMoveAvailable(Point point, List<Integer[]> list) {
        for (Integer[] move : list) {
            if (Arrays.equals(move, new Integer[]{point.x, point.y})) {
                return true;
            }
        }
        return false;
    }

    // 화면 기물 정보 업데이트
    private void updateBoardIcons(Piece[][] board) {
        Component[] components = gamePanel.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel && component != boardLabel) {
                gamePanel.remove(component);
            }
        }

        gamePanel.setLayout(null);

        int boardX = boardLabel.getX(); // 200
        int boardY = boardLabel.getY(); // 10

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.icon != null) {
                    JLabel pieceLabel = new JLabel(piece.icon);
                    int x = boardX + col * TILE_SIZE_X;
                    int y = boardY + row * TILE_SIZE_Y;
                    pieceLabel.setBounds(x, y, TILE_SIZE_X, TILE_SIZE_Y);
                    gamePanel.add(pieceLabel);
                }
            }
        }

        gamePanel.revalidate();
        gamePanel.repaint();
        gamePanel.setComponentZOrder(boardLabel, gamePanel.getComponentCount() - 1);
    }

    // 이동 가능 칸 표시
    private void highlightMoves(List<Integer[]> canMoveList) {
        this.canMoveList = canMoveList;

        int boardX = boardLabel.getX();
        int boardY = boardLabel.getY();

        for (Integer[] move : canMoveList) {
            int row = move[0];
            int col = move[1];
            int x = boardX + col * TILE_SIZE_X;
            int y = boardY + row * TILE_SIZE_Y;

            // 초록색 표시 추가
            JLabel highlightLabel = new JLabel();
            highlightLabel.setOpaque(false);
            highlightLabel.setBackground(new Color(0, 255, 0, 100));
            highlightLabel.setBounds(x, y, TILE_SIZE_X, TILE_SIZE_Y);
            highlightLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));

            highlightList.add(highlightLabel);

            gamePanel.add(highlightLabel);

            gamePanel.setComponentZOrder(boardLabel, gamePanel.getComponentCount() - 1);

        }

        gamePanel.revalidate();
        gamePanel.repaint();
    }

    // 이동 가능 칸 표시 제거
    private void clearHighlights() {
        for (JLabel highlightLabel : highlightList) {
            gamePanel.remove(highlightLabel);
        }

        highlightList.clear();

        gamePanel.revalidate();
        gamePanel.repaint();
    }

}