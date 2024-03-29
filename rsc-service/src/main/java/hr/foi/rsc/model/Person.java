/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.rsc.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Tomislav Turek
 */
@Entity
@Table(name="person")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="idPerson") 
public class Person implements Serializable {
    
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_person")
    long idPerson;
    
    @Column(name="name")
    String name;
    
    @Column(name="surname")
    String surname;
   
    @Embedded
    Credentials credentials;
    
    @Column(name="kills")
    int kill;
    
    @Column(name="death")
    int death;
    
    @Column(name="lat")
    double lat;
    
    @Column(name="lng")
    double lng;
    
    @Column(name="ready")
    double ready;

    public double getReady() {
        return ready;
    }

    public void setReady(double ready) {
        this.ready = ready;
    }
    
    

 
    @OneToMany(fetch = FetchType.LAZY, mappedBy ="judge", 
			 cascade=CascadeType.ALL)
    @JsonManagedReference
    private List<Game> judgedGames;
    
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "person_role", joinColumns = { @JoinColumn(name = "id_person") },
            inverseJoinColumns = { @JoinColumn(name = "id_role") })
    Set<Role> roles = new HashSet<>();

    public Person() {
    }

    public Person(Person person) {
        super();
        this.idPerson = person.getIdPerson();
        this.name = person.getName();
        this.surname = person.getSurname();
        this.credentials = person.getCredentials();
    }

    public long getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(long idPerson) {
        this.idPerson = idPerson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public List<Game> getJudgedGames() {
        return judgedGames;
    }

    public void setJudgedGames(List<Game> judgedGames) {
        this.judgedGames = judgedGames;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

  
  
    @Override
    public String toString() {
        return this.name + " " + this.surname;
    }
    
        public int getKill() {
        return kill;
    }

    public void setKill(int kill) {
        this.kill = kill;
    }

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
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
