/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.rsc.repositories;

import hr.foi.rsc.model.Person;
import java.util.List;
import javax.persistence.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Tomislav Turek
 */
@Repository
@Table(name="person")
public interface PersonRepository extends CrudRepository<Person, String> {
    
    public Person findByIdPerson(long id);
    
    
    public List<Person> findByIdPersonIn(List<Long> idPerson);
    
    public Person findByCredentialsUsername(String username);
    
}
