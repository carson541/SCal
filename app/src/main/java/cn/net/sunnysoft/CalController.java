package cn.net.sunnysoft;

import android.util.Log;

import java.util.Calendar;

public class CalController {
    private final static String TAG = "CalController";

    private static CalController sInstance;
    private int mActualYear, mActualMonth;
    private int mYear, mMonth;

    private CalController() {
        Log.d(TAG, "ctor");

        init();
    }

    private void init() {
        Log.d(TAG, "init");

        update();

        mYear = mActualYear;
        mMonth = mActualMonth;
    }

    public void update() {
        Log.d(TAG, "update");

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;

        mActualYear = year;
        mActualMonth = month;
    }

    public static CalController getInstance() {
        if (sInstance == null) {
            sInstance = new CalController();
        }

        return sInstance;
    }

    public void setYear(int year) {
        Log.d(TAG, "setYear to " + year);

        mYear = year;
    }

    public int getYear() {
        return mYear;
    }

    public void setMonth(int month) {
        Log.d(TAG, "setMonth to " + month);

        mMonth = month;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getActualYear() {
        return mActualYear;
    }

    public int getActualMonth() {
        return mActualMonth;
    }
}
