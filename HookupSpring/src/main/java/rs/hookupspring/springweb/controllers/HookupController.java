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
import rs.hookupspring.springweb.services.UserHookupService;

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

    @RequestMapping(value = "/updateResponse", method = RequestMethod.POST)
    public void updateToken(@RequestParam(value="currentUserUID") String currentUserUID,
                            @RequestParam(value="hookupPersonUID") String hookupPersonUID) {
        User user = userRepository.findByFirebaseUID(currentUserUID);
        User hookupPerson = userRepository.findByFirebaseUID(hookupPersonUID);

        Hookup hookup = userHookupService.findHookupPair(user, hookupPerson);

        if(hookup!= null) {
            int positiveResponseCount = hookup.getHookupPositiveResponseCount() + 1;
            hookup.setHookupPositiveResponseCount(positiveResponseCount);

            if(positiveResponseCount==2) {
                hookup.setPaired(true);
            }

            hookupRepository.save(hookup);
        }

    }

}
