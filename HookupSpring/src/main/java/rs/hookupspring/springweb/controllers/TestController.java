package rs.hookupspring.springweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rs.hookupspring.common.enums.Enums;
import rs.hookupspring.dao.UserActivitiesRepository;
import rs.hookupspring.dao.UserBasicInfoRepository;
import rs.hookupspring.dao.UserPsychologyRepository;
import rs.hookupspring.dao.UserRepository;
import rs.hookupspring.entity.UserActivities;
import rs.hookupspring.entity.UserBasicInfo;
import rs.hookupspring.entity.User;
import rs.hookupspring.entity.UserPsychology;
import rs.hookupspring.springweb.services.FirebaseNotificationService;
import rs.hookupspring.springweb.services.LocationsDistanceService;
import rs.hookupspring.springweb.services.UserHookupService;
import rs.hookupspring.springweb.services.WekaMiningService;

/**
 * Created by Bandjur on 8/20/2016.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBasicInfoRepository basicInfoRepository;

    @Autowired
    private UserActivitiesRepository activitiesRepository;

    @Autowired
    private UserPsychologyRepository psychologyRepository;

    @Autowired
    private FirebaseNotificationService notificationService;

    @Autowired
    private LocationsDistanceService locationsDistanceService;

    @Autowired
    private UserHookupService userHookupService;

    @Autowired
    private WekaMiningService wekaMiningService;

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {

//        model.addAttribute("message", "Hello world from test controller. There are " + userRepository.findAll().size() + " users in the database. !");

//        wekaMiningService.Test();
        String result = wekaMiningService.readArffData();
        model.addAttribute("message", result);

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
//        User userTest = userRepository.findOne(8);
//        double distance = 0.0;

//        for (User hookup : userTest.getHookups()) {
//            distance = locationsDistanceService.distance(userTest.getLatitude(),
//                    userTest.getLongitude(), hookup.getLatitude(), hookup.getLongitude(), 'K');
//
//            message.append("For user (rowId = " + hookup.getId() + ")" +
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

    @RequestMapping(value = "/jpa", method = RequestMethod.GET)
    public String jpaTest(ModelMap model) {

//        User bakiUser = userRepository.findOne(8);
        User newUser = new User();
        newUser.setFirstname("Imenko");
        newUser.setLastname("Prezimenic");
        newUser.setEmail("imenko@gmail.com");

        User user = userRepository.save(newUser);

        UserBasicInfo basicInfo = new UserBasicInfo();
        basicInfo.setImprelig(0);
        basicInfo.setImprace(4);
        basicInfo.setCareer(Enums.Career.Engineer);
        basicInfo.setField(Enums.Field.Engineering);
        basicInfo.setRace(Enums.Race.White);
        basicInfo.setGoOut(Enums.GoOut.AFewTimesInAWeek);
        basicInfo.setUser(user);
        basicInfo = basicInfoRepository.save(basicInfo);
        user.setBasicInfo(basicInfo);
        user = userRepository.save(newUser);


        UserActivities userActivities = new UserActivities();
        userActivities.setArt(8);
        userActivities.setClubbing(10);
        userActivities.setGaming(7);
        userActivities.setConcerts(8);
        userActivities.setDining(9);
        userActivities.setHiking(8);
        userActivities.setMusic(10);
        // TODO: rest of activities
        userActivities.setUser(user);
        userActivities = activitiesRepository.save(userActivities);
        user.setActivities(userActivities);
        user = userRepository.save(user);


        UserPsychology userPsychology = new UserPsychology();

        userPsychology.setAmbA(20);
        userPsychology.setAttrA(20);
        userPsychology.setSincA(20);
        userPsychology.setFunA(10);
        userPsychology.setIntelA(15);
        userPsychology.setSharA(15);
        userPsychology.setUser(user);
        userPsychology = psychologyRepository.save(userPsychology);
        user.setPsychology(userPsychology);

        user = userRepository.save(user);


        model.addAttribute("message", "uuu fuck yeah .. basic info exist");


//        if (newUp.getBasicInfo() != null) {
//            model.addAttribute("message", "uuu fuck yeah .. basic info exist");
//        }

        return "hello";
    }

    @RequestMapping(value = "/jpaGet", method = RequestMethod.GET)
    public String jpaGetTest(ModelMap model) {

//        UserPersonalization newUp = userPersonalizationRepository.findOne(52);
//
//        if (newUp.getBasicInfo() != null) {
//            model.addAttribute("message", "uuu fuck yeah .. basic info exist");
//        }
//        else {
//            model.addAttribute("message", "meeeeeeeh... null je");
//        }

        return "hello";
    }


}
