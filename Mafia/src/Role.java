public class Role {

    private String name;
    private String description;
    private String ability;
    private String iconPath;

    // Constructor
    public Role(String name, String description, String ability, String iconPath) {
        this.name = name;
        this.description = description;
        this.ability = ability;
        this.iconPath = iconPath;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
    
    @Override
    public String toString() {
        return "Role{" +
                "Name: " + name +
                ", Description: " + description +
                ", Ability: " + ability +
            '}';
    }

    

}
