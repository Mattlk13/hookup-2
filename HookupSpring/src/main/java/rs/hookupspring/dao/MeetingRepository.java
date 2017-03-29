package rs.hookupspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hookupspring.entity.Meeting;

/**
 * Created by Bandjur on 3/29/2017.
 */
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
}
