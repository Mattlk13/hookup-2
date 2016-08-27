package rs.hookupspring.springweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rs.hookupspring.dao.UserRepository;
import rs.hookupspring.entity.User;
import rs.hookupspring.springweb.services.FirebaseNotificationService;
import rs.hookupspring.springweb.services.LocationsDistanceService;
import rs.hookupspring.springweb.services.UserHookupService;

import java.util.List;

/**
 * Created by Bandjur on 8/20/2016.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FirebaseNotificationService notificationService;

    @Autowired
    private LocationsDistanceService locationsDistanceService;

    @Autowired
    private UserHookupService userHookupService;

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {

        model.addAttribute("message", "Hello world from test controller. There are " + userRepository.findAll().size() + " users in the database. !");


        try {
//            notificationService.asyncHttpTest();
            notificationService.httpApachePostExample();
        } catch (Exception e) {
            String exceptionMessage = e.getMessage();

        }

        return "hello";
    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String customMethod(ModelMap model) {
        StringBuilder message = new StringBuilder();
        User userTest = userRepository.findOne(8);
        double distance = 0.0;

//        for (User hookup : userTest.getHookups()) {
//            distance = locationsDistanceService.distance(userTest.getLatitude(),
//                    userTest.getLongitude(), hookup.getLatitude(), hookup.getLongitude(), 'K');
//
//            message.append("For user (rowId = " + hookup.getRowId() + ")" +
//                    ", DISTANCE (KM): " + distance + "\n");
//        }

        model.addAttribute("message", message);
        return "hello";
    }

    @RequestMapping(value = "/testHookupUpdateList", method = RequestMethod.GET)
    public String updateHookupListForUser(ModelMap model) {
        String message = " Mjau....   ";
        User userTest = userRepository.findOne(8);

        User hookupOne = userRepository.findOne(9);
        User hookupTwo = userRepository.findOne(10);
        User hookupThree = userRepository.findOne(11);

//        if(userHookupService.findHookupPair(userTest, hookupOne) == null) {
//            userHookupService.AddHookupPair(userTest, hookupOne);
//        }
//
//        if(userHookupService.findHookupPair(hookupOne,userTest) == null) {
//            message += "TREBALO BI DA POSTOJI HOOK UP PAIR AKO SAM GA VEC DODAO MALO PRE!";
//        }

        userHookupService.AddHookupPair(userTest, hookupOne);
        userHookupService.AddHookupPair(userTest, hookupTwo);
        userHookupService.AddHookupPair(userTest, hookupThree);

        userHookupService.AddHookupPair(hookupOne, userTest);
        userHookupService.AddHookupPair(hookupTwo, userTest);
        userHookupService.AddHookupPair(hookupThree, userTest);

        model.addAttribute("message", message);
        return "hello";
    }
}
