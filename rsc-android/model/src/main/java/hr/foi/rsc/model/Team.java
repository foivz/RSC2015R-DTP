package hr.foi.rsc.model;

import java.io.Serializable;

/**
 * Created by hrvoje on 21/11/15.
 */
public class Team implements Serializable{
    int idTeam;
    String name;

    public Team(int idTeam, String name) {
        this.idTeam = idTeam;
        this.name = name;
    }

    public int getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
