package rs.hookupspring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hookupspring.entity.UserBasicInfo;

/**
 * Created by Bandjur on 2/15/2017.
 */
public interface UserBasicInfoRepository extends JpaRepository<UserBasicInfo, Integer> {
}
