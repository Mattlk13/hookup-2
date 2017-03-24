package rs.hookupspring.springweb.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import rs.hookupspring.common.enums.Enums;
import rs.hookupspring.common.utils.CommonUtils;
import rs.hookupspring.dao.HookupRepository;
import rs.hookupspring.dao.UserRepository;
import rs.hookupspring.entity.Hookup;
import rs.hookupspring.entity.User;
import rs.hookupspring.entity.UserBasicInfo;
import rs.hookupspring.springweb.dto.ResponseUserDto;
import rs.hookupspring.springweb.dto.UserPersonalizationDto;
import rs.hookupspring.springweb.services.FirebaseNotificationService;
import rs.hookupspring.springweb.services.LocationsDistanceService;
import rs.hookupspring.springweb.services.UserHookupService;
import rs.hookupspring.springweb.services.WekaMiningService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bandjur on 8/20/2016.
 */
@Controller
@RequestMapping("/firebaseUser")
public class FirebaseUserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HookupRepository hookupRepository;

    @Autowired
    private LocationsDistanceService locationsDistanceService;

    @Autowired
    private FirebaseNotificationService firebaseNotificationService;

    @Autowired
    private UserHookupService userHookupService;

    @Autowired
    private WekaMiningService wekaMiningService;

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {

        model.addAttribute("message", "Hello world from firebaseUserController. There are " + userRepository.findAll().size() + " users in the database. !");
        return "hello";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@RequestParam(value="email") String email,
                             @RequestParam(value="uid") String uid,
                             @RequestParam(value="firstname") String firstname,
                             @RequestParam(value="lastname") String lastname,
                             @RequestParam(value="latitude", required = false) String latitude,
                             @RequestParam(value="longitude", required = false) String longitude,
                             @RequestParam(value="token", required = false) String token,
                             @RequestParam(value="birthday") String birthday,
                             @RequestParam(value="gender") String gender,
                             @RequestParam(value="city", required = false) String city,
                             @RequestParam(value="aboutMe", required = false) String aboutMe,
                             @RequestParam(value="country", required = false) String country) {
        User user = userRepository.findByFirebaseUID(uid);

        if(user == null) {
            user = new User();
            user.setEmail(email);
            user.setFirebaseUID(uid);
            user.setFirstname(firstname);
            user.setLastname(lastname);
            user.setLatitude(Double.parseDouble(latitude));
            user.setLongitude(Double.parseDouble(longitude));
            try {
                user.setBirthDate(new SimpleDateFormat("dd.MM.yyyy").parse(birthday));
                user.setAge(new Date().getYear() - user.getBirthDate().getYear());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            user.setAboutMe(aboutMe);
            user.setCity(city);
            user.setGender(Enums.Gender.valueOf(gender));
            if(!country.isEmpty()) {
                user.setCountry(country);
            }
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
            // TODO if token is different send warning email about new login via another device
            user.setFirebaseInstaceToken(token);
            userRepository.save(user);
        }
    }


    @RequestMapping(value = "/updateLocation", method = RequestMethod.POST)
    public void tryUpdateLocation(@RequestParam(value="latitude") String latitude,
                                  @RequestParam(value="longitude") String longitude) {
        int a = 1;
    }

    @RequestMapping(value = "/{uid}/updateLocation", method = RequestMethod.POST)
    public void updateLocation(@PathVariable String uid,
                               @RequestParam(value="latitude") String latitude,
                               @RequestParam(value="longitude") String longitude) {

        User user = userRepository.findByFirebaseUID(uid);
        user.setLatitude(Double.parseDouble(latitude));
        user.setLongitude(Double.parseDouble(longitude));
        userRepository.save(user);

        double distance = Double.MAX_VALUE;
        List<User> nearbyHookups = new ArrayList<User>();

        List<User> hookups = userHookupService.getHookupList(user, false);
        for (User hookup : hookups) {
            distance = locationsDistanceService.distance(user.getLatitude(),
                    user.getLongitude(), hookup.getLatitude(), hookup.getLongitude(), 'K');

            if(distance < 0.5 && distance > 0.0) {
                nearbyHookups.add(hookup);
            }
        }

        for(User nearbyHookup: nearbyHookups) {
            firebaseNotificationService.sendHookupNotification(user, nearbyHookup);
            firebaseNotificationService.sendHookupNotification(nearbyHookup, user);
        }
    }


    @RequestMapping(value = "/updatePersonalization", method = RequestMethod.POST, consumes = "application/json")
    public void requestEntityTest(RequestEntity<UserPersonalizationDto> requestEntity, ModelMap model) {

        UserPersonalizationDto userPersonalizationDto = requestEntity.getBody();
        userHookupService.updateUserPersonalizationData(userPersonalizationDto);

        User user = userRepository.findByFirebaseUID(userPersonalizationDto.getUid());

        if(user != null) {
            List<User> partners;
            if(user.getGender() == Enums.Gender.Male) {
                partners = userRepository.findAllByGender(Enums.Gender.Female);
            }
            else {
                partners = userRepository.findAllByGender(Enums.Gender.Male);
            }

            for(User partner : partners) {
                if(wekaMiningService.shouldUsersPair(user, partner)) {
                    Hookup hookup = new Hookup();
                    hookup.setRecommended(true);
                    hookup.setUserAId(user.getId());
                    hookup.setUserBId(partner.getId());
                    hookup.setHookupPairCreated(new Date());
                    hookupRepository.save(hookup);
                }
            }
        }
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers(ModelMap model) {
        List<User> users = userRepository.findAll();
        List<User> returnUsers = new ArrayList<User>();

        for (User u : users) {
            returnUsers.add(getUser(u));
        }

        return new ResponseEntity<List<User>>(returnUsers, HttpStatus.OK);
    }

//    @RequestMapping(value = "/{uid}/friends", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<User>> getAllFriends(@PathVariable String uid, ModelMap model) {
////        User currentUser = userRepository.findByFirebaseUID("pV3MOTu7yigZa0KRrzsLJM2ztpw1");
//        User currentUser = userRepository.findByFirebaseUID(uid);
//        List<User> returnUsers = new ArrayList<User>();
//
//        for (User u : userHookupService.getHookupList(currentUser, true)) {
//            returnUsers.add(getUser(u));
//        }
//
//        return new ResponseEntity<List<User>>(returnUsers, HttpStatus.OK);
//    }

    @RequestMapping(value = "/{uid}", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseUserDto> getCurrentUserDetails(@PathVariable String uid) {
        ResponseUserDto retVal = null;
        User currentUser = userRepository.findByFirebaseUID(uid);

        retVal = getUser(currentUser, null);

        return new ResponseEntity<ResponseUserDto>(retVal, HttpStatus.OK);
    }

    @RequestMapping(value = "/{uid}/friends", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResponseUserDto>> getAllFriends(@PathVariable String uid, ModelMap model) {
        User currentUser = userRepository.findByFirebaseUID(uid);
        List<ResponseUserDto> returnUsers = new ArrayList<ResponseUserDto>();

        for (User u : userHookupService.getHookupList(currentUser, true)) {
            returnUsers.add(getUser(u, currentUser));
        }

        return new ResponseEntity<List<ResponseUserDto>>(returnUsers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{uid}/pendingFriends", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResponseUserDto>> getAllPendingFriends(@PathVariable String uid, ModelMap model) {
        User currentUser = userRepository.findByFirebaseUID(uid);
        List<ResponseUserDto> returnUsers = new ArrayList<ResponseUserDto>();

        for (User u : userHookupService.getPendingHookups(currentUser)) {
            returnUsers.add(getUser(u, currentUser));
        }

        return new ResponseEntity<List<ResponseUserDto>>(returnUsers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{uid}/like/{nonFriend}", method = RequestMethod.POST)
    public void likePerson(@PathVariable String uid, @PathVariable String nonFriend) {

        User currentUser = userRepository.findByFirebaseUID(uid);
        User nonFriendUser = userRepository.findByFirebaseUID(nonFriend);

        Hookup hookup = userHookupService.findHookupPair(currentUser, nonFriendUser);

        if(hookup == null) {
            hookup = new Hookup();
            hookup.setUserAId(currentUser.getId());
            hookup.setUserBId(nonFriendUser.getId());
            hookup.setHookupPairCreated(new Date());
            hookup.setHookupPositiveResponseCount(1);
            hookup.setRecommended(false);
            hookup.setUserAResponse(true);

            hookupRepository.save(hookup);
        }
    }

    @RequestMapping(value = "/{uid}/unfriend/{enemyUid}", method = RequestMethod.POST)
    public void unfriend(@PathVariable String uid, @PathVariable String enemyUid) {

        User currentUser = userRepository.findByFirebaseUID(uid);
        User enemy = userRepository.findByFirebaseUID(enemyUid);

        Hookup hookupPair = userHookupService.findHookupPair(currentUser, enemy);
        hookupRepository.delete(hookupPair);

        // TODO update enemy user android database
    }

    @RequestMapping(value = "/{uid}/non-friends", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResponseUserDto>> getNonFriends(@PathVariable String uid) {
        List<ResponseUserDto> returnUsers = new ArrayList<ResponseUserDto>();
        User currentUser = userRepository.findByFirebaseUID(uid);
        List<User> resultUsers = new ArrayList<User>();

        List<User> friends = userHookupService.getHookupList(currentUser, true);
        List<User> pendingFriends = userHookupService.getPendingHookups(currentUser);

        if(currentUser.getGender() == Enums.Gender.Male) {
            resultUsers = userRepository.findAllByGender(Enums.Gender.Female);
        }
        else {
            resultUsers = userRepository.findAllByGender(Enums.Gender.Male);
        }

        resultUsers.removeAll(friends);
        resultUsers.removeAll(pendingFriends);

        for(User user: resultUsers) {
            returnUsers.add(getUser(user, null));
        }

        return new ResponseEntity<List<ResponseUserDto>>(returnUsers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{uid}/recommended", method = RequestMethod.GET)
    public String getAllRecommendedPersons(@PathVariable String uid, ModelMap model) {
        StringBuilder message = new StringBuilder();

        User currentUser = userRepository.findByFirebaseUID(uid);

        for (User u : userHookupService.getRecommendedHookupUserList(currentUser)) {
            message.append(u.getLatitude() + ", " + u.getLongitude() + " </br>");
        }

        model.addAttribute("message", message);

        return "hello";
    }

    private ResponseUserDto getUser(User u, User currentUser) {
        ResponseUserDto user = new ResponseUserDto();

        user.setFirebaseUID(u.getFirebaseUID());
        user.setFirstname(u.getFirstname());
        user.setLastname(u.getLastname());
        user.setAge(u.getAge());
        user.setCity(u.getCity());
        user.setCountry(u.getCountry());

        if(u.getAboutMe() != null) {
            user.setAboutMe(u.getAboutMe());
        }

        if(u.getBasicInfo() != null && u.getActivities() != null && u.getPsychology() !=null) {
            user.setProfileComplete(true);

            user.setNearestHookupDistnace(getNearestUnpairedHookupDistance(u));
            user.setUnpairedRecommendationsCounter(userHookupService.getHookupList(u, false).size());
        }
        else {
            user.setProfileComplete(false);
        }

        if (u.getBasicInfo()!=null) {
            UserBasicInfo basicInfo = new UserBasicInfo();
            basicInfo.setCareer(u.getBasicInfo().getCareer());
            user.setBasicInfo(basicInfo);
        }

        if(currentUser != null) {
            Hookup hookupPair = userHookupService.findHookupPair(u, currentUser);

            if (hookupPair.getHookupPairedDate() != null) {
                user.setFriendsDate(hookupPair.getHookupPairedDate());
            }

            if (hookupPair.getHookupRequestDate() != null) {
                user.setNotificationReceivedDate(hookupPair.getHookupRequestDate());
            }
        }

        return user;
    }

    private User getUser(User u) {
        User user = new User();
        user.setFirebaseUID(u.getFirebaseUID());
        user.setFirstname(u.getFirstname());
        user.setLastname(u.getLastname());
        user.setAge(u.getAge());
        user.setCity(u.getCity());
        user.setCountry(u.getCountry());

        if(u.getAboutMe() != null) {
            user.setAboutMe(u.getAboutMe());
        }

        UserBasicInfo basicInfo = new UserBasicInfo();
        basicInfo.setCareer(u.getBasicInfo().getCareer());
        user.setBasicInfo(basicInfo);

        return user;
    }

    private double getNearestUnpairedHookupDistance(User user) {
        double distance = Double.MAX_VALUE;
        List<User> nearbyHookups = new ArrayList<User>();

        List<User> hookups = userHookupService.getHookupList(user, false);
        for (User hookup : hookups) {
            double newDistance = locationsDistanceService.distance(user.getLatitude(),
                    user.getLongitude(), hookup.getLatitude(), hookup.getLongitude(), 'K');

            if(newDistance<distance) {
                distance = newDistance;
            }

        }

        return CommonUtils.round(distance, 2);
    }
}
