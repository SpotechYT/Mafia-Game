public class Player {
    /*TODO:
    * Variables: name, role, alive, votes, PathToIcon
    */
    private String name;
    private Role role;
    private boolean alive;
    private String pathToChar;

    // Constructor
    public Player(String name, String pathToChar) {
        this.name = name;
        Civilian civilian = new Civilian(); // Default role is civilian 
        this.role = civilian;
        this.alive = true; // Default to alive
        this.pathToChar = pathToChar;
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

    public String getPathToChar() {
        return pathToChar;
    }

    public void setPathToChar(String pathToChar) {
        this.pathToChar = pathToChar;
    }

    
}
