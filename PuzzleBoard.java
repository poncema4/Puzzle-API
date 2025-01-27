import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PuzzleBoard {
    int rows; // # of rows
    int cols; // # of columns
    List<ImagePiece> pieces; // List containing all the puzzle pieces
    BufferedImage solvedImage; // Full image that is created for the puzzle game

    // The constructor
    PuzzleBoard(int rows, int cols, String imageUrl) {
        this.rows = rows;
        this.cols = cols;
        this.pieces = new ArrayList<>();

        // Load the image from the given URL
        BufferedImage image = loadImage(imageUrl);
        int pieceWidth = image.getWidth() / cols;
        int pieceHeight = image.getHeight() / rows;

        // Dividing the full original image into smaller puzzle pieces and add them to the (list-of pieces)
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                BufferedImage pieceImage = image.getSubimage(col * pieceWidth, row * pieceHeight, pieceWidth, pieceHeight);
                ImagePiece piece = new ImagePiece(row, col, pieceImage);
                pieces.add(piece); // Now each puzzle piece represents a Sub-image of the original
            }
        }
        // Rearranges the pieces in the puzzle before the puzzle game starts
        shuffleAndTransformPieces();
        this.solvedImage = image; // Stores the original puzzle image
    }

    // Method that returns the number of rows in a puzzle
    public int getRows() {
        return rows;
    }

    // Method that returns the number of columns in a puzzle
    public int getCols() {
        return cols;
    }

    // Computes the puzzle piece that is set a specific coordinates -> specific row and column
    public ImagePiece getPieceAt(int row, int col) {
        return pieces.get(row * cols + col);
    }

    // Swaps two pieces from specific row and column to another row and column -> can be considered coordinates
    public void swapPieces(int row1, int col1, int row2, int col2) {
        int index1 = row1 * cols + col1;
        int index2 = row2 * cols + col2;
        Collections.swap(pieces, index1, index2);
    }

    // Checks if the puzzle is solved checking that the puzzle pieces are in their correct positions and place
    public boolean isSolved() {
        // Compare each piece's current position with the correct position based on the original image
        for (int i = 0; i < pieces.size(); i++) {
            ImagePiece piece = pieces.get(i); // Gets piece from index "i"
            int correctRow = i / cols;
            int correctCol = i % cols;
            if (piece.getCurrentRow() != correctRow || piece.getCurrentCol() != correctCol) {
                return false;
            }
        }
        return true;
    }

    // Returns the list of all the pieces that are in the puzzle
    public List<ImagePiece> getPieces() {
        return pieces;
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

    // Shuffles the pieces for the puzzle game applying random transformation -> rotate L, rotate R, flip
    public void shuffleAndTransformPieces() {
        Collections.shuffle(pieces);
        // For each piece that is a ImagePiece in a list of pieces apply, the random transformation before the puzzle game starts to shuffle the board
        for (ImagePiece piece : pieces) {
            if (Math.random() > 0.33) { // 33% chance for this to happen
                piece.rotateLeft();
            }
            if (Math.random() > 0.33) { // 33% chance for this to happen
                piece.rotateRight();
            }
            if (Math.random() > 0.33) { // 33% chance for this to happen
                piece.flip();
            }
        }
    }
}
