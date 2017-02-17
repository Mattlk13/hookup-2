package rs.hookupspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hookupspring.common.enums.Enums;
import rs.hookupspring.entity.User;

import java.util.List;

/**
 * Created by Bandjur on 8/20/2016.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByFirebaseUID(String firebaseUID);
    public List<User> findAllByGender(Enums.Gender gender);
}
