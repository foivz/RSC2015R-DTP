/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.rsc.controllers;

import hr.foi.rsc.model.Maps;
import hr.foi.rsc.repositories.MapsRepository;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author paz
 */
@RestController
@RequestMapping(value = "/maps")
public class MapsController {
    
    MapsRepository mapsRepository;
    
    @Autowired
    public MapsController(MapsRepository mapsRepository) {
        
        this.mapsRepository=mapsRepository;
        
    }
    
     /**
     * gets all maps from database
     * @return all maps in json format with HTTP 200
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Maps>> retrieveAll() {
        Logger.getLogger("MapsController.java").log(Level.INFO,
                "GET on /maps -- retrieving full list of maps");
        return new ResponseEntity(this.mapsRepository.findAll(), HttpStatus.OK);
    }
    
    @RequestMapping(value="/create", method=RequestMethod.POST)
    public ResponseEntity<Maps> createMap(@RequestBody Maps map){
         org.jboss.logging.Logger.getLogger("MapController.java").log(org.jboss.logging.Logger.Level.INFO,
                "POST on /map/create -- " + map.toString());
        
        Maps signed = this.mapsRepository.save(map);
        if(signed != null) {
            org.jboss.logging.Logger.getLogger("MapsController.java").log(org.jboss.logging.Logger.Level.INFO,
                    "Creation succes for " + signed.toString());
            return new ResponseEntity(signed, HttpStatus.OK);
        } else {
            org.jboss.logging.Logger.getLogger("GameController.java").log(org.jboss.logging.Logger.Level.WARN,
                    "Registration failed for " + map.toString());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    
    
}
