package rs.hookupspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hookupspring.entity.Hookup;

import java.util.List;

/**
 * Created by Bandjur on 8/27/2016.
 */
public interface HookupRepository extends JpaRepository<Hookup, Integer> {

    public List<Hookup> findAllByUserAId(int userAId);
    public List<Hookup> findAllByUserBId(int userBId);

}
