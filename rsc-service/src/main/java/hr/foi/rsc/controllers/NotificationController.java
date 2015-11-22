/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.rsc.controllers;

import hr.foi.rsc.model.Notification;
import hr.foi.rsc.model.TeamMember;
import hr.foi.rsc.repositories.NotificationRepository;
import hr.foi.rsc.repositories.TeamMemberRepository;
import java.util.ArrayList;
import java.util.List;
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
@RequestMapping(value = "/notification")
public class NotificationController {
    
         NotificationRepository notificationRepository;
         TeamMemberRepository teamMemberRepository;
         
         @Autowired
         public NotificationController(NotificationRepository notificationRepostiory,
                                       TeamMemberRepository teamMeberReposiory){
             
             this.notificationRepository    =   notificationRepostiory;
             this.teamMemberRepository = teamMeberReposiory;

         }
         
         @RequestMapping(value="/{id}/team/{idTeam}", method = RequestMethod.GET)
         public ResponseEntity<Notification> getNotification(@PathVariable("id") long idPerson, @PathVariable("idTeam") long idTeam){
             
             
             
             org.jboss.logging.Logger.getLogger("NotController.java").log(org.jboss.logging.Logger.Level.INFO,
                "getting notifications ");
             
             
             org.jboss.logging.Logger.getLogger("NotController.java").log(org.jboss.logging.Logger.Level.INFO,
                "id person " + idPerson );
             
             org.jboss.logging.Logger.getLogger("NotController.java").log(org.jboss.logging.Logger.Level.INFO,
                "id team " + idTeam );
             
             List<Notification> unread = this.notificationRepository.findByIdTeam(idTeam);
             List<Notification> un2=new ArrayList<>();
             
             for(Notification a : unread){
                 
                 org.jboss.logging.Logger.getLogger("NotController.java").log(org.jboss.logging.Logger.Level.INFO,
                "id person" + a.getIdPerson());
                        
                    if(a.getIdPerson() != idPerson && a.getReaded() == 0 ){
                         org.jboss.logging.Logger.getLogger("NotController.java").log(org.jboss.logging.Logger.Level.INFO,
                        "id dodajem" );
                        un2.add(a);
                    }
             }
             
          
             
             if( un2 != null ){
                  org.jboss.logging.Logger.getLogger("NotController.java").log(org.jboss.logging.Logger.Level.INFO,
                "id person un2" + un2.get(0).getName());
                 return new ResponseEntity(un2,HttpStatus.OK);
             }
             else
                 return new ResponseEntity(HttpStatus.NOT_FOUND);
             
         }
         
         @RequestMapping(value="/{id}", method = RequestMethod.POST)
         public ResponseEntity setReadedNotification(@RequestBody List<Notification> notifications){
             org.jboss.logging.Logger.getLogger("NotController.java").log(org.jboss.logging.Logger.Level.INFO,
                "reading notifications ");
             
            for(Notification n: notifications){
                org.jboss.logging.Logger.getLogger("NotController.java").log(org.jboss.logging.Logger.Level.INFO,
                "reading notification " +n.getName() +" " +n.getIdPerson());
                
                n.setReaded(1);
                
                this.notificationRepository.save(n);
                
            }
            
            if(notifications != null )
                return new ResponseEntity(HttpStatus.OK);
            else
                return new ResponseEntity(HttpStatus.NOT_FOUND);
             
             
         }
         
         @RequestMapping(value="/{idTeam}/teamnotifications", method = RequestMethod.GET)
         public ResponseEntity<List<Notification>> retriveTeamNotification(@PathVariable("idTeam") long idTeam){
             
             List<Notification> notf=this.notificationRepository.findByIdTeam(idTeam);
             List<Notification> notfReturn=null;
             
            if(notf.size()>7){
                for(int i=notf.size()-1; i>notf.size()-6;i--){
                notfReturn.add(notf.get(i));
                }
                return new ResponseEntity(notfReturn,HttpStatus.OK);
            }else
               return new ResponseEntity(notf,HttpStatus.OK);
             
         }
         
         @RequestMapping(value="/{idTeam}/person/{idPerson}/message/{message}", method = RequestMethod.POST)
         public ResponseEntity setNotification(@PathVariable("idTeam")long idTeam,
                 @PathVariable("idPerson") long idPerson,
                 @PathVariable("message") String message){
             
             org.jboss.logging.Logger.getLogger("NotController.java").log(org.jboss.logging.Logger.Level.INFO,
                "seting notifications ");
             
             List <TeamMember> members=this.teamMemberRepository.findByIdTeam(idTeam);
             
             Notification notf=null;
             
             for(TeamMember m: members){
                 
                  if(m.getIdPerson() == idPerson)
                      continue;
                 
                  notf=new Notification();
                  notf.setIdPerson(m.getIdPerson());
                  notf.setIdTeam(idTeam);
                  notf.setReaded(0);
                  notf.setName(message);
                  this.notificationRepository.save(notf);
                  
             }
             
             
             if((notf)!=null){
                 return new ResponseEntity(HttpStatus.OK);
             }else{
                 return new ResponseEntity(HttpStatus.NOT_FOUND);
             }
             
           
             
         }
    
}
