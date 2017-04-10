package matchbaker.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import matchbaker.R;

/**
 * Created by Bandjur on 2/23/2017.
 */

public class FragmentTransitionUtils {

    public static void to(Fragment newFragment, AppCompatActivity activity)
    {
        to(newFragment, activity, true);
    }

    public static void to(Fragment newFragment, AppCompatActivity activity, boolean addToBackstack)
    {
        FragmentTransaction transaction = activity.getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.content_nav_drawer_main, newFragment);

        if(addToBackstack) transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void remove(AppCompatActivity activity)
    {
        activity.getSupportFragmentManager().popBackStack();
    }

}
