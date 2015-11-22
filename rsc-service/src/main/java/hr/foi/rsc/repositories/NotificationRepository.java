/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.rsc.repositories;

import hr.foi.rsc.model.Notification;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Table;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author paz
 */
@Repository
@Table(name="notification")
public interface NotificationRepository extends CrudRepository<Notification , String>{
    
    public ArrayList<Notification> findByIdTeam(long idTeam);
   
}



