/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.rsc.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author paz
 */
@Entity
@Table(name="teammember")
public class TeamMember implements Serializable{
    
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_teammember")
    long idTeamMember;
    
    
    @Column(name="id_person")
    long idPerson;
    
    @Column(name="id_team")
    long idTeam;
    
    @Column(name="kill")
    long kill;
    
    @Column(name="death")
    long death;
    
    @Column(name="lat")
    double lat;
    
    @Column(name="lng")
    double lng;

    public TeamMember() {
    }
    
    

    public TeamMember(long idPerson, long idTeam, double lat, double lng) {
        this.idPerson = idPerson;
        this.idTeam = idTeam;
        this.lat = lat;
        this.lng = lng;
    }

    public TeamMember(long idPerson, long idTeam, long kill, long death, double lat, double lng) {
        this.idPerson = idPerson;
        this.idTeam = idTeam;
        this.kill = kill;
        this.death = death;
        this.lat = lat;
        this.lng = lng;
    }
    
    
    
    
    
    public long getIdTeamMember() {
        return idTeamMember;
    }

    public void setIdTeamMember(long idTeamMember) {
        this.idTeamMember = idTeamMember;
    }

    public long getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(long idPerson) {
        this.idPerson = idPerson;
    }

    public long getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(long idTeam) {
        this.idTeam = idTeam;
    }

    public long getKill() {
        return kill;
    }

    public void setKill(long kill) {
        this.kill = kill;
    }

    public long getDeath() {
        return death;
    }

    public void setDeath(long death) {
        this.death = death;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
    
    
    
    
    
    
    
}
