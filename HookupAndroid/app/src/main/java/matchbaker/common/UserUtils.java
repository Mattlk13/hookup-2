package matchbaker.common;

import java.util.List;

import matchbaker.activities.NavDrawerMainActivity;
import matchbaker.common.enums.PersonRelation;
import matchbaker.model.User;

/**
 * Created by Bandjur on 3/26/2017.
 */

public class UserUtils {

    public static User getUser (List<User> list, String uid) {
        User retVal = null;

        for (User user: list) {
            if(user.getFirebaseUID().equals(uid)) {
                return user;
            }
        }

        return retVal;
    }

    public static User getUserFromMainActivityData (String uid) {
        User retVal = null;

        if(NavDrawerMainActivity.nonFriends != null) {
            for (User user : NavDrawerMainActivity.nonFriends) {
                if (user.getFirebaseUID().equals(uid)) {
                    retVal = user;
                    retVal.setPersonRelation(PersonRelation.NON_FRIEND);
                    return retVal;
                }
            }
        }

        if(NavDrawerMainActivity.pendingHookups != null) {
            for (User user : NavDrawerMainActivity.pendingHookups) {
                if (user.getFirebaseUID().equals(uid)) {
                    retVal = user;
                    retVal.setPersonRelation(PersonRelation.PENDING);
                    return retVal;
                }
            }
        }

        if(NavDrawerMainActivity.friends != null) {
            for (User user : NavDrawerMainActivity.friends) {
                if (user.getFirebaseUID().equals(uid)) {
                    retVal = user;
                    retVal.setPersonRelation(PersonRelation.FRIEND);
                    return retVal;
                }
            }
        }

        if(NavDrawerMainActivity.allUsers != null) {
            for (User user : NavDrawerMainActivity.allUsers) {
                if (user.getFirebaseUID().equals(uid)) {
                    retVal = user;
                    retVal.setPersonRelation(PersonRelation.PENDING); // TODO: change this logic completely
                    return retVal;
                }
            }
        }

        return retVal;
    }

}
