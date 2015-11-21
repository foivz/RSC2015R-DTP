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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author paz
 */
@RestController
@RequestMapping(value = "/game")
public class GameController {
    
    GameRepository gameRepository;
    PersonRepository personRepository;
    TeamRepository teamRepository;
    TeamMemberRepository teamMemberRespository;

    @Autowired
    public GameController(GameRepository gameRepository, PersonRepository personRepository, TeamRepository teamRepository,
            TeamMemberRepository teamMemberRepository) {
        
        this.gameRepository = gameRepository;
        this.personRepository=personRepository;
        this.teamRepository=teamRepository;
        this.teamMemberRespository=teamMemberRepository;
        
        
    }
    
     /**
     * gets all games from database
     * @return all games in json format with HTTP 200
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Game>> retrieveAll() {
        Logger.getLogger("GameController.java").log(Level.INFO,
                "GET on /game -- retrieving full list of games");
        return new ResponseEntity(this.gameRepository.findAll(), HttpStatus.OK);
    }
    
    /**
     * gets game with specified id
     * @param id id of game
     * @return person info with HTTP 200 on success or HTTP 404 on fail
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Game> retrieveById(@PathVariable("id") long id) {
        
        org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.INFO,
                "GET on /game/" + id + " -- ");
        Game found = this.gameRepository.findByIdGame(id);
        
        if(found != null) {
            org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.INFO,
                    "Game found for id " + id + ", returning " + found.toString());
            return new ResponseEntity(found, HttpStatus.OK);
        } else {
            org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.WARN,
                    "No game found for id " + id);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Game> create(@RequestBody Game game) {
        org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.INFO,
                "POST on /game/create -- " + game.toString());
        
        Game signed = this.gameRepository.save(game);
        if(signed != null) {
            org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.INFO,
                    "Registration success for " + signed.toString());
            return new ResponseEntity(signed, HttpStatus.OK);
        } else {
            org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.WARN,
                    "Registration failed for " + game.toString());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity modify(@PathVariable("id") long id, @RequestBody Game game) {
        org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.INFO,
                "PUT on /game/" + id + " -- " + game.toString());
        
        Game signed = this.gameRepository.findByIdGame(id);
        if(signed != null) {
            this.gameRepository.save(game);
            org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.INFO,
                    "Update successful for " + game.toString());
            return new ResponseEntity(HttpStatus.OK);
        } else {
            org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.WARN,
                    "No game found for id " + id);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    
    @RequestMapping(value="{id}/team/{idTeam}",method=RequestMethod.GET)
    public ResponseEntity<List<TeamMember>> getLocations(@PathVariable("id") long idGame,@PathVariable("idTeam") long idTeam){
        
        List<TeamMember> teamMemberLocations=this.teamMemberRespository.findByIdTeam(idTeam);
        
        if(teamMemberLocations!=null){
            org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.WARN,
                    "No team found for id " + idTeam);
            return new ResponseEntity(teamMemberLocations,HttpStatus.OK);
         
        }
        else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        
    }
    
    @RequestMapping(value="/game/{code}", method=RequestMethod.GET)
    public ResponseEntity getGame(@PathVariable("code") String code){
        
        Game game=this.gameRepository.findByCode(code);
        
        if(game!=null){
             org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.WARN,
                    " team found for code " + code);
            return new ResponseEntity(game,HttpStatus.OK);
        }
        else{
             org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.WARN,
                    " team found for code " + code);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    
    @RequestMapping(value="{code}", method=RequestMethod.POST)
     public ResponseEntity<List<Team>> getTeams(@PathVariable("code") String code){
        
        Game found = this.gameRepository.findByCode(code);
        List<Team> teams=found.getTeam();
        if(teams!=null){
            org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.WARN,
                    "No team found for code " + code);
            return new ResponseEntity(teams,HttpStatus.OK);
         
        }
        else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        
    }
    
}
