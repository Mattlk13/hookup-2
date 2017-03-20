package rs.hookupspring.springweb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hookupspring.common.enums.Enums;
import rs.hookupspring.dao.*;
import rs.hookupspring.entity.*;
import rs.hookupspring.springweb.dto.UserActivitiesDto;
import rs.hookupspring.springweb.dto.UserBasicInfoDto;
import rs.hookupspring.springweb.dto.UserPersonalizationDto;
import rs.hookupspring.springweb.dto.UserPsychologyDto;

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
    private UserBasicInfoRepository basicInfoRepository;

    @Autowired
    private UserActivitiesRepository activitiesRepository;

    @Autowired
    private UserPsychologyRepository psychologyRepository;

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

    public List<User> getPendingHookups(User user) {
        List<User> hookupList = new ArrayList<User>();

        for (Hookup hookup : hookupRepository.findAllByUserAId(user.getId())) {
            if (!hookup.isPaired() && hookup.getHookupRequestDate() != null
                    && !hookup.isUserAResponse()) {
                User compareUser = userRepository.findOne(hookup.getUserBId());
                if (!hookupList.contains(compareUser)) {
                    hookupList.add(compareUser);
                }
            }
        }

        for (Hookup hookup : hookupRepository.findAllByUserBId(user.getId())) {
            if (!hookup.isPaired() && hookup.getHookupRequestDate() != null
                    && !hookup.isUserBResponse()) {
                User compareUser = userRepository.findOne(hookup.getUserAId());
                if (!hookupList.contains(compareUser)) {
                    hookupList.add(compareUser);
                }
            }
        }

        return hookupList;
    }

    public List<User> getRecommendedHookupUserList(User user) {
        List<User> hookupList = new ArrayList<User>();

        for (Hookup hookup : hookupRepository.findAllByUserAId(user.getId())) {
            if (!hookup.isPaired() && hookup.isRecommended()) {
                User compareUser = userRepository.findOne(hookup.getUserBId());
                if (!hookupList.contains(compareUser)) {
                    hookupList.add(compareUser);
                }
            }
        }

        for (Hookup hookup : hookupRepository.findAllByUserBId(user.getId())) {
            if (!hookup.isPaired() && hookup.isRecommended()) {
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

    @Transactional
    public void updateUserPersonalizationData(UserPersonalizationDto userPersonalizationDto) {

        User user = userRepository.findByFirebaseUID(userPersonalizationDto.getUid());

        if (user != null) {
            updateUserBasicInfo(user, userPersonalizationDto.getBasicInfo());
            updateUserActivities(user, userPersonalizationDto.getActivities());
            updateUserPsychology(user, userPersonalizationDto.getPsychology());
        }

    }

    private void updateUserBasicInfo(User user, UserBasicInfoDto basicInfoDto) {
        UserBasicInfo basicInfo = null;

        if(user.getBasicInfo() == null) {
            basicInfo = new UserBasicInfo();
            basicInfoRepository.save(basicInfo);
            basicInfo.setUser(user);
            userRepository.save(user);
        }
        else {
            basicInfo = user.getBasicInfo();
        }

        basicInfo.setImprelig(basicInfoDto.getImprelig());
        basicInfo.setImprace(basicInfoDto.getImprace());
        basicInfo.setRace(Enums.Race.values()[basicInfoDto.getRace()]);
        basicInfo.setCareer(Enums.Career.values()[basicInfoDto.getCareer()]);
        basicInfo.setField(Enums.Field.values()[basicInfoDto.getField()]);
        basicInfoRepository.save(basicInfo);
        user.setBasicInfo(basicInfo);
        userRepository.save(user);
    }

    private void updateUserActivities(User user, UserActivitiesDto activitiesDto) {
        UserActivities activities = null;

        if(user.getActivities() == null) {
            activities = new UserActivities();
            activitiesRepository.save(activities);
            activities.setUser(user);
            userRepository.save(user);
        }
        else {
            activities = user.getActivities();
        }

        activities.setDining(activitiesDto.getDining());
        activities.setHiking(activitiesDto.getHiking());
        activities.setClubbing(activitiesDto.getClubbing());
        activities.setGaming(activitiesDto.getGaming());
        activities.setYoga(activitiesDto.getYoga());
        activities.setArt(activitiesDto.getArt());
        activities.setConcerts(activities.getArt());
        activities.setExcersice(activitiesDto.getExcersice());
        activities.setMovies(activitiesDto.getMovies());
        activities.setMuseums(activitiesDto.getMuseums());
        activities.setMusic(activitiesDto.getMusic());
        activities.setReading(activitiesDto.getReading());
        activities.setShopping(activitiesDto.getShopping());
        activities.setSports(activitiesDto.getSports());
        activities.setTheater(activitiesDto.getTheater());
        activities.setTv(activitiesDto.getTv());
        activities.setTvsports(activitiesDto.getTvsports());
        activitiesRepository.save(activities);
        user.setActivities(activities);
        userRepository.save(user);
    }

    private void updateUserPsychology(User user, UserPsychologyDto psychologyDto) {
        UserPsychology psychology = null;

        if(user.getPsychology() == null) {
            psychology = new UserPsychology();
            psychologyRepository.save(psychology);
            psychology.setUser(user);
            userRepository.save(user);
        }
        else {
            psychology = user.getPsychology();
        }

        psychology.setSharA(psychologyDto.getSharA());
        psychology.setAmbA(psychologyDto.getAmbA());
        psychology.setFunA(psychologyDto.getFunA());
        psychology.setAttrA(psychologyDto.getAttrA());
        psychology.setIntelA(psychologyDto.getIntelA());
        psychology.setSincA(psychologyDto.getSincA());

        psychology.setSharB(psychologyDto.getSharB());
        psychology.setAmbB(psychologyDto.getAmbB());
        psychology.setFunB(psychologyDto.getFunB());
        psychology.setAttrB(psychologyDto.getAttrB());
        psychology.setIntelB(psychologyDto.getIntelB());
        psychology.setSincB(psychologyDto.getSincB());

        psychology.setAmbC(psychologyDto.getAmbC());
        psychology.setFunC(psychologyDto.getFunC());
        psychology.setAttrC(psychologyDto.getAttrC());
        psychology.setIntelC(psychologyDto.getIntelC());
        psychology.setSincC(psychologyDto.getSincC());
        psychologyRepository.save(psychology);
        user.setPsychology(psychology);
        userRepository.save(user);
    }

}
