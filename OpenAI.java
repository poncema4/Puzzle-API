import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenAI {
    // Get URL and the KEY variables from the environment
    static String OPENAI_API_URL = System.getenv("OPENAI_API_URL");
    static String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");

    // Fetches image URL from OpenAI's API based on the user's prompt
    public static String fetchImage(String prompt) {
        try {
            if (OPENAI_API_URL == null) {
                throw new IllegalStateException("API URL is not set.");
            }
            if (OPENAI_API_KEY == null) {
                throw new IllegalStateException("API KEY is not set.");
            }
            // Sets up the HTTP connection to the OpenAI API
            URL url = new URL(OPENAI_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + OPENAI_API_KEY);
            connection.setDoOutput(true);

            // JSON payload with the image generation info
            JSONObject payload = new JSONObject();
            payload.put("model", "dall-e-3");
            payload.put("prompt", prompt);
            payload.put("n", 1);
            payload.put("size", "1024x1024");

            // Sends the JSON payload
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.toString().getBytes("utf-8"); // String -> bytes (for communication)
                os.write(input, 0, input.length);
            }

            // Reads the response from the server
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            // Parses the JSON response to get the URL of the generated image
            JSONObject jsonResponse = new JSONObject(response.toString());
            String imageUrl = jsonResponse.getJSONArray("data").getJSONObject(0).getString("url");

            return imageUrl;

        } catch (Exception e) {
            e.printStackTrace();
            return "Could not find the generate image URL :(";
        }
    }
}
