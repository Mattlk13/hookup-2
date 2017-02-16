package rs.hookupspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hookupspring.entity.UserPsychology;

/**
 * Created by Bandjur on 2/16/2017.
 */
public interface UserPsychologyRepository extends JpaRepository<UserPsychology, Integer> {
}
