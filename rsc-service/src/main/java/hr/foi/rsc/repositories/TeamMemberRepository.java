/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.rsc.repositories;

import hr.foi.rsc.model.TeamMember;
import java.util.List;
import javax.persistence.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author paz
 */
@Repository
@Table(name="teammember")
public interface TeamMemberRepository extends CrudRepository<TeamMember, String>{
    
     public TeamMember findByIdTeamMember(long id);
     public TeamMember findByIdPerson(long id);
     public List<TeamMember> findByIdTeam(long id);
     
     public List<TeamMember> findByIdPersonIn(List<Long> idPerson);
     

}
