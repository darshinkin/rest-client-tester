package com.sbt.friend.utils;

import org.joda.time.DateTime;

public class CastUtils {
    public static final String ISO_OUT_FORMAT = "yyyy-MM-dd\'T\'HH:mm:ssZ";

    public static String printDateToIso(DateTime dateTime) {
        return dateTime.toString(ISO_OUT_FORMAT);
    }
}
