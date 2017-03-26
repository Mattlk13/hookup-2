package hookupandroid.common;

import java.util.List;

import hookupandroid.activities.NavDrawerMainActivity;
import hookupandroid.common.enums.PersonRelation;
import hookupandroid.model.User;

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

        for (User user: NavDrawerMainActivity.nonFriends) {
            if(user.getFirebaseUID().equals(uid)) {
                retVal = user;
                retVal.setPersonRelation(PersonRelation.NON_FRIEND);
                return retVal;
            }
        }

        for (User user: NavDrawerMainActivity.pendingHookups) {
            if(user.getFirebaseUID().equals(uid)) {
                retVal = user;
                retVal.setPersonRelation(PersonRelation.PENDING);
                return retVal;
            }
        }

        for (User user: NavDrawerMainActivity.friends) {
            if(user.getFirebaseUID().equals(uid)) {
                retVal = user;
                retVal.setPersonRelation(PersonRelation.FRIEND);
                return retVal;
            }
        }

        for (User user: NavDrawerMainActivity.allUsers) {
            if(user.getFirebaseUID().equals(uid)) {
                retVal = user;
                retVal.setPersonRelation(PersonRelation.PENDING); // TODO: change this logic completely
                return retVal;
            }
        }

        return retVal;
    }

}
