/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.rsc.controllers;

//import hr.foi.rsc.model.MapObstacles;
//import hr.foi.rsc.repositories.MapObstaclesRepository;
import hr.foi.rsc.model.MapObstacles;
import hr.foi.rsc.repositories.MapObstaclesRepository;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author paz
 */

@RestController
@RequestMapping(value = "/mapsobstacles")
public class MapObstaclesController {
    
    MapObstaclesRepository mapObstaclesRepository;
   

    @Autowired
    public MapObstaclesController(MapObstaclesRepository mapObstaclesRepository) {
        
        this.mapObstaclesRepository = mapObstaclesRepository;
             
    }
    
     /**
     * gets all games from database
     * @return all games in json format with HTTP 200
     */

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<MapObstacles>> retrieveAll() {
        Logger.getLogger("MapsObstaclesController.java").log(Level.INFO,
                "GET on /game -- retrieving full list of games");
        return new ResponseEntity(this.mapObstaclesRepository.findAll(), HttpStatus.OK);
    }
    
    
}
