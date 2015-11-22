/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.rsc.repositories;

import hr.foi.rsc.model.Notification;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author paz
 */
public interface NotificationRepository extends CrudRepository<Notification,String>{
    
    public List<Notification> findByIdTeamAndPersonAndRead(long idTeam, long idPerson,long read);
   
    
}
