import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

public class ImagePiece {
    int correctRow; // Correct row of the original image
    int correctCol; // Correct columns of the original image
    int currentRow; // Current row of the puzzle board
    int currentCol; // Current column of the puzzle board
    int rotation; // Rotation angle in degrees
    boolean flipped; // Determines if the piece is flipped or not
    BufferedImage image; // Image of the piece

    // The constructor
    ImagePiece(int correctRow, int correctCol, BufferedImage image) {
        this.correctRow = correctRow;
        this.correctCol = correctCol;
        this.currentRow = correctRow;
        this.currentCol = correctCol;
        this.rotation = 0;
        this.flipped = false;
        this.image = image;
    }

    // The current row position of the piece
    public int getCurrentRow() {
        return currentRow;
    }

    // The current column position of the piece
    public int getCurrentCol() {
        return currentCol;
    }

    // Rotates the puzzle piece to the left, counterclockwise 90 degrees
    public void rotateLeft() {
        rotation = (rotation - 90 + 360) % 360;
    }

    // Rotates the puzzle piece to the right, clockwise 90 degrees
    public void rotateRight() {
        rotation = (rotation + 90) % 360;
    }

    // Flips the piece horizontally
    public void flip() {
        flipped = !flipped;
    }

    // Get the original image of the piece without any transformation to know how the solved piece should look like
    public BufferedImage getImage() {
        if (image == null) {
            throw new IllegalStateException("Image has not yet been loaded in :(");
        }

        // Sets up the origin of the puzzle piece to now let all transformation to the puzzle piece happen in relation to the image's top left corner
        BufferedImage transformedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = transformedImage.createGraphics();
        AffineTransform transform = new AffineTransform();
        transform.translate(image.getWidth() / 2, image.getHeight() / 2);

        // Apply the rotation if needed
        if (rotation != 0) {
            transform.rotate(Math.toRadians(rotation));
        }

        // Apply the flip if needed
        if (flipped) {
            transform.scale(-1, 1);
        }

        // Sets the origin back to the top left corner of the puzzle piece after performing a transformation
        transform.translate(-image.getWidth() / 2, -image.getHeight() / 2);

        // Draw the new transformed image onto the new buffered image
        g2d.drawImage(image, transform, null); // We do not care about the loading state of the image, only copying the image
        g2d.dispose();

        // Return the new transformed image
        return transformedImage;
    }

    // Gets the correct image of the piece without any transformations
    public BufferedImage getCorrectImage() {
        BufferedImage correctImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = correctImage.createGraphics();

        // Draws the image onto the correctImage since no transformations happened
        g2d.drawImage(image, 0, 0, null); // We do not care about the loading state of the image, only copying the image
        g2d.dispose();

        // Return the new correct image
        return correctImage;
    }
}
