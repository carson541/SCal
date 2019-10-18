package cn.net.sunnysoft;

public class DayInfo {
    public static final int CURRENT_MONTH = 1;
    public static final int LAST_MONTH = 2;
    public static final int NEXT_MONTH = 3;
    public static final int UNKNOWN_MONTH = 4;

    private int day;
    private int monthInfo;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonthInfo() {
        return monthInfo;
    }

    public void setMonthInfo(int info) {
        this.monthInfo = info;
    }

    public static boolean isCurrentMonth(int monthinfo) {
        return monthinfo == CURRENT_MONTH;
    }
}
