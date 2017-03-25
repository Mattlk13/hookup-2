package rs.hookupspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hookupspring.entity.IgnoredPair;

import java.util.List;

/**
 * Created by Bandjur on 3/25/2017.
 */
public interface IgnoredPairRepository extends JpaRepository<IgnoredPair, Integer> {
    public List<IgnoredPair> findAllByUserAId(int userAId);
    public List<IgnoredPair> findAllByUserBId(int userBId);
}
