package com.rise.mealplanner.model;

import java.util.Date;

/**
 * Created by rise on 29/8/15.
 */
public class Week {

    private int weekNumber;
    private Date startOfWeek = null;
    private Date endOfWeek = null;


    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Date getStartOfWeek() {
        return startOfWeek;
    }

    public void setStartOfWeek(Date startOfWeek) {
        this.startOfWeek = startOfWeek;
    }

    public Date getEndOfWeek() {
        return endOfWeek;
    }

    public void setEndOfWeek(Date endOfWeek) {
        this.endOfWeek = endOfWeek;
    }
}
