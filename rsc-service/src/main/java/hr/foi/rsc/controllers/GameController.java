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
import java.util.ArrayList;
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
    
    @RequestMapping(value="/{id}/timer/{sec}", method = RequestMethod.PUT)
    public ResponseEntity updateUserLocation(@PathVariable("id") long id,@PathVariable("sec") int timer){
        
        Logger.getLogger("GameController.java").log(Level.INFO,
                "saving timer");
        Game game=this.gameRepository.findByIdGame(id);
        game.setTimer(timer);
        this.gameRepository.save(game);
        Logger.getLogger("GameController.java").log(Level.INFO,
                "GET on /game -- retrieving full list of games");
        if(timer==0){
            Logger.getLogger("GameController.java").log(Level.INFO,
                "timer 0");
            return new ResponseEntity(HttpStatus.NOT_FOUND);}
        else {
            Logger.getLogger("GameController.java").log(Level.INFO,
                "timernot 0");
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
 
    }
    
    
    @RequestMapping(value="/personLocation", method = RequestMethod.PUT)
    public ResponseEntity updateUserLocation(@RequestBody Person person){
        
        if(person!=null){
            
            this.personRepository.save(person);
            Logger.getLogger("GameController.java").log(Level.INFO,
                "update person location");
            return new ResponseEntity(HttpStatus.OK);
            
        }else{
            
            Logger.getLogger("GameController.java").log(Level.INFO,
                "cannot update");
            
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
                
        
    }
    
    @RequestMapping(value="/{id}/end", method = RequestMethod.GET)
    public ResponseEntity<List<TeamMember>> endGame(@PathVariable("id") long id){
        
        Game game=this.gameRepository.findByIdGame(id);
        int teamOne = game.getTeam().indexOf(0);
        int teamTwo = game.getTeam().indexOf(1);
        
        List<TeamMember> teamCompetitors= new ArrayList<>();
        
        teamCompetitors.addAll(this.teamMemberRespository.findByIdTeam(teamOne));
        teamCompetitors.addAll(this.teamMemberRespository.findByIdTeam(teamTwo));
        
        
         Logger.getLogger("GameController.java").log(Level.INFO,
                "Retrurning details of match");
        
        if(teamCompetitors != null){
             Logger.getLogger("GameController.java").log(Level.INFO,
                "Returning details");
               return new ResponseEntity(teamCompetitors,HttpStatus.OK);
        }
        else{
             Logger.getLogger("GameController.java").log(Level.INFO,
                "Cannot return details");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        
    }
    
    @RequestMapping(value = "/isReady/{id}", method = RequestMethod.GET)
    public ResponseEntity isReady(@PathVariable("id") long id){
        
        Game game=this.gameRepository.findByIdGame(id);
        
        if(game.getStart()==1)
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);    
    }
    
    
    @RequestMapping(value="/{id}/start", method = RequestMethod.POST)
    public ResponseEntity<Game> startGame(@PathVariable("id") long id){
        
        Game start = this.gameRepository.findByIdGame(id);
        
        if(start != null){
            
            start.setStart(1);
            start=this.gameRepository.save(start);
            return new ResponseEntity(start,HttpStatus.OK);
        }
        else 
             return new ResponseEntity(HttpStatus.NOT_FOUND);
        
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
    
    @RequestMapping(value = "/create/{id}", method = RequestMethod.POST)
    public ResponseEntity<Game> create(@PathVariable("id") long idUser, @RequestBody Game game) {
        
        org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.INFO,
                "POST on /game/create -- " + game.toString());
        
        Person judge= this.personRepository.findByIdPerson(idUser);
        
        
        Game signed = game;
        signed.setJudge(judge);
        signed= this.gameRepository.save(game);
        
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
    public ResponseEntity<List<Person>> getLocations(@PathVariable("id") long idGame,
                @PathVariable("idTeam") long idTeam){
        
        org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.INFO,
                    "Request comming");
        
        
        List<TeamMember> teamMemberLocations=this.teamMemberRespository.findByIdTeam(idTeam);
        
   
        List<Long> ids= new ArrayList<>();
        
        for(TeamMember a : teamMemberLocations){
            ids.add(a.getIdPerson());
            org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.INFO,
                    "Person" + a.getIdPerson());
        }
        
        List<Person> person=this.personRepository.findByIdPersonIn(ids);
        
        if(person!=null){
            
            org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.WARN,
                    "Returning persons " );
            return new ResponseEntity(person , HttpStatus.OK);
            
        }
        
        else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        
    }
    
    @RequestMapping(value="getGameCode/{code}", method=RequestMethod.GET)
    public ResponseEntity getGame(@PathVariable("code") String code){
        
         org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.WARN,
                    "  found for code " + code);
        
        
        Game game=this.gameRepository.findByCode(code);
        
         org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.WARN,
                    " team found for code " + game.toString());
        
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
    
    @RequestMapping(value="/{code}", method=RequestMethod.POST)
     public ResponseEntity<List<Team>> getTeams(@PathVariable("code") String code){
         
          org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.WARN,
                    "  POST found for code " + code);
        
        Game found = this.gameRepository.findByCode(code);
        List<Team> teams=found.getTeam();
        
         org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.WARN,
                    "  POST found for code " + code);
         
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
