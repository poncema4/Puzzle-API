import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class PuzzleGUI extends JPanel {
    PuzzleBoard board; // The puzzle board that has all the pieces
    int boxSize; // The size of each individual puzzle piece
    int selectedRow = -1; // X Coordinate of the current selected piece (-1 no piece chosen)
    int selectedCol = -1; // Y Coordinate of the current selected piece (-1 no piece chosen)
    int startTime; // Start timer of the puzzle game
    int endTime; // End timer of the puzzle game when solved
    boolean isSolved; // Determines if the puzzle is solved
    int totalSecond; // Time passed by since the puzzle started in seconds
    Timer timer; // Increments every second
    String imageUrl; // URL of the image generated for the puzzle
    BufferedImage solvedImage; // Represents the solved image used for comparison

    // The constructor
    PuzzleGUI(PuzzleBoard board, String imageUrl) {
        // Initialize both the board and image URL
        this.board = board;
        this.imageUrl = imageUrl;
        // Initialize the starting time and puzzle state
        this.startTime = (int) System.currentTimeMillis();
        this.isSolved = false;
        this.totalSecond = 0;
        // Recognize the solved image for the comparison
        this.solvedImage = loadImage(imageUrl);
        // Determines the size of each puzzle piece
        this.boxSize = 500 / Math.max(board.getCols(), board.getRows());
        // Bottom of puzzle timer
        setPreferredSize(new Dimension(board.getCols() * boxSize, board.getRows() * boxSize + 50));
        setBackground(Color.WHITE);

        // Increases every second
        timer = new Timer(1000, e -> {
            totalSecond++; // totalSecond = totalSecond + 1
            checkSolved(); // Checks every second if it is solved
            repaint(); // Updates the timer panel display
        });
        timer.start();

        // Mouse clicks -> piece selection and swapping
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isSolved) return; // Ignore clicks if the puzzle is solved

                // Calculate which column and row the mouse click happened in
                int col = e.getX() / boxSize;
                int row = e.getY() / boxSize;

                // If no piece is selected, select the clicked piece, remembers what piece was selected first
                if (selectedRow == -1 && selectedCol == -1) {
                    selectedRow = row;
                    selectedCol = col;
                } else {
                    // If a piece is already selected, swap pieces with the new clicked piece
                    board.swapPieces(selectedRow, selectedCol, row, col);
                    selectedRow = -1;
                    selectedCol = -1;
                    repaint();
                }
                checkSolved(); // Checks if the puzzle is solved after a swap
            }
        });

        // Key presses -> Rotations and flipping selected pieces
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isSolved) return; // Ignores key presses once the puzzle is solved

                // If a valid piece position is selected, get the row and column it was selected from
                if (selectedRow != -1 && selectedCol != -1) {
                    ImagePiece selectedPiece = board.getPieceAt(selectedRow, selectedCol);

                    // Cases for each key press
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_L:
                            selectedPiece.rotateLeft();
                            break;
                        case KeyEvent.VK_R:
                            selectedPiece.rotateRight();
                            break;
                        case KeyEvent.VK_F:
                            selectedPiece.flip();
                            break;
                    }
                    repaint();
                }
                checkSolved(); // Checks if the puzzle is solved after a rotated or flipped piece
            }
        });
        setFocusable(true); // Can receive keyboard inputs from user
        requestFocusInWindow(); // Ensures keyboard inputs respond appropriately
    }

    // Load the image generate from the URL
    public BufferedImage loadImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl); // New URL holding generated URL value
            return ImageIO.read(url); // Return image using the URL
        } catch (IOException e) {
            e.printStackTrace(); // Prints exception case
            return emptyImage(); // Default image if it fails
        }
    }

    // Set back emptyImage if the loadImage method fails, to avoid using null
    public BufferedImage emptyImage() {
        int width = 100, height = 100;
        BufferedImage fallbackImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = fallbackImage.createGraphics();
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.RED);
        g2.drawString("Image Not Found", 10, 50);
        g2.dispose();
        return fallbackImage;
    }

    // Checks if the puzzle is solved -> correct piece position and rotation and flipped correct
    public void checkSolved() {
        if (solvedImage != null && board.isSolved()) {
            // Goes through each board piece and checks if the rotation of it is the same as the generated image
            for (ImagePiece piece : board.getPieces()) {
                BufferedImage pieceImage = piece.getImage();
                BufferedImage correctPieceImage = piece.getCorrectImage(); // Correct image piece comparison
                if (pieceImage != null && !compareImages(pieceImage, correctPieceImage)) {
                    return; // If the pieces do not match, puzzle is not solved yet
                }
            }
            // If puzzle is solved, end the timer, pop a JPanel message telling the user the puzzle is solved with how long it took
            isSolved = true;
            endTime = (int) System.currentTimeMillis();
            timer.stop();
            JOptionPane.showMessageDialog(this, "Puzzle is solved in " + totalSecond + " seconds!");
            repaint();
        }
    }

    // Compares two images pixel by pixel from the generate image for rotation
    public boolean compareImages(BufferedImage image1, BufferedImage image2) {
        for (int y = 0; y < image1.getHeight(); y++) { // Y coordinate
            for (int x = 0; x < image1.getWidth(); x++) { // X coordinate
                if (image1.getRGB(x, y) != image2.getRGB(x, y)) {
                    return false; // If they do not match, images not identical
                }
            }
        }
        return true; // If they do match, images are identical
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < board.getRows(); row++) { // Each row
            for (int col = 0; col < board.getCols(); col++) { // Each column
                ImagePiece piece = board.getPieceAt(row, col); // Specific image piece at a certain coordinate
                drawPiece(g, piece, col * boxSize, row * boxSize); // Draws the piece that goes in that spot
            }
        }

        // Timer portion on the bottom of the puzzle game for the user to see how much time has passed by
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.BLACK);
        g.drawString("Time: " + totalSecond + " seconds", getWidth() / 2 - 75, board.getRows() * boxSize + 30);
    }

    //  Draws the piece of each generated puzzle piece
    public void drawPiece(Graphics g, ImagePiece piece, int x, int y) {
        if (piece.getImage() != null) {
            g.drawImage(piece.getImage(), x, y, boxSize, boxSize, null); // We do not care about the loading state of the image, only copying the image
        } else {
            g.setColor(Color.BLACK);
            g.drawRect(x, y, boxSize, boxSize);
        }
    }
}
