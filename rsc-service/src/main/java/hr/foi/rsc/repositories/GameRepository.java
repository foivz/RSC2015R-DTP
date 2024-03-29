/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.rsc.repositories;

import hr.foi.rsc.model.Game;
import hr.foi.rsc.model.Person;
import javax.persistence.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author paz
 */
@Repository
@Table(name="game")
public interface GameRepository extends CrudRepository<Game, String>{
    
      public Game findByIdGame(long id);
      public Game findByCode(String code);
      
}
