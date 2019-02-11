package com.sbt.friend.utils;

import org.joda.time.DateTime;

public class Dates {

	public static String printDateToIso(DateTime dateTime) {
		return dateTime.toString(com.sbt.friend.server.gwt.util.Dates.DATE_PATTERN_ISO8601);
	}
}
