

public class Mafia extends Role{
    private Player target;
    public Mafia() {
        super("Mafia", "Kill all civilians", "Kill", "path/to/mafia/icon.png");
        target = null; // Initially, no target
    }
    public Player getTarget() {
        return target;
    }
    public void setTarget(Player target) {
        this.target = target;
    }

    public void target(Player p) {
        if(!p.isAlive()) {
            System.out.println(p.getName() + " is already dead.");
        }else{
            System.out.println(p.getName() + " has been targeted by the mafia.");
            target = p; // Set the target
        }
    }


    public void kill() {
        // Logic to kill a player
        target.setAlive(false); // Set the target player to dead
        System.out.println(target.getName() + " has been killed by the mafia.");
        target = null; // Reset the target after killing
    }

    public void reset() {
        target = null; // Reset the target for the next round
    }

    @Override
    public String toString() {
        return super.toString() + "\n(Target: " + (target != null ? target.getName() : "None") + ")";
    }
}
