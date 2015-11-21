package hr.foi.rsc.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hrvoje on 21/11/15.
 */
public class Game implements Serializable{
    int idGame;
    String name;
    String code;
    int timer;
    List<Team> team;
    int start;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Team> getTeam() {
        return team;
    }

    public void setTeam(List<Team> team) {
        this.team = team;
    }

    public Game(String code, int idGame, String name, int timer) {
        this.code = code;
        this.idGame = idGame;
        this.name = name;
        this.timer = timer;
    }

   public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIdGame() {
        return idGame;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
}
