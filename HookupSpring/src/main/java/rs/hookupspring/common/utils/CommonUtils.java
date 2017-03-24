package rs.hookupspring.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Bandjur on 3/21/2017.
 */
public class CommonUtils {
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}