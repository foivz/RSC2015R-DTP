package hr.foi.rsc.model;

/**
 * Created by hrvoje on 21/11/15.
 */
public class Game {
    int idGame;
    String code;
    int timer;

    public Game(String code, int idGame, int timer) {
        this.code = code;
        this.idGame = idGame;
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
