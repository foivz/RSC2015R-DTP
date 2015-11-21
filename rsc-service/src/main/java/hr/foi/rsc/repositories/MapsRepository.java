/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.rsc.repositories;

import hr.foi.rsc.model.Game;
import hr.foi.rsc.model.Maps;
import javax.persistence.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author paz
 */
@Repository
@Table(name="maps")
public interface MapsRepository extends CrudRepository<Maps, String>{
    
    public Maps findByIdGame(long id);
    
}
