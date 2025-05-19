public class Player {
    /*TODO:
    * Variables: name, role, alive, votes, PathToIcon
    */
    private String name;
    private Role role;
    private boolean alive;
    private int votes;
    private String pathToIcon;

    // Constructor
    public Player(String name, int votes, String pathToIcon) {
        this.name = name;
        Civilian civilian = new Civilian(); // Default role is civilian 
        this.role = civilian;
        this.alive = true; // Default to alive
        this.votes = votes;
        this.pathToIcon = pathToIcon;
    }


    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
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
