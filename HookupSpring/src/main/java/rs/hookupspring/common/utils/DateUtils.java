package rs.hookupspring.common.utils;

import rs.hookupspring.common.enums.Enums;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Bandjur on 8/27/2016.
 */
public class DateUtils {

    public static long getDatetimeDifference(Date dt1, Date dt2, Enums.TimeUnit unit) {
        long retVal = 0;
        long diff = 0;
        long diffYears = 0;
        long diffMonths = 0;

        if(dt1.before(dt2)) {
            diff = dt2.getTime() - dt1.getTime();
            diffYears = getDiffYears(dt1, dt2);
        }
        else {
            diff = dt1.getTime() - dt2.getTime();
            diffYears = getDiffYears(dt2, dt1);
        }


        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        int diffInDays = (int) ((dt2.getTime() - dt1.getTime()) / (1000 * 60 * 60 * 24));
//        int diffInDays = (int) (((diffYears * 12) + getDiffMonths(dt1,dt2)* 30) + ((int) ((dt2.getTime() - dt1.getTime()) / (1000 * 60 * 60 * 24))));

        // TODO test case  for different years and months
//        if(unit == Enums.TimeUnit.Year) {
//            retVal = diffYears;
//        }
//        else if(unit == Enums.TimeUnit.Month) {
//            retVal = (diffYears * 12) + getDiffMonths(dt2,dt1) ;
//        }
        if(unit == Enums.TimeUnit.Day) {
            // TODO change this hardcoded stupid code ( * 30)
//            retVal = ((diffYears * 12) + getDiffMonths(dt2,dt1)* 30) + diffInDays;
            retVal = diffInDays;
        }
        else if(unit == Enums.TimeUnit.Hour) {
            retVal = diffInDays*24 + diffHours;
        }
        else if(unit == Enums.TimeUnit.Minute) {
            retVal = ((diffInDays * 24 + diffHours) * 60) + diffMinutes;
        }
        else if(unit == Enums.TimeUnit.Second) {
            retVal = (((diffInDays * 24 + diffHours) * 60) + diffMinutes * 60) + diffSeconds;
        }

        return retVal;
    }


    public static int getDiffMonths(Date first, Date last) {
        if(last.after(first)) {
            return last.getMonth() - first.getMonth();
        }
        else {
            return first.getMonth() - last.getMonth();
        }
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }
}
