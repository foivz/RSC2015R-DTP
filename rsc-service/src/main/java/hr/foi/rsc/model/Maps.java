/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.rsc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author paz
 */
@Entity
@Table(name="maps")
public class Maps implements Serializable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_map")
    long idMap;
    
    @Column(name="name")
    String name;
    
    @Column(name="start_lat")
    double startLat;
    @Column(name="end_lat")
    double endLat;
    
    @Column(name="start_lng")
    double startLng;
    
    @Column(name="end_lng")
    double endLng;
    
    @Column(name="flag_lat")
    double flagLat;
    
    @Column(name="flag_lng")
    double flagLng;
    
    @JsonIgnore
    @OneToMany( mappedBy ="map", 
			 cascade=CascadeType.ALL)
    List<Game> game;

    public long getIdMap() {
        return idMap;
    }

    public void setIdMap(long idMap) {
        this.idMap = idMap;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getStartLng() {
        return startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }

    public double getEndLng() {
        return endLng;
    }

    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }

    public double getFlagLat() {
        return flagLat;
    }

    public void setFlagLat(double flagLat) {
        this.flagLat = flagLat;
    }

    public double getFlagLng() {
        return flagLng;
    }

    public void setFlagLng(double flagLng) {
        this.flagLng = flagLng;
    }

    public List<Game> getGame() {
        return game;
    }

    public void setGame(List<Game> game) {
        this.game = game;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
    
    
    
}
