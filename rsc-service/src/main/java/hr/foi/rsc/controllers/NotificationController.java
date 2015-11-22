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
             
             List<Notification> unread=
                     this.notificationRepository.findByIdTeamAndPersonAndRead(idPerson, idTeam, 0);
             
             if(unread!=null)
                 return new ResponseEntity(unread,HttpStatus.OK);
             else
                 return new ResponseEntity(HttpStatus.NOT_FOUND);
             
         }
         
         @RequestMapping(value="/{id}", method = RequestMethod.POST)
         public ResponseEntity setReadedNotification(@RequestBody List<Notification> notifications){
             
            for(Notification n: notifications){
                n.setRead(1);
                this.notificationRepository.save(n);
                
            }
            
            if(notifications != null )
                return new ResponseEntity(HttpStatus.OK);
            else
                return new ResponseEntity(HttpStatus.NOT_FOUND);
             
             
         }
         
         @RequestMapping(value="/{idTeam}/person/{idPerson}/message/{message}", method = RequestMethod.POST)
         public ResponseEntity setNotification(@PathVariable("idTeam")long idTeam,
                 @PathVariable("idPerson") long idPerson,
                 @PathVariable("message") String message){
             
             List <TeamMember> members=this.teamMemberRepository.findByIdTeam(idTeam);
             
             Notification notf=null;
             
             for(TeamMember m: members){
                 
                  notf=new Notification();
                  notf.setId_person(m.getIdPerson());
                  notf.setId_team(idTeam);
                  notf.setRead(0);
                  notf.setName(message);
                 
             }
             
             
             if((this.notificationRepository.save(notf))!=null){
                 return new ResponseEntity(HttpStatus.OK);
             }else{
                 return new ResponseEntity(HttpStatus.NOT_FOUND);
             }
             
           
             
         }
    
}
