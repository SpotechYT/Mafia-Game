public class Game {
    boolean gameOver = false;

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
