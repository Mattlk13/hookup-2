package rs.hookupspring.springweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import rs.hookupspring.dao.HookupRepository;
import rs.hookupspring.dao.UserRepository;
import rs.hookupspring.entity.Hookup;
import rs.hookupspring.entity.User;
import rs.hookupspring.springweb.services.FirebaseNotificationService;
import rs.hookupspring.springweb.services.UserHookupService;

import java.util.Date;

/**
 * Created by Bandjur on 8/28/2016.
 */
@Controller
@RequestMapping("/hookup")
public class HookupController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HookupRepository hookupRepository;

    @Autowired
    private UserHookupService userHookupService;

    @Autowired
    private FirebaseNotificationService firebaseNotificationService;

    @RequestMapping(value = "/updateResponse", method = RequestMethod.POST)
    public void updateToken(@RequestParam(value="currentUserUID") String currentUserUID,
                            @RequestParam(value="hookupPersonUID") String hookupPersonUID) {
        User user = userRepository.findByFirebaseUID(currentUserUID);
        User hookupPerson = userRepository.findByFirebaseUID(hookupPersonUID);

        Hookup hookup = userHookupService.findHookupPair(user, hookupPerson);

        if(hookup!= null) {
            int positiveResponseCount = hookup.getHookupPositiveResponseCount() + 1;
            hookup.setHookupPositiveResponseCount(positiveResponseCount);

            if(hookup.getUserAId() ==  user.getId()) {
                hookup.setUserAResponse(true);
            }
            else {
                hookup.setUserBResponse(true);
            }

            if(positiveResponseCount==2) {
                hookup.setPaired(true);
                hookup.setHookupPairedDate(new Date());
                firebaseNotificationService.sendFriendsNotification(user, hookupPerson);
                firebaseNotificationService.sendFriendsNotification(hookupPerson, user);
            }

            hookupRepository.save(hookup);
        }

    }



}
