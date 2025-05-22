import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MafiaScenarioGenerator {
    private static String API_KEY; 
    private static String API_URL;

    public static String getScenario() throws Exception {
        StringBuilder response = getRAWScenario();
        String rawJson = response.toString();
        String textMarker = "\"text\": \"";
        int textStart = rawJson.indexOf(textMarker);
        if (textStart != -1) {
            textStart += textMarker.length();
            int textEnd = rawJson.indexOf("\"}],", textStart); // assumes "text" ends before end of part
            if (textEnd != -1) {
                String extracted = rawJson.substring(textStart, textEnd).trim();
                // Remove wrapping quotes and unescape escaped characters
                if (extracted.startsWith("\"") && extracted.endsWith("\"")) {
                    extracted = extracted.substring(1, extracted.length() - 1);
                }
                extracted = extracted.replace("\\n", "\n").replace("\\\"", "\"");

                System.out.println("\nPlain Text Output:\n" + extracted);
                return extracted;
            } else {
                System.out.println("Could not find end of text.");
            }
        } else {
            System.out.println("Text field not found in response.");
        }
        return null;
    }

    public static StringBuilder getRAWScenario() throws Exception {
        keyFromDrive();  // Load API key from environment variable
        try {
            // Prompt for Gemini
            String prompt = """
                Write a short but dramatic mafia-style story about the following events:
                - The Mafia tried to kill Alice.
                - Alice was saved by the Doctor.
                - Bob, a Villager, was killed in his sleep.
                Make it sound mysterious and entertaining, but avoid using dialogue. Act as a narrator.
                """;

            // Gemini JSON input structure
            String jsonInput = "{"
                    + "\"contents\": ["
                    + "  {\"parts\": ["
                    + "    {\"text\": " + toJsonString(prompt) + "}"
                    + "  ]}"
                    + "]"
                    + "}";

            System.out.println("Sending JSON:\n" + jsonInput);  // Debug log

            URI uri = new URI(API_URL);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            // Write the request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get response
            int responseCode = connection.getResponseCode();
            InputStream inputStream = (responseCode == 200)
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            StringBuilder response = new StringBuilder();
            try (Scanner scanner = new Scanner(inputStream, "utf-8")) {
                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine().trim());
                }
            }

            System.out.println("\nResponse Code: " + responseCode);
            System.out.println("Raw Response:\n" + response);

            // Parse the JSON response
            
            return response;
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        return null;
    }

    // Escape JSON safely
    private static String toJsonString(String text) {
        return "\"" + text.replace("\\", "\\\\")
                          .replace("\"", "\\\"")
                          .replace("\n", "\\n")
                          .replace("\r", "") + "\"";
    }

    private static void keyFromDrive() throws Exception {
        String urlString = "https://drive.google.com/uc?export=download&id=1TjrgGGJprwDtaPxzEU7EaXdAgkHt1Tgk";
        URI uri = new URI(urlString);
        URL url = uri.toURL();
        Scanner s = new Scanner(url.openStream());
        String line = s.nextLine();
        System.out.println("Line: " + line);
        s.close();
        API_KEY = line.substring(line.indexOf("=")+1);  
        API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;
        System.out.println("API Key: " + API_KEY);
    }
    
}
