import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MafiaScenarioGenerator {
    private static String API_KEY; 
    private static String API_URL;
    private static ArrayList<String> weapons = new ArrayList<>();

    public static String getScenario(String target, boolean saved) throws Exception {
        StringBuilder response = getRAWScenario(target, saved);
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

    public static StringBuilder getRAWScenario(String target, boolean saved) throws Exception {
        keyFromDrive();  // Load API key from environment variable
        try {
            // Prompt for Gemini
            String prompt = """
                Write a short story about the following events:
                - The Mafia tried to kill {target}.
                - {target} was saved by the Doctor = {saved}.
                If the Doctor did not save {target}, write a story about the Mafia killing {target}  successfully. 
                If the Doctor did save {target}, write a story about the Mafia failing to kill {target}.
                Avoid using dialogue. Act as a narrator. Keep it simple and avoid using complex words.
                Use only general terms, like "the mafia" or "the doctor", and avoid using specific names, other than {target}.
                The attack can be anything. The mafia can attack {target} in any way they want, even random and absurd ways.
                The story should be in the past tense.
                Use a third person perspective. 
                Generate only one story, not multiple stories.
                This is for the mafia party game.

                Use the following example as a reference:
                PLAYERNAME was eliminated by The Mafia after mistaking a horse’s head for a pillow. Rest in pieces (and hay).
                PLAYERNAME challenged The Mafia to a dance battle. Unfortunately, The Mafia moonwalked over their spine. Fatal, but funky.
                PLAYERNAME thought The Mafia was a magician. They volunteered for the "sawing in half" trick. No refunds.
                PLAYERNAME received a suspicious cannoli. They ate it anyway. "Leave the gun, take the indigestion."
                PLAYERNAME tried to spy on The Mafia with a drone. The Mafia returned the favor with a live grenade strapped to a pigeon.
                PLAYERNAME was whacked after tweeting “#MafiaSucks.” Turns out The Mafia follows back—and hits back harder.
                PLAYERNAME tripped on a banana peel left by The Mafia. Classic slapstick… with a deadly fall off a 3-story balcony.
                PLAYERNAME refused to pay protection money—in Monopoly. The Mafia flipped the board... and the table... and PLAYERNAME.
                PLAYERNAME opened a cursed spaghetti shop across the street from The Mafia's restaurant. Business: bad. Fate: worse.
                PLAYERNAME tried to prank The Mafia with a fake parking ticket. The Mafia responded by parking a tank on them.
                PLAYERNAME got sleep-deprived and accused the toaster of being The Mafia. The actual Mafia heard—and toasted them.
                PLAYERNAME was cast as an extra in a mafia movie. The Mafia thought it was a documentary. Rest in realism.
                PLAYERNAME stole The Mafia’s favorite meme and watermarked it. Internet beef turned into actual beef—with cement shoes.
                PLAYERNAME thought they could out-pizza The Mafia. The Mafia delivered… a pineapple-and-C4 special.
                PLAYERNAME hosted a roast of The Mafia. The jokes were killer. So was The Mafia.
                PLAYERNAME was crushed by a suspiciously large meatball. The Doctor re-inflated them using a bike pump and olive oil.
                The Mafia filled PLAYERNAME’s bathtub with live eels. After the shocking death, The Doctor jumpstarted them back with jumper cables and whale songs.
                PLAYERNAME choked on a poisoned cannoli. The Doctor removed the toxin using a reverse espresso enema and strong opinions.
                The Mafia launched PLAYERNAME into the sun (via catapult). The Doctor caught the ashes in a jar, added Red Bull, and shook vigorously. Reassembled!
                PLAYERNAME was drowned in carbonated water. The Doctor resuscitated them by whispering flat facts until the fizz wore off.
                PLAYERNAME exploded from laughing at a Mafia pun. The Doctor stitched them back together using duct tape and a CPR-certified dad joke.
                The Mafia hit PLAYERNAME with a piano. The Doctor extracted their soul from the C key and slammed it back in with a defibrillator solo.
                PLAYERNAME was run over by a gelato truck. The Doctor scooped their remains into a waffle cone and reanimated them with sprinkles of life.
                The Mafia turned PLAYERNAME into lasagna. The Doctor deconstructed the dish, shouted “UNDO,” and cast a life-saving microwave spell.
                PLAYERNAME was launched into orbit by a “cement cannon.” The Doctor caught them with a space Roomba and rebooted their vitals with space WiFi.
                PLAYERNAME was turned into a statue by The Mafia's cursed Nonna. The Doctor melted the curse with a garlic poultice and soft jazz.
                The Mafia poisoned PLAYERNAME’s bubble tea. The Doctor extracted the toxic boba with tiny tweezers and pure disbelief.
                PLAYERNAME’s soul was sent to voicemail. The Doctor called it back, left a strongly worded message, and boom—respawned.
                PLAYERNAME was folded into a pizza box. The Doctor unboxed them, microwaved for 30 seconds, and fluffed them back to life.
                PLAYERNAME died in a mysterious “accident” involving a rubber duck and a fish tank. The Doctor performed a rubber-duckectomy and rebooted them with a snorkel and CPR rap beat.
                """;

            prompt = prompt.replace("{target}", target)
                           .replace("PLAYERNAME", target)
                           .replace("{saved}", String.valueOf(saved));

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
