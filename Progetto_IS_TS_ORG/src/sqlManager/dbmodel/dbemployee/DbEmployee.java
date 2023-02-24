package sqlManager.dbmodel.dbemployee;

public class DbEmployee {

    private int id;
    private final int sn;
    private String name;
    private final String surname;
    private final String email;

    public DbEmployee(int id, String name, String surname, String email,int sn) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.sn=sn;
    }

    public int getSn() {
        return sn;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
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

}
