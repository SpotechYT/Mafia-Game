public class Player {
    /*TODO:
    * Variables: name, role, alive, votes, PathToIcon
    */
    private String name;
    private String role;
    private boolean alive;
    private int votes;
    private String pathToIcon;

    public Player(String name, int votes, String pathToIcon) {
        this.name = name;
        this.role = "civilian"; // Default to civilian
        this.alive = true; // Default to alive
        this.votes = votes;
        this.pathToIcon = pathToIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getPathToIcon() {
        return pathToIcon;
    }

    public void setPathToIcon(String pathToIcon) {
        this.pathToIcon = pathToIcon;
    }

    
}
