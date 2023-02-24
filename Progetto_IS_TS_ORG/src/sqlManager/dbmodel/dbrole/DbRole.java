package sqlManager.dbmodel.dbrole;

import java.util.Objects;

public class DbRole {

    private int id;
    private final String name;

    public DbRole(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public DbRole(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbRole dbRole = (DbRole) o;
        return id==dbRole.id || name.equals(dbRole.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
