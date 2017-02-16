package rs.hookupspring.springweb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.hookupspring.dao.HookupRepository;
import rs.hookupspring.dao.UserRepository;
import rs.hookupspring.entity.Hookup;
import rs.hookupspring.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bandjur on 8/27/2016.
 */
@Service
public class UserHookupService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HookupRepository hookupRepository;

    public List<User> getHookupList(User user, boolean paired) {
        List<User> hookupList = new ArrayList<User>();

        for (Hookup hookup : hookupRepository.findAllByUserAId(user.getId())) {
            if (hookup.isPaired() == paired) {
                User compareUser = userRepository.findOne(hookup.getUserBId());
                if (!hookupList.contains(compareUser)) {
                    hookupList.add(compareUser);
                }
            }
        }

        for (Hookup hookup : hookupRepository.findAllByUserBId(user.getId())) {
            if (hookup.isPaired() == paired) {
                User compareUser = userRepository.findOne(hookup.getUserAId());
                if (!hookupList.contains(compareUser)) {
                    hookupList.add(compareUser);
                }
            }
        }

        return hookupList;
    }

    public void AddHookupPair(User userA, User userB) {

        if(findHookupPair(userA, userB) != null) {
            return;
        }

//        // check if this pair is already in db (check in both direction UserA-UserB and UserB-UserA
//        for (Hookup hookup : hookupRepository.findAllByUserAId(userA.getId())) {
//            if (hookup.getUserBId() == userB.getId()) {
//                return;
//            }
//        }
//
//        for (Hookup hookup : hookupRepository.findAllByUserBId(userB.getId())) {
//            if (hookup.getUserAId() == userA.getId()) {
//                return;
//            }
//        }

        Date now = new Date();

        Hookup hookup = new Hookup();
        hookup.setUserAId(userA.getId());
        hookup.setUserBId(userB.getId());
        hookup.setHookupPairCreated(now);
        hookupRepository.save(hookup);
    }


    public Hookup findHookupPair(User userA, User userB) {
        Hookup retVal = null;

        List<Hookup> hookupPairsForUserA = hookupRepository.findAllByUserAId(userA.getId());
        for (Hookup hookup : hookupPairsForUserA) {
            User compareUser = userRepository.findOne(hookup.getUserBId());
            if (compareUser != null && compareUser.getId() == userB.getId()) {
                return hookup;
            }
        }

        List<Hookup> hookupPairsForUserB = hookupRepository.findAllByUserBId(userA.getId());
        for (Hookup hookup : hookupPairsForUserB) {
            User compareUser = userRepository.findOne(hookup.getUserAId());
            if (compareUser != null && compareUser.getId() == userB.getId()) {
                return hookup;
            }
        }

        return retVal;
    }

}
