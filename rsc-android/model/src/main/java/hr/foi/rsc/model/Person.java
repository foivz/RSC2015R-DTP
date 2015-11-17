package hr.foi.rsc.model;

import java.io.Serializable;

/**
 *
 * Created by Tomislav Turek on 23.10.15..
 */
public class Person implements Serializable {

    long idPerson;
    String name;
    String surname;
    Credentials credentials;

    public Person() {
    }

    public Person(long idPerson, String name, String surname, Credentials credentials) {
        this.idPerson = idPerson;
        this.name = name;
        this.surname = surname;
        this.credentials = credentials;
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

    public long getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(long idPerson) {
        this.idPerson = idPerson;
    }

    @Override
    public String toString() {
        return this.name + " " + this.surname;
    }
}
