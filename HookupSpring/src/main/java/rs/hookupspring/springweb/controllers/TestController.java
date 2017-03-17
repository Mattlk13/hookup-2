package rs.hookupspring.springweb.controllers;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rs.hookupspring.common.enums.Enums;
import rs.hookupspring.dao.UserActivitiesRepository;
import rs.hookupspring.dao.UserBasicInfoRepository;
import rs.hookupspring.dao.UserPsychologyRepository;
import rs.hookupspring.dao.UserRepository;
import rs.hookupspring.entity.*;
import rs.hookupspring.springweb.dto.UserPersonalizationDto;
import rs.hookupspring.springweb.services.FirebaseNotificationService;
import rs.hookupspring.springweb.services.LocationsDistanceService;
import rs.hookupspring.springweb.services.UserHookupService;
import rs.hookupspring.springweb.services.WekaMiningService;
import weka.classifiers.Classifier;
import weka.core.Instance;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by Bandjur on 8/20/2016.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    private static final Logger logger = Logger.getLogger(TestController.class);

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

    @Autowired
    private FirebaseNotificationService firebaseNotificationService;

    @Transactional
    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {

        model.addAttribute("message", "Hello world from test controller. There are " + userRepository.findAll().size() + " users in the database. !");

        User me = userRepository.findOne(4624);
        User randomPerson = userRepository.findOne(3687);
//
        firebaseNotificationService.sendFcmDataTest(me, randomPerson, true);
        firebaseNotificationService.sendFcmDataTest(randomPerson, me, true);

        for(User u : userRepository.findAll()) {
            if (u.getFirebaseUID() == null || u.getFirebaseUID().equals("")) {
                u.setFirebaseUID(UUID.randomUUID().toString());
                userRepository.save(u);
            }
        }

//        firebaseNotificationService.sendFcmDataTest(me, randomPerson, false);



//        wekaMiningService.createSingleInstanceDataset();

//        try {
//            notificationService.httpApachePostExample();
//        } catch (Exception e) {
//            String exceptionMessage = e.getMessage();
//        }

        return "hello";
    }


    @RequestMapping(value = "/insertExperimentData", method = RequestMethod.GET)
    public String customMethod(ModelMap model) {
        StringBuilder message = new StringBuilder();
//        User userTest = userRepository.findOne(8);
//        double distance = 0.0;

//        for (User hookup : userTest.getHookups()) {
//            distance = locationsDistanceService.distance(userTest.getLatitude(),
//                    userTest.getLongitude(), hookup.getLatitude(), hookup.getLongitude(), 'K');
//
//            message.append("For user (id = " + hookup.getId() + ")" +
//                    ", DISTANCE (KM): " + distance + "\n");
//        }


        wekaMiningService.insertExperimentDataUsersInDb();
        message.append("wooohooooo");

        model.addAttribute("message", message);
        return "hello";
    }

    @Transactional
    @RequestMapping(value = "/updateUserLocations", method = RequestMethod.GET)
    public void updateUserLocations(ModelMap model) {

        try {

            File f = new File("C:\\Users\\Bandjur\\Desktop\\randomLocations.txt");

            List<String> lines = FileUtils.readLines(f, "UTF-8");
            List<User> users = userRepository.findAll();

            int counter = 0;
            for (String line : lines) {
                String [] parsedLine = line.split(",");
                double latitude = Double.parseDouble(parsedLine[1]);
                double longitude = Double.parseDouble(parsedLine[3]);

                if(counter <= users.size() - 1 && users.get(counter) != null) {
                    User user = users.get(counter);

                    user.setLongitude(longitude);
                    user.setLatitude(latitude);
                    userRepository.save(user);
                }

                counter++;
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

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

//    @RequestMapping(value = "/jpa", method = RequestMethod.GET)
//    public String jpaTest(ModelMap model) {
//
////        User bakiUser = userRepository.findOne(8);
//        User newUser = new User();
//        newUser.setFirstname("Imenko");
//        newUser.setLastname("Prezimenic");
//        newUser.setEmail("imenko@gmail.com");
//
//        User user = userRepository.save(newUser);
//
//        UserBasicInfo basicInfo = new UserBasicInfo();
//        basicInfo.setImprelig(4);
//        basicInfo.setCareer(Enums.Career.Engineer);
//        basicInfo.setField(Enums.Field.Engineering);
//        basicInfo.setRace(Enums.Race.White);
////        basicInfo.setGoOut(Enums.GoOut.AFewTimesInAWeek);
//        basicInfo.setUser(user);
//        basicInfo = basicInfoRepository.save(basicInfo);
//        user.setBasicInfo(basicInfo);
//        user = userRepository.save(newUser);
//
//
//        UserActivities userActivities = new UserActivities();
//        userActivities.setArt(8);
//        userActivities.setClubbing(10);
//        userActivities.setGaming(7);
//        userActivities.setConcerts(8);
//        userActivities.setDining(9);
//        userActivities.setHiking(8);
//        userActivities.setMusic(10);
//        // TODO: rest of activities
//        userActivities.setUser(user);
//        userActivities = activitiesRepository.save(userActivities);
//        user.setActivities(userActivities);
//        user = userRepository.save(user);
//
//
//        UserPsychology userPsychology = new UserPsychology();
//
//        userPsychology.setAmbA(20);
//        userPsychology.setAttrA(19.5);
//        userPsychology.setSincA(20.5);
//        userPsychology.setFunA(10);
//        userPsychology.setIntelA(15);
//        userPsychology.setSharA(15);
//        userPsychology.setUser(user);
//        userPsychology = psychologyRepository.save(userPsychology);
//        user.setPsychology(userPsychology);
//
//        user = userRepository.save(user);
//
//
//        model.addAttribute("message", "uuu fuck yeah .. basic info exist");
//
//
////        if (newUp.getBasicInfo() != null) {
////            model.addAttribute("message", "uuu fuck yeah .. basic info exist");
////        }
//
//        return "hello";
//    }

    @Transactional
    @RequestMapping(value = "/classifyTestEntries", method = RequestMethod.GET)
    public String jpaGetTest(ModelMap model) {
        List<User> males = null;
        List<User> females = null;
        Classifier randomForest = null;
        try {
            males = userRepository.findAllByGender(Enums.Gender.Male);
            females = userRepository.findAllByGender(Enums.Gender.Female);

            randomForest = (Classifier) weka.core.SerializationHelper.read("E:\\!FAKS\\!MSC\\Speed hookup\\20170216-final\\weka\\random-forest-model.model");
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        for (User theMale : males) {
            int counter = 0;
            int positive = 0;
            int negative = 0;

            for (User female : females) {

                double result = 0.0;
                Instance instance = wekaMiningService.createPairEntryInstance(theMale, female);
                if (instance != null) {
                    try {
                        instance.dataset().setClassIndex(instance.dataset().numAttributes() - 1);
                        result = randomForest.classifyInstance(instance);
                        counter++;
                        if (result == 1.0) {
                            positive++;
                        } else {
                            negative++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            logger.info("Results for " + theMale.getFirstname() + " " + theMale.getLastname() + " Ukupno = " + counter + " (positive: " + positive + ", negative: " + negative + ")");
        }
        model.addAttribute("message", "bleja");

        return "hello";
    }


    @RequestMapping(value = "/requestEntity", method = RequestMethod.POST, consumes = "application/json")
    public String requestEntityTest(RequestEntity<UserPersonalizationDto> requestEntity, ModelMap model) {

        UserPersonalizationDto userTest = requestEntity.getBody();

        model.addAttribute("message", "Welcome user uid =  " + userTest.getUid() + ", sports: " + userTest.getActivities().getSports());

        return "hello";
    }

}
