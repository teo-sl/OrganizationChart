package prog.model.role;

public class Role {
    private final String name;

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public boolean equals(Object o) {
        if(o==null) return false;
        if(o==this) return true;
        if(!(o instanceof Role)) return false;
        Role r=(Role) o;
        return r.name.equals(name);
    }

    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
