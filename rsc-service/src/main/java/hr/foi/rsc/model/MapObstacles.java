/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.rsc.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author paz
 */

@Entity
@Table(name="mapobstacles")
public class MapObstacles implements Serializable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_mapobstacles")
    long idMapObstacle;
    
    @Column(name="name")
    String name;
    
    @Column(name="lat")
    double lat;
    
    @Column(name="lng")
    double lng;
    
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="id_map")   
    Maps map;
 

    public long getIdMapObstacle() {
        return idMapObstacle;
    }

    public void setIdMapObstacle(long idMapObstacle) {
        this.idMapObstacle = idMapObstacle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
