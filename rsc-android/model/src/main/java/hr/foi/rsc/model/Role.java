package hr.foi.rsc.model;

/**
 * Created by hrvoje on 21/11/15.
 */
public class Role {
    int idRole;
    String name;

    public Role(int idRole, String name) {
        this.idRole = idRole;
        this.name = name;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
