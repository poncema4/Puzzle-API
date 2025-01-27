import javax.swing.*;

public class PuzzleGame {
    public static void main(String[] args) {
        try {
            // Creates a JOptionPane for the user to input the prompt to generate the image
            String prompt = JOptionPane.showInputDialog("Enter a prompt for the image: ");
            if (prompt == null || prompt.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Prompt cannot be empty!");
                return;
            }

            // Prompt the user to set up their desired dimensions for their puzzle
            String rowsInput = JOptionPane.showInputDialog("Enter number of rows for the puzzle: ");
            String colsInput = JOptionPane.showInputDialog("Enter number of columns for the puzzle: ");

            int rows; // Stores the number of rows
            int cols; // Stores the number of columns

            try {
                rows = Integer.parseInt(rowsInput); // Parses users input to an integer -> rows
                cols = Integer.parseInt(colsInput); // Parses users input to an integer -> columns
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Not a valid input, please try again!"); // If they are not integers
                return;
            }

            // Checks if the inputted integers are greater than 0
            if (rows <= 0 || cols <= 0) {
                JOptionPane.showMessageDialog(null, "Rows and columns must be positive integers!");
                return;
            }

            // Fetch the image URL from AzureAI class
            String imageUrl = OpenAI.fetchImage(prompt);

            // Invalid URL link
            if (imageUrl == null) {
                JOptionPane.showMessageDialog(null, "Not able to generate image, please try again!");
                return;
            }

            // For the user to see the generate URL, be able to see how the puzzle should look like if needed
            System.out.println("Generated Image URL: " + imageUrl);

            // Sets up the GUI for the puzzle game
            PuzzleBoard board = new PuzzleBoard(rows, cols, imageUrl);
            PuzzleGUI gui = new PuzzleGUI(board, imageUrl);
            JFrame frame = new JFrame("Puzzle Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(gui);
            frame.pack();
            frame.setVisible(true);
            frame.setResizable(false);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage()); // Prints the error if any error occurs
        }
    }
}
