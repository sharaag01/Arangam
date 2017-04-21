package com.yessel.arangam.util;

import com.yessel.arangam.model.Segment;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by yasar on 20/12/16.
 */
public class DateSort implements Comparator<Segment> {

    @Override
    public int compare(Segment lhs, Segment rhs) {
        Calendar lhsSegmentDate = DateUtil.convertStringToCalendar(lhs.getPeriod(), "yyyy-MM-dd'T'HH:mm");
        Calendar rhsSegmentDate = DateUtil.convertStringToCalendar(rhs.getPeriod(), "yyyy-MM-dd'T'HH:mm");
        return lhsSegmentDate.compareTo(rhsSegmentDate);
    }
}
