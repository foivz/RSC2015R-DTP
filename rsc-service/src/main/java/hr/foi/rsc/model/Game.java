/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.rsc.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author paz
 */
@Entity
@Table(name = "game")
public class Game {
    
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "idgame")
    long idGame;
    
    @Column(name="timer")
    int timer;
    
    @Column(name="code")
    String code;
    
    @JsonManagedReference
    @ManyToOne
    Person judge;
    
    @OneToMany( mappedBy ="game", 
			 cascade=CascadeType.ALL)
     private List<Team> teams;
    
    

    public long getIdGame() {
        return idGame;
    }

    public void setIdGame(long idGame) {
        this.idGame = idGame;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Person getJudge() {
        return judge;
    }

    public void setJudge(Person judge) {
        this.judge = judge;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
    
    
    
    
    
    
}
