/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.rsc.controllers;

import hr.foi.rsc.model.Game;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.model.Team;
import hr.foi.rsc.model.TeamMember;
import hr.foi.rsc.repositories.GameRepository;
import hr.foi.rsc.repositories.PersonRepository;
import hr.foi.rsc.repositories.TeamMemberRepository;
import hr.foi.rsc.repositories.TeamRepository;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author paz
 */
@RestController
@RequestMapping(value = "/team")
public class TeamController {
    
    TeamRepository teamRepository;
    TeamMemberRepository teamMemberRepository;
    PersonRepository personRepository;
    

    @Autowired
    public TeamController(TeamRepository teamRepository, TeamMemberRepository teamMemberRepository, 
            PersonRepository personRepository) {
        
        this.teamRepository = teamRepository;
        this.teamMemberRepository=teamMemberRepository;
        this.personRepository=personRepository;
        
    }
    
     /**
     * gets all games from database
     * @return all games in json format with HTTP 200
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Game>> retrieveAll() {
        Logger.getLogger("PersonController.java").log(Level.INFO,
                "GET on /game -- retrieving full list of games");
        return new ResponseEntity(this.teamRepository.findAll(), HttpStatus.OK);
    }
    
    /**
     * gets game with specified id
     * @param id id of game
     * @return person info with HTTP 200 on success or HTTP 404 on fail
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Game> retrieveById(@PathVariable("id") long id) {
        
        org.jboss.logging.Logger.getLogger("TeamController.java").log(org.jboss.logging.Logger.Level.INFO,
                "GET on /game/" + id + " -- ");
        Team found = this.teamRepository.findByIdTeam(id);
        
        if(found != null) {
            org.jboss.logging.Logger.getLogger("TeamController.java").log(org.jboss.logging.Logger.Level.INFO,
                    "Game found for id " + id + ", returning " + found.toString());
            return new ResponseEntity(found, HttpStatus.OK);
        } else {
            org.jboss.logging.Logger.getLogger("TeamController.java").log(org.jboss.logging.Logger.Level.WARN,
                    "No game found for id " + id);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Game> create(@RequestBody Team team) {
        org.jboss.logging.Logger.getLogger("TeamController.java").log(org.jboss.logging.Logger.Level.INFO,
                "POST on /game/create -- " + team.toString());
        
        Team signed = this.teamRepository.save(team);
        if(signed != null) {
            org.jboss.logging.Logger.getLogger("TeamController.java").log(org.jboss.logging.Logger.Level.INFO,
                    "Registration success for " + signed.toString());
            return new ResponseEntity(signed, HttpStatus.OK);
        } else {
            org.jboss.logging.Logger.getLogger("TeamController.java").log(org.jboss.logging.Logger.Level.WARN,
                    "Registration failed for " + team.toString());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity modify(@PathVariable("id") long id, @RequestBody Team team) {
        org.jboss.logging.Logger.getLogger("TeamController.java").log(org.jboss.logging.Logger.Level.INFO,
                "PUT on /person/" + id + " -- " + team.toString());
        
        Team signed = this.teamRepository.findByIdTeam(id);
        if(signed != null) {
            this.teamRepository.save(team);
            org.jboss.logging.Logger.getLogger("TeamController.java").log(org.jboss.logging.Logger.Level.INFO,
                    "Update successful for " + team.toString());
            return new ResponseEntity(HttpStatus.OK);
        } else {
            org.jboss.logging.Logger.getLogger("TeamController.java").log(org.jboss.logging.Logger.Level.WARN,
                    "No user found for id " + id);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    
    @RequestMapping(value="/{id}/person/{idPerson}", method=RequestMethod.POST)
    public ResponseEntity addTeamMember(@PathVariable("id") long id,@PathVariable("idPerson") long idPerson){
        
         org.jboss.logging.Logger.getLogger("TeamController.java").log(org.jboss.logging.Logger.Level.INFO,
                    "Finding team ");
        Team foundTeam = this.teamRepository.findByIdTeam(id);
        
         org.jboss.logging.Logger.getLogger("TeamController.java").log(org.jboss.logging.Logger.Level.INFO,
                    "Finding user ");
         
        Person foundPerson = this.personRepository.findByIdPerson(id);
        
        TeamMember memberTeam= new TeamMember(foundPerson.getIdPerson(),foundTeam.getIdTeam(),0,0,0.0,0.0);
        
        if(foundPerson!=null && foundTeam!=null){
            
             org.jboss.logging.Logger.getLogger("TeamController.java").log(org.jboss.logging.Logger.Level.INFO,
                    "Saving memberTeam " + memberTeam.toString());
             
             this.teamMemberRepository.save(memberTeam);
             return new ResponseEntity(memberTeam, HttpStatus.OK);
             
            
        }else{
             org.jboss.logging.Logger.getLogger("TeamController.java").log(org.jboss.logging.Logger.Level.INFO,
                    "Failed " + memberTeam.toString());
             return new ResponseEntity(HttpStatus.NOT_FOUND);
            
        }
        
        
        
        
        
    }
    
    
}
