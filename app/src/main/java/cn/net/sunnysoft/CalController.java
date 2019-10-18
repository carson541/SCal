package cn.net.sunnysoft;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class CalController {
    private final static String TAG = "CalController";

    private static CalController sInstance;
    private DayInfo[][] mDayInfos;
    private int mYear, mMonth;
    private int mTodayPos = -1;

    private CalController() {
        Log.d(TAG, "ctor");

        update();
    }

    public void update() {
        Log.d(TAG, "update");

        DayInfo[][] dayinfos = new DayInfo[CalConst.COLS_OF_MONTH]
                [CalConst.DAYS_OF_WEEK];
        int daysCount = 0;
        DayInfo dayInfo;
        for (int i = 0; i < CalConst.COLS_OF_MONTH; i ++) {
            for (int j = 0; j < CalConst.DAYS_OF_WEEK; j++) {
                dayinfos[i][j] = new DayInfo();
            }
        }

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int today = c.get(Calendar.DATE);

        mYear = year;
        mMonth = month;

        /* reserved for week name */
        for (int i = 0; i < CalConst.DAYS_OF_WEEK; i++) {
            dayInfo = dayinfos[daysCount / CalConst.DAYS_OF_WEEK]
                    [daysCount % CalConst.DAYS_OF_WEEK];
            dayInfo.setDay(-1);
            dayInfo.setMonthInfo(DayInfo.UNKNOWN_MONTH);
            daysCount ++;
        }

        /* last month */
        c.set(Calendar.DATE, 1);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int daysOfLastMonth = dayOfWeek - Calendar.SUNDAY;
        if (daysOfLastMonth == 0) {
            daysOfLastMonth = CalConst.DAYS_OF_WEEK;
        }

        c.add(Calendar.DATE, -daysOfLastMonth);
        int day = c.get(Calendar.DATE);
        for (int i = 0; i < daysOfLastMonth; i++) {
            dayInfo = dayinfos[daysCount / CalConst.DAYS_OF_WEEK]
                    [daysCount % CalConst.DAYS_OF_WEEK];
            dayInfo.setDay(day + i);
            dayInfo.setMonthInfo(DayInfo.LAST_MONTH);
            daysCount ++;
        }
        c.add(Calendar.DATE, daysOfLastMonth);

        /* this month */
        c.set(Calendar.DATE, 1);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DATE, -1);
        int lastDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < lastDayOfMonth; i++) {
            dayInfo = dayinfos[daysCount / CalConst.DAYS_OF_WEEK]
                    [daysCount % CalConst.DAYS_OF_WEEK];
            dayInfo.setDay(i + 1);
            dayInfo.setMonthInfo(DayInfo.CURRENT_MONTH);
            if (today == i + 1) {
                mTodayPos = daysCount;
            }
            daysCount ++;
        }

        /* next month */
        c.add(Calendar.DATE, 1);
        for (int i = 0; daysCount < CalConst.COLS_OF_MONTH * CalConst.DAYS_OF_WEEK;
             i++, daysCount ++) {
            dayInfo = dayinfos[daysCount / CalConst.DAYS_OF_WEEK]
                    [daysCount % CalConst.DAYS_OF_WEEK];
            dayInfo.setDay(i + 1);
            dayInfo.setMonthInfo(DayInfo.NEXT_MONTH);
        }

        /* dump */
        int[][] days = new int[CalConst.COLS_OF_MONTH][CalConst.DAYS_OF_WEEK];
        for (int i = 0; i < CalConst.COLS_OF_MONTH; i ++) {
            for (int j = 0; j < CalConst.DAYS_OF_WEEK; j++) {
                days[i][j] = dayinfos[i][j].getDay();
            }
        }
        for (int i = 0; i < CalConst.COLS_OF_MONTH; i++) {
            Log.d(TAG, "days[" + i + "] = " + Arrays.toString(days[i]));
        }

        mDayInfos = dayinfos;
    }

    public static CalController getInstance() {
        if (sInstance == null) {
            sInstance = new CalController();
        }

        return sInstance;
    }

    public String getMonthYearText(Context context) {
        String text;

        Locale locale = context.getResources().getConfiguration().locale;
        Log.d(TAG, "locale: " + locale.getLanguage() + "-" + locale.getCountry());

        if ("zh-CN".equals(locale.getLanguage() + "-" + locale.getCountry())) {
            text = "" + mYear + "年" + mMonth + "月";
        } else {
            text = "" + mYear + "." + mMonth;
        }

        return text;
    }

    public DayInfo[][] getDayInfos() {
        return mDayInfos;
    }

    public String[] getWeekText(Context context) {
        String[] text;
        final String[] zh_text = {"日", "一", "二", "三", "四", "五", "六"};
        final String[] en_text = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        Locale locale = context.getResources().getConfiguration().locale;

        if ("zh-CN".equals(locale.getLanguage() + "-" + locale.getCountry())) {
            text = zh_text;
        } else {
            text = en_text;
        }

        return text;
    }

    public int getTodayPos() {
        return mTodayPos;
    }
}
