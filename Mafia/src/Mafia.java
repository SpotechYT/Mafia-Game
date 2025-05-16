

public class Mafia extends Role{
    private Player target;
    public Mafia() {
        // Constructor
    }

    public void kill(Player target) {
        // Logic to kill a player
        if(!target.isAlive()) {
            System.out.println(target.getName() + " is already dead.");
        } else {
            this.target = target;
        }
    }
}
