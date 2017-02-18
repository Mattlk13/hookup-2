package rs.hookupspring.springweb.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import rs.hookupspring.common.SerbianNames;
import rs.hookupspring.common.enums.Enums;
import rs.hookupspring.dao.UserActivitiesRepository;
import rs.hookupspring.dao.UserBasicInfoRepository;
import rs.hookupspring.dao.UserPsychologyRepository;
import rs.hookupspring.dao.UserRepository;
import rs.hookupspring.entity.User;
import rs.hookupspring.entity.UserActivities;
import rs.hookupspring.entity.UserBasicInfo;
import rs.hookupspring.entity.UserPsychology;
import weka.classifiers.Classifier;
import org.springframework.stereotype.Service;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Bandjur on 2/4/2017.
 */

@Service
public class WekaMiningService {

    private static final Logger logger = Logger.getLogger(WekaMiningService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBasicInfoRepository basicInfoRepository;

    @Autowired
    private UserActivitiesRepository activitiesRepository;

    @Autowired
    private UserPsychologyRepository psychologyRepository;

    public void ClassifyInstancesAndTests() {

        try {
            Classifier randomForest = (Classifier) weka.core.SerializationHelper.read("E:\\!FAKS\\!MSC\\Speed hookup\\20170201-wekaSampling-metacost\\apply data\\randomForestAlmostBalanced.model");

            // TODO make test .arff file with attribute 'match' included (the attribute set should be exactly like the one in the commented section
//            Instances unlabeled = new Instances(
//                    new BufferedReader(
//                            new FileReader("E:\\!FAKS\\!MSC\\Speed hookup\\weka\\almost-balanced-not-normalized.arff")));

            Instances unlabeled = new Instances(
                    new BufferedReader(
                            new FileReader("E:\\!FAKS\\!MSC\\Speed hookup\\weka\\bandar_harem_dataset.arff")));

            unlabeled.insertAttributeAt(new Attribute("match", Attribute.NOMINAL), unlabeled.numAttributes());

            unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
//            unlabeled.setClassIndex(68);

            // create copy
            Instances labeled = new Instances(unlabeled);

            // label instances
            for (int i = 0; i < unlabeled.numInstances(); i++) {
                unlabeled.instance(i).classIsMissing();
                double clsLabel = randomForest.classifyInstance(unlabeled.instance(i));
                labeled.instance(i).setClassValue(clsLabel);
            }
            // save labeled data
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("E:\\!FAKS\\!MSC\\Speed hookup\\weka\\weka-classifyResults.arff"));
            writer.write(labeled.toString());
            writer.newLine();
            writer.flush();
            writer.close();

            int counter = 0, paired = 0, unpaired = 0;
            for (Instance i : unlabeled) {
                double v = randomForest.classifyInstance(i);
                if (v == 0.0) {
                    unpaired++;
                } else {
                    paired++;
                }
                System.out.println(++counter + " rezultat: " + v + "\n");
            }

            System.out.println("\n paired = " + paired + ", unpaired = " + unpaired);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateWekaInstance(User male, User female) {

    }

    public String readArffData() {

        StringBuilder sb = new StringBuilder();

        try {
            Instances instances = new Instances(
                    new BufferedReader(
                            new FileReader("E:\\!FAKS\\!MSC\\Speed hookup\\20170216-final\\weka\\male-non-redudant-set.arff")));

            int a = 0;
            for (Instance instance : instances) {
                a++;
//                System.out.println("*******************    " + a + "    ********************");
                logger.info("*******************    " + a + "    ********************");
                sb.append("*******************    " + a + "    ********************\n");

                for (int i = 0; i < instance.numAttributes() - 1; i++) {
                    logger.info("attribute index (" + i + "), attribute name (" + instance.attribute(i).name() + "), value = " + instance.value(i) + "\n");

                    sb.append("attribute name = " + instance.attribute(i).name() + "\n");
                    sb.append("attribute index (" + i + "), value = " + instance.value(i) + "\n\n");

                }
//                System.out.println("*******************************************************");
//                log.info("*******************************************************");
                sb.append("\n*******************************************************\n");
            }

        } catch (Exception e) {

        }

        return sb.toString();
    }

    public void createSingleInstanceDataset() {

        try {
            Instances instances = new Instances(
                    new BufferedReader(
                            new FileReader("E:\\!FAKS\\!MSC\\Speed hookup\\20170216-final\\weka\\joined-data-balanced-set.arff")));

            Instances copy = new Instances(instances);

            while (copy.numInstances() != 1) {
                for (int i = 0; i < copy.numInstances() - 1; i++) {
                    copy.delete(i);
                }

                logger.info("number of instance after second deletion.... " + copy.numInstances());
            }

            logger.info(copy.firstInstance().toString());

            for (int i = 0; i < copy.numAttributes(); i++) {
                copy.firstInstance().setValue(i, 0.0);
                logger.info("\n" + copy.attribute(i).name() + "\n");
            }

            logger.info(copy.firstInstance().toString());

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("E:\\!FAKS\\!MSC\\Speed hookup\\20170216-final\\weka\\single-instance-dataset.arff"));
            writer.write(copy.toString());
            writer.newLine();
            writer.flush();
            writer.close();

        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    public Instance createPairEntryInstance(User male, User female) {
        Instance retVal = null;

        try {
            Instances originalSet = new Instances(
                    new BufferedReader(
                            new FileReader("E:\\!FAKS\\!MSC\\Speed hookup\\20170216-final\\weka\\single-instance-dataset.arff")));

            Instances singleBlankInstance = new Instances(originalSet);
            setUserDataToWekaInstance(male, singleBlankInstance.firstInstance());
            setUserDataToWekaInstance(female, singleBlankInstance.firstInstance());

            retVal = singleBlankInstance.firstInstance();
        }
        catch (Exception e) {

        }

        return retVal;
    }

    public void insertExperimentDataUsersInDb() {

        try {
            Instances males = new Instances(
                    new BufferedReader(
                            new FileReader("E:\\!FAKS\\!MSC\\Speed hookup\\20170216-final\\weka\\male-non-redundant-set.arff")));

            int counter = 0 ;
            for(Instance male : males) {
                logger.info("********************************************* Creating instance [index = " + counter + "]");
                createUserFromWekaInstance(male, Enums.Gender.Male);
                counter++;
            }

            Instances females = new Instances(
                    new BufferedReader(
                            new FileReader("E:\\!FAKS\\!MSC\\Speed hookup\\20170216-final\\weka\\female-non-redundant-set.arff")));

            for (Instance female : females) {
                createUserFromWekaInstance(female, Enums.Gender.Female);
            }


        } catch (Exception e) {
            logger.info(e.getMessage());
        }

    }

    @Transactional
    private User createUserFromWekaInstance(Instance instance, Enums.Gender gender) {
        User user = new User();

        try {
            String firstname = gender == Enums.Gender.Male ?
                    SerbianNames.MALE_FIRSTANAMES[ThreadLocalRandom.current().nextInt(0, SerbianNames.MALE_FIRSTANAMES.length + 1)] :
                    SerbianNames.FEMALE_FIRSTANAMES[ThreadLocalRandom.current().nextInt(0, SerbianNames.FEMALE_FIRSTANAMES.length + 1)];
            user.setFirstname(firstname);
            user.setLastname(SerbianNames.LASTNAMES[ThreadLocalRandom.current().nextInt(0, SerbianNames.LASTNAMES.length + 1)]);
            user.setCity("Novi Sad");
            user.setCountry("Serbia");
            user.setGender(gender);
            user.setAge((int) instance.value(0));
            user.setAboutMe("Hey, my name is " + firstname + " and I have a generic background story like most of boring people out there... ");
            user = userRepository.save(user);

            UserBasicInfo basicInfo = new UserBasicInfo();
            basicInfo.setRace(Enums.Race.values()[((int) instance.value(2))]);
            basicInfo.setField(Enums.Field.values()[((int) instance.value(1))]);
            basicInfo.setCareer(Enums.Career.values()[((int) instance.value(5))]);
//        basicInfo.setGoOut();
            basicInfo.setImprace((int) instance.value(3));
            basicInfo.setImprelig((int) instance.value(4));
            basicInfo.setUser(user);
            basicInfoRepository.save(basicInfo);
            user.setBasicInfo(basicInfo);
//            user = userRepository.save(user);


            UserActivities activities = new UserActivities();
            activities.setSports((int) instance.value(6));
            activities.setTvsports((int) instance.value(7));
            activities.setExcersice((int) instance.value(8));
            activities.setDining((int) instance.value(9));
            activities.setMuseums((int) instance.value(10));
            activities.setArt((int) instance.value(11));
            activities.setHiking((int) instance.value(12));
            activities.setGaming((int) instance.value(13));
            activities.setClubbing((int) instance.value(14));
            activities.setReading((int) instance.value(15));
            activities.setTv((int) instance.value(16));
            activities.setTheater((int) instance.value(17));
            activities.setMovies((int) instance.value(18));
            activities.setConcerts((int) instance.value(19));
            activities.setMusic((int) instance.value(20));
            activities.setShopping((int) instance.value(21));
            activities.setYoga((int) instance.value(22));
            activities.setUser(user);
            activitiesRepository.save(activities);
            user.setActivities(activities);
//            user = userRepository.save(user);

            UserPsychology psychology = new UserPsychology();
            psychology.setAttrA(instance.value(23));
            psychology.setSincA(instance.value(24));
            psychology.setIntelA(instance.value(25));
            psychology.setFunA(instance.value(26));
            psychology.setAmbA(instance.value(27));
            psychology.setSharA(instance.value(28));

            psychology.setAttrB(instance.value(29));
            psychology.setSincB(instance.value(30));
            psychology.setIntelB(instance.value(31));
            psychology.setFunB(instance.value(32));
            psychology.setAmbB(instance.value(33));
            psychology.setSharB(instance.value(34));

            psychology.setAttrC(instance.value(35));
            psychology.setSincC(instance.value(36));
            psychology.setFunC(instance.value(37));
            psychology.setIntelC(instance.value(38));
            psychology.setAmbB(instance.value(39));

            psychology.setUser(user);
            psychologyRepository.save(psychology);
            user.setPsychology(psychology);
            user = userRepository.save(user);
        }
        catch(Exception e) {
            logger.info("Exception message: " +  e.getMessage());
            logger.info("Exception stack trace: " + e.getStackTrace());
        }

        return user;
    }

    public void setUserDataToWekaInstance(User user, Instance instance) {

        if (user.getGender() == Enums.Gender.Male) {
            instance.setValue(0, user.getAge());
            instance.setValue(1, user.getBasicInfo().getField().ordinal());
            instance.setValue(2, user.getBasicInfo().getRace().ordinal());
            instance.setValue(3, user.getBasicInfo().getImprace());
            instance.setValue(4, user.getBasicInfo().getImprelig());
            instance.setValue(5, user.getBasicInfo().getCareer().ordinal());
            instance.setValue(6, user.getActivities().getSports());
            instance.setValue(7, user.getActivities().getTvsports());
            instance.setValue(8, user.getActivities().getExcersice());
            instance.setValue(9, user.getActivities().getDining());
            instance.setValue(10, user.getActivities().getMuseums());
            instance.setValue(11, user.getActivities().getArt());
            instance.setValue(12, user.getActivities().getHiking());
            instance.setValue(13, user.getActivities().getGaming());
            instance.setValue(14, user.getActivities().getClubbing());
            instance.setValue(15, user.getActivities().getReading());
            instance.setValue(16, user.getActivities().getTv());
            instance.setValue(17, user.getActivities().getTheater());
            instance.setValue(18, user.getActivities().getMovies());
            instance.setValue(19, user.getActivities().getConcerts());
            instance.setValue(20, user.getActivities().getMusic());
            instance.setValue(21, user.getActivities().getShopping());
            instance.setValue(22, user.getActivities().getYoga());
            instance.setValue(23, user.getPsychology().getAttrA());
            instance.setValue(24, user.getPsychology().getSincA());
            instance.setValue(25, user.getPsychology().getIntelA());
            instance.setValue(26, user.getPsychology().getFunA());
            instance.setValue(27, user.getPsychology().getAmbA());
            instance.setValue(28, user.getPsychology().getSharA());
            instance.setValue(29, user.getPsychology().getAttrB());
            instance.setValue(30, user.getPsychology().getSincB());
            instance.setValue(31, user.getPsychology().getIntelB());
            instance.setValue(32, user.getPsychology().getFunB());
            instance.setValue(33, user.getPsychology().getAmbB());
            instance.setValue(34, user.getPsychology().getSharB());
            instance.setValue(35, user.getPsychology().getAttrC());
            instance.setValue(36, user.getPsychology().getSincC());
            instance.setValue(37, user.getPsychology().getFunC());
            instance.setValue(38, user.getPsychology().getIntelC());
            instance.setValue(39, user.getPsychology().getAmbC());
        } else {
            instance.setValue(40, user.getAge());
            instance.setValue(41, user.getBasicInfo().getField().ordinal());
            instance.setValue(42, user.getBasicInfo().getRace().ordinal());
            instance.setValue(43, user.getBasicInfo().getImprace());
            instance.setValue(44, user.getBasicInfo().getImprelig());
            instance.setValue(45, user.getBasicInfo().getCareer().ordinal());
            instance.setValue(46, user.getActivities().getSports());
            instance.setValue(47, user.getActivities().getTvsports());
            instance.setValue(48, user.getActivities().getExcersice());
            instance.setValue(49, user.getActivities().getDining());
            instance.setValue(50, user.getActivities().getMuseums());
            instance.setValue(51, user.getActivities().getArt());
            instance.setValue(52, user.getActivities().getHiking());
            instance.setValue(53, user.getActivities().getGaming());
            instance.setValue(54, user.getActivities().getClubbing());
            instance.setValue(55, user.getActivities().getReading());
            instance.setValue(56, user.getActivities().getTv());
            instance.setValue(57, user.getActivities().getTheater());
            instance.setValue(58, user.getActivities().getMovies());
            instance.setValue(59, user.getActivities().getConcerts());
            instance.setValue(60, user.getActivities().getMusic());
            instance.setValue(61, user.getActivities().getShopping());
            instance.setValue(62, user.getActivities().getYoga());
            instance.setValue(63, user.getPsychology().getAttrA());
            instance.setValue(64, user.getPsychology().getSincA());
            instance.setValue(65, user.getPsychology().getIntelA());
            instance.setValue(66, user.getPsychology().getFunA());
            instance.setValue(67, user.getPsychology().getAmbA());
            instance.setValue(68, user.getPsychology().getSharA());
            instance.setValue(69, user.getPsychology().getAttrB());
            instance.setValue(70, user.getPsychology().getSincB());
            instance.setValue(71, user.getPsychology().getIntelB());
            instance.setValue(72, user.getPsychology().getFunB());
            instance.setValue(73, user.getPsychology().getAmbB());
            instance.setValue(74, user.getPsychology().getSharB());
            instance.setValue(75, user.getPsychology().getAttrC());
            instance.setValue(76, user.getPsychology().getSincC());
            instance.setValue(77, user.getPsychology().getFunC());
            instance.setValue(78, user.getPsychology().getIntelC());
            instance.setValue(79, user.getPsychology().getAmbC());
        }

    }

}
