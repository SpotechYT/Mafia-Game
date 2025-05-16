public class Doctor extends Role{

    private Player savedPlayer;

    public Doctor() {
        // Constructor
        savedPlayer = null; // Initially, no player is saved
    }

    public void save(Player player) {
        // Logic to save a player
        // This could involve checking if the player is alive and then preventing their death
        if (player.isAlive()) {
            System.out.println(player.getName() + " has been saved by the doctor.");
            savedPlayer = player; // Save the player
        } else {
            System.out.println(player.getName() + " is already dead and cannot be saved.");
        }
    }

    public Player getSavedPlayer() {
        return savedPlayer;
    }

    public void reset(){
        savedPlayer = null; // Reset the saved player for the next round
    }
}
