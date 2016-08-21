package rs.hookupspring.springweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import rs.hookupspring.dao.UserRepository;
import rs.hookupspring.entity.User;

/**
 * Created by Bandjur on 8/20/2016.
 */
@Controller
@RequestMapping("/firebaseUser")
public class FirebaseUserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {

        model.addAttribute("message", "Hello world from firebaseUserController. There are " + userRepository.findAll().size() + " users in the database. !");
        return "hello";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@RequestParam(value="email") String email,
                             @RequestParam(value="uid") String uid,
                             @RequestParam(value="latitude") String latitude,
                             @RequestParam(value="longitude") String longitude,
                             @RequestParam(value="token", required = false) String token) {
        User user = userRepository.findByFirebaseUID(uid);

        if(user == null) {
            user = new User();
            user.setEmail(email);
            user.setFirebaseUID(uid);
            user.setLatitude(Double.parseDouble(latitude));
            user.setLongitude(Double.parseDouble(longitude));
            if(token != null && !token.isEmpty()) {
                user.setFirebaseInstaceToken(token);
            }

            userRepository.save(user);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{uid}/updateToken", method = RequestMethod.POST)
    public void updateToken(@PathVariable String uid, @RequestParam(value="token") String token) {
        User user = userRepository.findByFirebaseUID(uid);

        if(user != null) {
            user.setFirebaseInstaceToken(token);
            userRepository.save(user);
        }
    }

    @RequestMapping(value = "/{uid}/updateLocation", method = RequestMethod.POST)
    public void updateLocation(@PathVariable String uid) {

    }
}
