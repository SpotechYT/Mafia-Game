
import java.util.HashMap;

public class Game {
    boolean gameOver = false;
    // Name, IP
    HashMap<String, String> players = new HashMap<>();
    // Player, Role
    HashMap<String, String> roles = new HashMap<>();

    public void addPlayer(String name, String ip) {
        players.put(name, ip);
    }

    public void startGame() {
        // Initialize game logic here
        System.out.println("Game started!");
        // Add your game logic here

        assignRoles();
        while(!gameOver) {
            // Game loop
            // Check for game over conditions
            // If game over, set gameOver = true;
            gameOver = true;
        }
    }

    public void assignRoles() {
        // Logic to assign roles to players
        int mafia = (int)(Math.random() * players.size());
        int doctor = (int)(Math.random() * players.size());
        while(mafia == doctor) {
            doctor = (int)(Math.random() * players.size());
        }

        System.out.println("Roles assigned!");
    }

    public void startNightPhase() {
        // Logic to start the night phase
        System.out.println("Night phase started!");
    }

    public void startDayPhase() {
        // Logic to start the day phase
        System.out.println("Day phase started!");
    }
}
