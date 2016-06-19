package com.rise.mealplanner.util;

import com.rise.mealplanner.model.Week;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by rise on 25/11/15.
 */
public class CalendarGenerator {

    private ArrayList<Week> weeks = new ArrayList<>(53);
    private Calendar now = null;
    private int currentWeekNumber = -1;
    private static CalendarGenerator calendarGenerator = null;

    private CalendarGenerator() {

    }

    public static CalendarGenerator getInstance() {
        if(calendarGenerator == null) {
            synchronized (CalendarGenerator.class) {
                calendarGenerator = new CalendarGenerator();
            }
        }

        return calendarGenerator;
    }

    public ArrayList<Week> getWeeksList() {

        for (int index=1; index<=53; index++) {
            Week week = new Week();
            week.setWeekNumber(index);

            now = Calendar.getInstance();
            now.clear();
            now.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            now.set(Calendar.WEEK_OF_YEAR, index);
            now.set(Calendar.DAY_OF_WEEK, now.getFirstDayOfWeek());
            week.setStartOfWeek(now.getTime());

            now.add(Calendar.DAY_OF_YEAR, 6);
            week.setEndOfWeek(now.getTime());

            weeks.add(week);
        }

        return weeks;
    }

    public int getCurrentWeekNumber() {

        Calendar cal = Calendar.getInstance();
        currentWeekNumber = (cal.get(Calendar.WEEK_OF_YEAR) - 1);

        return currentWeekNumber;
    }

    public Week getCurrentWeek() {

        if(currentWeekNumber == -1) {
            currentWeekNumber = getCurrentWeekNumber();
        }

        return weeks.get(currentWeekNumber);
    }

    public Week getWeekDetails(int position) {

        if(weeks == null || weeks.size() == 0) {
            weeks = getWeeksList();
        }

        return  weeks.get(position);
    }
}
