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
@Table(name="notification")
public class Notification implements Serializable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_notification")
    long idNotification;
    
    @Column(name="content")
    String name;
    
    @Column(name="id_team")
    long id_team;
    
    @Column(name="id_person")
    long id_person;
    
    @Column(name="read")
    int read;


    public long getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(long idNotification) {
        this.idNotification = idNotification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId_team() {
        return id_team;
    }

    public void setId_team(long id_team) {
        this.id_team = id_team;
    }

    public long getId_person() {
        return id_person;
    }

    public void setId_person(long id_person) {
        this.id_person = id_person;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    
    
    
    
   
    
}
