package org.nefure.tools.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author nefure
 * @since 2022/11/25 22:20
 */
public class TimeUtils {

    public static String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return simpleDateFormat.format(new Date());
    }

}
