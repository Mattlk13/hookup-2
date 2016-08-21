package rs.hookupspring.springweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rs.hookupspring.dao.UserRepository;
import rs.hookupspring.springweb.services.FirebaseNotificationService;

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

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {

        model.addAttribute("message", "Hello world from test controller. There are " + userRepository.findAll().size() + " users in the database. !");


        try {
//            notificationService.asyncHttpTest();
            notificationService.httpApachePostExample();
        }
        catch (Exception e) {
            String exceptionMessage = e.getMessage();

        }

        return "hello";
    }


    @RequestMapping(value="/test", method = RequestMethod.GET)
    public String customMethod(ModelMap model) {

        model.addAttribute("message", "Custom method!");
        return "hello";
    }
}
