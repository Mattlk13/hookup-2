package rs.hookupspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hookupspring.entity.User;

/**
 * Created by Bandjur on 8/20/2016.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByFirebaseUID(String firebaseUID);
}
