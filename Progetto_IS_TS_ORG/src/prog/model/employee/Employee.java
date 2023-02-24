package prog.model.employee;

public class Employee {
    private final String name;
    private final String surname;
    private final String email;
    private final int sn;


    public Employee( int sn,String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.sn = sn;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public int getSn() {
        return sn;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object o) {
        if(o==null) return false;
        if(o==this) return true;
        if(!(o instanceof Employee)) return false;
        Employee e = (Employee) o;
        return sn==e.sn;
    }
    public int hashCode() {
        return (sn+"").hashCode();
    }
    public String toString() {
        return sn+" - "+name;
    }
}
