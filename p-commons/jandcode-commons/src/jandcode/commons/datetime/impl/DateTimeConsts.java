package jandcode.commons.datetime.impl;

public interface DateTimeConsts {

    int HOUR_IN_DAY = 24;
    int MIN_IN_HOUR = 60;
    int SEC_IN_MIN = 60;
    int MSEC_IN_SEC = 1000;
    int MSEC_IN_MIN = MSEC_IN_SEC * SEC_IN_MIN;
    int MSEC_IN_HOUR = MSEC_IN_MIN * MIN_IN_HOUR;
    int MSEC_IN_DAY = MSEC_IN_HOUR * HOUR_IN_DAY;
    int NANOSEC_IN_MSEC = 1000000;

}
